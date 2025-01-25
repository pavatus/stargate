package dev.pavatus.stargate.core;

import dev.pavatus.lib.block.ABlockSettings;
import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.container.impl.NoBlockItem;
import dev.pavatus.lib.datagen.util.PickaxeMineable;
import dev.pavatus.lib.item.AItemSettings;
import dev.pavatus.stargate.core.block.DHDBlock;
import dev.pavatus.stargate.core.block.StargateBlock;
import dev.pavatus.stargate.core.block.StargateRingBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class StargateBlocks extends BlockContainer {
	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	public static final Block STARGATE = new StargateBlock(ABlockSettings.create().nonOpaque());
	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	public static final Block DHD = new DHDBlock(ABlockSettings.create().nonOpaque());
	@NoBlockItem
	public static final Block RING = new StargateRingBlock(ABlockSettings.create().nonOpaque());

	@Override
	public Item.Settings createBlockItemSettings(Block block) {
		return new AItemSettings().group(StargateItems.Groups.MAIN);
	}
}
