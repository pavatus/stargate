package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.core.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DHDBlockEntity extends BlockEntity implements BlockEntityTicker<DHDBlockEntity> {
    public DHDBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.DHD, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, DHDBlockEntity blockEntity) {

    }
}
