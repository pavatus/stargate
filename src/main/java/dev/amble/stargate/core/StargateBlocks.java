package dev.amble.stargate.core;

import dev.pavatus.lib.block.ABlockSettings;
import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.container.impl.NoBlockItem;
import dev.pavatus.lib.datagen.util.AutomaticModel;
import dev.pavatus.lib.datagen.util.NoEnglish;
import dev.pavatus.lib.datagen.util.PickaxeMineable;
import dev.pavatus.lib.item.AItemSettings;
import dev.amble.stargate.core.block.DHDBlock;
import dev.amble.stargate.core.block.StargateBlock;
import dev.amble.stargate.core.block.StargateRingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

public class StargateBlocks extends BlockContainer {
	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@AutomaticModel(justItem = true)
	public static final Block STARGATE = new StargateBlock(ABlockSettings.create().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(5.5F, 10.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));
	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@NoEnglish
	public static final Block DHD = new DHDBlock(ABlockSettings.create().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));
	@NoBlockItem
	public static final Block RING = new StargateRingBlock(ABlockSettings.create().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));

	@Override
	public Item.Settings createBlockItemSettings(Block block) {
		return new AItemSettings().group(Registries.ITEM_GROUP.get(ItemGroups.FUNCTIONAL));
	}
}
