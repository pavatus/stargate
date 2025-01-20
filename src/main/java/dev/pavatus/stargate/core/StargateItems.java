package dev.pavatus.stargate.core;

import dev.pavatus.lib.container.impl.ItemContainer;
import dev.pavatus.lib.container.impl.ItemGroupContainer;
import dev.pavatus.lib.itemgroup.AItemGroup;
import dev.pavatus.stargate.StargateMod;
import net.minecraft.item.ItemGroup;

public class StargateItems extends ItemContainer {
	public static class Groups implements ItemGroupContainer {
		public static final AItemGroup MAIN = AItemGroup.builder(StargateMod.id("main")).build();
	}
}
