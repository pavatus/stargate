package dev.pavatus.stargate.core.item;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.stargate.api.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class StargateLinkableItem extends Item {
	private final boolean showTooltip;

	public StargateLinkableItem(Settings settings, boolean showTooltip) {
		super(settings);
		this.showTooltip = showTooltip;
	}

	public void link(ItemStack stack, Stargate gate) {
		this.link(stack, gate.getAddress().text());
	}

	public void link(ItemStack stack, String address) {
		stack.getOrCreateNbt().putString("Address", address);
	}

	public static boolean isLinked(ItemStack stack) {
		return stack.getOrCreateNbt().contains("Address");
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		this.handleTooltip(stack, tooltip);
		super.appendTooltip(stack, world, tooltip, context);
	}

	private void handleTooltip(ItemStack stack, List<Text> tooltip) {
		if (!showTooltip)
			return;

		String id = StargateLinkableItem.getStargateAddressFromKey(stack, "Address");

		if (id == null)
			return;

		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("tooltip.stargate.link_item.holdformoreinfo").formatted(Formatting.GRAY)
					.formatted(Formatting.ITALIC));
			return;
		}

		ClientStargate stargate = (ClientStargate) ClientStargateNetwork.getInstance().get(id);

		if (stargate != null) {
			tooltip.add(Text.literal("STARGATE: ").formatted(Formatting.BLUE));

			Address address = stargate.getAddress();
			DirectedGlobalPos pos = address.pos();

			tooltip.add(Text.literal("> ").append(address.toGlyphs())
					.formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("> " + pos.getDimension().getValue().getPath()).formatted(Formatting.DARK_GRAY)); // todo cleanup
			tooltip.add(Text.literal("> " + pos.getPos().getX() + ", " + pos.getPos().getY() + ", " + pos.getPos().getZ())
					.formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("> " + pos.getRotationDirection().name()).formatted(Formatting.DARK_GRAY));
		}
	}

	public static boolean isOf(World world, ItemStack stack, Stargate tardis) {
		return StargateLinkableItem.getStargate(world, stack) == tardis;
	}

	public static Stargate getStargate(World world, ItemStack stack) {
		return StargateLinkableItem.getStargateFromKey(world, stack, "Address");
	}

	public static String getStargateAddressFromKey(ItemStack stack, String path) {
		NbtCompound nbt = stack.getOrCreateNbt();
		NbtElement element = nbt.get(path);

		if (element == null)
			return null;

		return nbt.getString(path);
	}

	public static Stargate getStargateFromKey(World world, ItemStack stack, String path) {
		return StargateLinkableItem.getStargate(world, StargateLinkableItem.getStargateAddressFromKey(stack, path));
	}

	public static Stargate getStargate(World world, String address) {
		return StargateNetwork.with(world, (o, manager) -> manager.get(address));
	}
}
