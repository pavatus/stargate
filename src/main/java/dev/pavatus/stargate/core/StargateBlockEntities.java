package dev.pavatus.stargate.core;

import dev.pavatus.lib.container.impl.BlockEntityContainer;
import dev.pavatus.stargate.core.block.entities.DHDBlockEntity;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class StargateBlockEntities implements BlockEntityContainer {
	public static BlockEntityType<StargateBlockEntity> STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.STARGATE).build();
	public static BlockEntityType<DHDBlockEntity> DHD = FabricBlockEntityTypeBuilder.create(DHDBlockEntity::new, StargateBlocks.DHD).build();
}
