package dev.amble.stargate.core.block;

import dev.amble.stargate.core.block.entities.DHDBlockEntity;
import dev.amble.stargate.core.item.StargateLinkableItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DHDBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public DHDBlock(Settings settings) {
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
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DHDBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state,
																  @NotNull BlockEntityType<T> type) {
		return (world1, blockPos, blockState, ticker) -> {
			if (ticker instanceof DHDBlockEntity dhd)
				dhd.tick(world, blockPos, blockState, dhd);
		};
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof DHDBlockEntity be) {
			be.onPlaced(world, pos, state, placer, itemStack);

			be.markNeedsControl();
		}

		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		super.onBroken(world, pos, state);

		if (world.getBlockEntity(pos) instanceof DHDBlockEntity dhd) {
			dhd.onBroken();
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!(player.getStackInHand(hand).getItem() instanceof StargateLinkableItem) && world.getBlockEntity(pos) instanceof DHDBlockEntity be && hand == Hand.MAIN_HAND) {
			return be.onUse(state, world, pos, player);
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

}
