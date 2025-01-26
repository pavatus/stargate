package dev.pavatus.stargate.core;

import dev.pavatus.lib.container.impl.ItemContainer;
import dev.pavatus.lib.container.impl.ItemGroupContainer;
import dev.pavatus.lib.item.AItemSettings;
import dev.pavatus.lib.itemgroup.AItemGroup;
import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.core.item.DialerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class StargateItems extends ItemContainer {
	public static final Item SPECTRAL_PROJECTOR = new DialerItem(new AItemSettings().maxCount(1).group(Groups.MAIN));;

	public static class Groups implements ItemGroupContainer {
		public static final AItemGroup MAIN = AItemGroup.builder(StargateMod.id("main")).build();
	}
}
