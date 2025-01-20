package dev.pavatus.stargate.core;

import dev.pavatus.lib.container.impl.BlockEntityContainer;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class StargateBlockEntities implements BlockEntityContainer {
	public static BlockEntityType<StargateBlockEntity> STARGATE = FabricBlockEntityTypeBuilder.create(StargateBlockEntity::new, StargateBlocks.STARGATE).build();
}
