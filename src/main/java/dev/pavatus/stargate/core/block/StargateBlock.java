package dev.pavatus.stargate.core.block;

import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class StargateBlock extends BlockWithEntity {
	public StargateBlock(Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return StargateBlockEntities.STARGATE.instantiate(pos, state);
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
}
