package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.*;
import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.StargateBlocks;
import dev.pavatus.stargate.core.block.StargateBlock;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StargateBlockEntity extends StargateLinkableBlockEntity implements StargateWrapper, BlockEntityTicker<StargateBlockEntity> {
	public AnimationState ANIM_STATE = new AnimationState();
	private static final Identifier SYNC_GATE_STATE = new Identifier(StargateMod.MOD_ID, "sync_gate_state");
	public int age;

	static {
		ClientPlayNetworking.registerGlobalReceiver(StargateBlockEntity.SYNC_GATE_STATE,
				(client, handler, buf, responseSender) -> {
					if (client.world == null)
						return;

					Stargate.GateState state = Stargate.GateState.values()[buf.readInt()];
					BlockPos stargatePos = buf.readBlockPos();

					if (client.world.getBlockEntity(stargatePos) instanceof StargateBlockEntity stargate)
						stargate.setGateState(state);
				});
	}

	public StargateBlockEntity(BlockPos pos, BlockState state) {
		super(StargateBlockEntities.STARGATE, pos, state);
	}

	@Override
	public void setGateState(Stargate.GateState state) {
		super.setGateState(state);
		this.syncGateState();
	}

	@Nullable @Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (world.isClient()) return ActionResult.SUCCESS;

		// rotate and locking
		Stargate gate = this.getStargate();
		Dialer dialer = gate.getDialer();

		if (player.isSneaking()) {
			dialer.lock();
			player.sendMessage(Text.literal("LOCKED " + dialer.getSelected() + " (" + dialer.getAmountLocked() + "/7)"), false);

			player.sendMessage(Text.literal("THIS IS ").append(gate.getAddress().toGlyphs()).append(Text.literal(" (" + gate.getAddress().text() + ")")), true);

			return ActionResult.SUCCESS;
		}

		dialer.next();
		player.sendMessage(Text.literal("SELECTED " + dialer.getSelected()), true);

		return ActionResult.SUCCESS;
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!(e instanceof LivingEntity entity)) return;

		// teleport the player to the stargate
		StargateCall existing = this.getStargate().getCurrentCall().orElse(null);
		if (existing != null && existing.to != this.getStargate()) {
			existing.to.teleportHere(entity);
		}
	}

	public void onBreak() {
		this.getStargate().dispose();
		this.ref = null;
		this.removeRing();
	}
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) return;

		createRing(StargateBlocks.RING.getDefaultState());

		Direction facing = this.getWorld().getBlockState(this.getPos()).get(StargateBlock.FACING);
		DirectedGlobalPos globalPos = DirectedGlobalPos.create(this.getWorld().getRegistryKey(), this.getPos(), DirectedGlobalPos.getGeneralizedRotation(facing));
		this.setStargate(Stargate.create(new Address(globalPos)));
	}

	/**
	 * Creates a ring of state around the stargate
	 * todo - FIX DIRECTIONS
	 * @param state the state to create the ring with
	 * @return the positions of the blocks created
	 */
	public Set<BlockPos> createRing(BlockState state) {
		int radius = 4; // Adjust the radius as needed
		Set<BlockPos> ringPositions = new HashSet<>();
		BlockPos center = this.getPos().up(radius);
		Direction facing = this.getCachedState().get(StargateBlock.FACING);

		int x = radius;
		int y = 0;
		int radiusError = 1 - x;

		while (x >= y) {
			addCircleBlocks(center, x, y, ringPositions, state, facing);
			y++;

			if (radiusError < 0) {
				radiusError += 2 * y + 1;
			} else {
				x--;
				radiusError += 2 * (y - x + 1);
			}
		}

		return ringPositions;
	}

	private void addCircleBlocks(BlockPos center, int x, int y, Set<BlockPos> ringPositions, BlockState state, Direction facing) {
		World world = this.getWorld();

		BlockPos[] positions = new BlockPos[]{
				center.add(rotate(x, y, facing)),
				center.add(rotate(-x, y, facing)),
				center.add(rotate(x, -y, facing)),
				center.add(rotate(-x, -y, facing)),
				center.add(rotate(y, x, facing)),
				center.add(rotate(-y, x, facing)),
				center.add(rotate(y, -x, facing)),
				center.add(rotate(-y, -x, facing))
		};

		for (BlockPos pos : positions) {
			if (!pos.equals(this.getPos())) {
				ringPositions.add(pos);
				world.setBlockState(pos, state);
			}
		}
	}

	public static BlockPos rotate(int x, int y, Direction facing) {
		return switch (facing) {
			case NORTH -> new BlockPos(x, y, 0);
			case SOUTH -> new BlockPos(-x, y, 0);
			case WEST -> new BlockPos(0, y, x);
			case EAST -> new BlockPos(0, y, -x);
			default -> BlockPos.ORIGIN;
		};
	}
	/**
	 * Removes the ring of "StargateRingBlock" around the stargate
	 * @return the positions of the blocks removed
	 */
	public Set<BlockPos> removeRing() {
		return createRing(Blocks.AIR.getDefaultState());
	}

	private void syncGateState() {
		if (!hasWorld() || world.isClient())
			return;

		PacketByteBuf buf = PacketByteBufs.create();

		buf.writeInt(getGateState().ordinal());
		buf.writeBlockPos(getPos());

		for (PlayerEntity player : world.getPlayers()) {
			ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_GATE_STATE, buf); // safe cast as we know its
			// server
		}
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, StargateBlockEntity blockEntity) {
		if (world.isClient()) {
			age++;

			ANIM_STATE.startIfNotRunning(age);
			return;
		}
		if (world.getServer() == null) return;

		if (world.getServer().getTicks() % 20 == 0) {
			if (this.getStargate() == null) return;
			if (this.getStargate().getState() != Stargate.GateState.OPEN) return;

			// Define the bounding box
			// FIXME - this box sucks, should also be thinner
			Box detectionBox = new Box(
					pos.getX() - 1 + (world.getBlockState(pos).get(StargateBlock.FACING).getAxis() == Direction.Axis.X ? 0 : (world.getBlockState(pos).get(StargateBlock.FACING) == Direction.NORTH ? -1 : 1)),
					pos.getY() + 1,
					pos.getZ() - 1 + (world.getBlockState(pos).get(StargateBlock.FACING).getAxis() == Direction.Axis.Z ? 0 : (world.getBlockState(pos).get(StargateBlock.FACING) == Direction.NORTH ? -1 : 1)),
					pos.getX() + 3 + (world.getBlockState(pos).get(StargateBlock.FACING).getAxis() == Direction.Axis.X ? 0 : (world.getBlockState(pos).get(StargateBlock.FACING) == Direction.NORTH ? 1 : -1)),
					pos.getY() + 4,
					pos.getZ() + 3 + (world.getBlockState(pos).get(StargateBlock.FACING).getAxis() == Direction.Axis.Z ? 0 : (world.getBlockState(pos).get(StargateBlock.FACING) == Direction.NORTH ? 1 : -1))
			);

			// Find entities inside the bounding box
			List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, detectionBox, e -> true);

			for (LivingEntity entity : entities) {
				// teleport the player to the stargate
				StargateCall existing = this.getStargate().getCurrentCall().orElse(null);
				if (existing != null && existing.to != this.getStargate()) {
					existing.to.teleportHere(entity);
				}
			}
		}
	}

}
