package dev.pavatus.stargate.core;

import dev.pavatus.lib.block.ABlockSettings;
import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.datagen.util.PickaxeMineable;
import dev.pavatus.lib.item.AItemSettings;
import dev.pavatus.stargate.core.block.StargateBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class StargateBlocks extends BlockContainer {
	public static final Block STARGATE = new StargateBlock(ABlockSettings.create());
	@Override
	public Item.Settings createBlockItemSettings(Block block) {
		return new AItemSettings().group(StargateItems.Groups.MAIN);
	}
}
