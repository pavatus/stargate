package dev.pavatus.stargate.datagen;

import dev.pavatus.lib.datagen.loot.SakitusBlockLootTable;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class StargateBlockLootProvider extends SakitusBlockLootTable {
	public StargateBlockLootProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}
}
