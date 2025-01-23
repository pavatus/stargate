package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.*;
import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.StargateBlocks;
import dev.pavatus.stargate.core.block.StargateBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StargateBlockEntity extends BlockEntity implements StargateWrapper, BlockEntityTicker<StargateBlockEntity> {
	private Stargate stargate;
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
	public Stargate getStargate() {
		if (this.stargate == null) {
			// try to find existing
			StargateNetwork network = StargateNetwork.getInstance(this.getWorld());
			this.stargate = network.get(GlobalPos.create(this.getWorld().getRegistryKey(), this.getPos())).orElseGet(() -> {
				if (!network.isServer()) return null;

				return Stargate.create(new Address(GlobalPos.create(this.getWorld().getRegistryKey(), this.getPos())));
			});

			if (this.stargate == null) {
				// owch
				return null;
			}

			StargateMod.LOGGER.info("Created/Found stargate at {}", this.getPos());
		}

		return this.stargate;
	}

	@Override
	public void setGateState(Stargate.GateState state) {
		if (state.equals(this.getGateState())) return;
		this.getStargate().setState(state);

		this.markDirty();
		this.syncGateState();

		if (this.getWorld() instanceof ServerWorld serverWorld) {
			serverWorld.getChunkManager().markForUpdate(this.pos);
		}
	}

	@Nullable @Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		nbt.put("Stargate", this.getStargate().toNbt());
		nbt.putInt("GateState", this.getGateState().ordinal());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains("Stargate") && this.stargate == null && this.hasWorld()) {
			this.stargate = Stargate.fromNbt(nbt.getCompound("Stargate"));

			StargateMod.LOGGER.info("Loaded stargate ({}) at {}", this.stargate.getAddress(), this.getPos());
			StargateNetwork.getInstance(this.getWorld()).add(this.stargate);
		}
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (world.isClient()) return ActionResult.SUCCESS;

		// temporary test code

		// dial a random address that isnt us
		Stargate chosen = this.getStargate();
		int counter = 0;
		while (chosen == this.getStargate() && counter < 10) {
			chosen = StargateNetwork.getInstance(world).getRandom();
			counter++;
		}

		StargateCall call = this.getStargate().dial(chosen).orElse(null);

		if (call == null) {
			player.sendMessage(Text.literal("TARGET UNAVAILABLE"), true);

			return ActionResult.FAIL;
		}

		call.start();
		player.sendMessage(Text.literal("CALL CONNECTED TO ").append(chosen.getAddress().toGlyphs()), true);

		call.onEnd(c -> {
			player.sendMessage(Text.literal("WORMHOLE CLOSED"), true);
		});

		return ActionResult.SUCCESS;
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!(e instanceof LivingEntity entity)) return;

		// teleport the player to the stargate
		StargateCall existing = this.getStargate().getCurrentCall().orElse(null);
		if (existing != null && existing.to != this.getStargate()) {
			existing.to.teleportHere(entity);

			if (entity instanceof ServerPlayerEntity player) {
				player.sendMessage(Text.literal("HAVE A GOOD TRIP"), true);
			}
		}
	}

	public void onBreak() {
		this.getStargate().dispose();
		this.stargate = null;
		this.removeRing();
	}
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) return;

		createRing(StargateBlocks.RING.getDefaultState());
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

		int x = radius;
		int y = 0;
		int radiusError = 1 - x;

		while (x >= y) {
			addCircleBlocks(center, x, y, ringPositions, state);
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

	private void addCircleBlocks(BlockPos center, int x, int y, Set<BlockPos> ringPositions, BlockState state) {
		World world = this.getWorld();

		BlockPos[] positions = new BlockPos[]{
				center.add(x, y, 0),
				center.add(-x, y, 0),
				center.add(x, -y, 0),
				center.add(-x, -y, 0),
				center.add(y, x, 0),
				center.add(-y, x, 0),
				center.add(y, -x, 0),
				center.add(-y, -x, 0)
		};

		for (BlockPos pos : positions) {
			if (!pos.equals(this.getPos())) {
				ringPositions.add(pos);
				world.setBlockState(pos, state);
			}
		}
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
			// Define the bounding box
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

					if (entity instanceof ServerPlayerEntity player) {
						player.sendMessage(Text.literal("HAVE A GOOD TRIP"), true);
					}
				}
			}
		}
	}

}
