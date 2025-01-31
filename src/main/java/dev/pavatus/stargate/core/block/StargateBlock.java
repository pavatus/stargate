package dev.pavatus.stargate.core.block;

import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.core.StargateSounds;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import dev.pavatus.stargate.core.item.StargateLinkableItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StargateBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public StargateBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!(player.getStackInHand(hand).getItem() instanceof StargateLinkableItem) && world.getBlockEntity(pos) instanceof StargateBlockEntity be && hand == Hand.MAIN_HAND) {
			return be.onUse(state, world, pos, player);
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			be.onEntityCollision(state, world, pos, entity);
		}

		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			be.onEntityCollision(state, world, pos, entity);
		}

		super.onSteppedOn(world, pos, state, entity);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}


	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof StargateBlockEntity) {
				((StargateBlockEntity) be).onBreak();
			}
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof StargateBlockEntity) {
			((StargateBlockEntity) be).onBreak();
		}

		super.onBroken(world, pos, state);
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof StargateBlockEntity) {
			((StargateBlockEntity) be).onBreak();
		}

		super.onDestroyedByExplosion(world, pos, explosion);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			be.onPlaced(world, pos, state, placer, itemStack);
		}

		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new StargateBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state,
																  @NotNull BlockEntityType<T> type) {
		return (world1, blockPos, blockState, ticker) -> {
			if (ticker instanceof StargateBlockEntity exterior)
				exterior.tick(world, blockPos, blockState, exterior);
		};
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		if (!(world.getBlockEntity(pos) instanceof StargateBlockEntity be)) return;
		if (!be.hasStargate()) return;
		if (be.getGateState() != Stargate.GateState.OPEN) return;

		if (random.nextInt(100) < 5) {
			be.getStargate().get().playSound(StargateSounds.WORMHOLE_LOOP, 1.0f, 1.0f);
		}
	}
}
