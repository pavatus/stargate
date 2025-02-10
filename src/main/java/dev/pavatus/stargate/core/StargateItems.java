package dev.pavatus.stargate.core;

import dev.pavatus.lib.container.impl.ItemContainer;
import dev.pavatus.lib.container.impl.ItemGroupContainer;
import dev.pavatus.lib.item.AItemSettings;
import dev.pavatus.lib.itemgroup.AItemGroup;
import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.core.item.DialerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

public class StargateItems extends ItemContainer {
	public static final Item SPECTRAL_PROJECTOR = new DialerItem(new AItemSettings().maxCount(1).group(Registries.ITEM_GROUP.get(ItemGroups.FUNCTIONAL)));;

	public static class Groups implements ItemGroupContainer {
		public static final AItemGroup MAIN = AItemGroup.builder(StargateMod.id("main")).build();
	}
}
