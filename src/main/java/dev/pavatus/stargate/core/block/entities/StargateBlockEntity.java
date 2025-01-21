package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.*;
import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.StargateBlocks;
import dev.pavatus.stargate.core.block.StargateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class StargateBlockEntity extends BlockEntity implements StargateWrapper {
	private Stargate stargate;

	public StargateBlockEntity(BlockPos pos, BlockState state) {
		super(StargateBlockEntities.STARGATE, pos, state);
	}



	@Override
	public Stargate getStargate() {
		if (this.stargate == null) {
			this.stargate = Stargate.create(new Address(GlobalPos.create(this.getWorld().getRegistryKey(), this.getPos())));
			StargateMod.LOGGER.info("Created stargate at {}", this.getPos());
		}

		return this.stargate;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		nbt.put("Stargate", this.getStargate().toNbt());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains("Stargate")) {
			this.stargate = Stargate.fromNbt(nbt.getCompound("Stargate"));
			StargateNetwork.getInstance().add(this.stargate);
		}
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (world.isClient()) return ActionResult.SUCCESS;

		// temporary test code

		// dial a random address that isnt us
		Stargate chosen = this.getStargate();
		int counter = 0;
		while (chosen == this.getStargate() && counter < 10) {
			chosen = StargateNetwork.getInstance().getRandom();
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
	 * @param state the state to create the ring with
	 * @return the positions of the blocks created
	 */
	public Set<BlockPos> createRing(BlockState state) {
		int width = 4;
		int height = 4;

		Direction facing = this.getCachedState().get(StargateBlock.FACING);
		Direction direction = facing.rotateYClockwise();
		BlockPos start = this.getPos().offset(direction.getOpposite(), width / 2).down(height / 2); // bottom left

		Set<BlockPos> ringPositions = new HashSet<>();
		BlockPos current = start.up(height/2);

		// Move along the specified direction for the given width
		for (int i = 0; i < width; i++) {
			if (!current.equals(this.getPos())) {
				ringPositions.add(current);
				this.getWorld().setBlockState(current, state);
			}
			current = current.offset(direction);
		}

		// Move upwards for the given height
		for (int i = 0; i < height; i++) {
			if (!current.equals(this.getPos())) {
				ringPositions.add(current);
				this.getWorld().setBlockState(current, state);
			}
			current = current.up();
		}

		// Move back along the opposite direction for the given width
		Direction oppositeDirection = direction.getOpposite();
		for (int i = 0; i < width; i++) {
			if (!current.equals(this.getPos())) {
				ringPositions.add(current);
				this.getWorld().setBlockState(current, state);
			}
			current = current.offset(oppositeDirection);
		}

		// Move downwards to link back to the original corner
		for (int i = 0; i < height; i++) {
			if (!current.equals(this.getPos())) {
				ringPositions.add(current);
				this.getWorld().setBlockState(current, state);
			}
			current = current.down();
		}

		return ringPositions;
	}

	/**
	 * Removes the ring of "StargateRingBlock" around the stargate
	 * @return the positions of the blocks removed
	 */
	public Set<BlockPos> removeRing() {
		return createRing(Blocks.AIR.getDefaultState());
	}
}
