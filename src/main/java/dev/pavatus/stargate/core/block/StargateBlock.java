package dev.pavatus.stargate.core.block;

import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be && hand == Hand.MAIN_HAND) {
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
}
