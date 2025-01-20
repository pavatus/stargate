package dev.pavatus.stargate.core.block;

import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class StargateBlock extends BlockWithEntity {
	public StargateBlock(Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return StargateBlockEntities.STARGATE.instantiate(pos, state);
	}
}
