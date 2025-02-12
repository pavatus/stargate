package dev.amble.stargate.core.block;

import dev.pavatus.lib.api.ICantBreak;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StargateRingBlock extends Block implements ICantBreak {
	public StargateRingBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}
}
