package dev.amble.stargate.core.item;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.StargateLinkable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * right click to link to stargate
 * right click on stargate to dial
 */
public class DialerItem extends StargateLinkableItem{
	public DialerItem(Settings settings) {
		super(settings, true);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().isClient()) return super.useOnBlock(context);

		ServerWorld world = (ServerWorld) context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack hand = context.getStack();

		if (world.getBlockEntity(pos) instanceof StargateLinkable be) {
			if (!be.hasStargate()) return ActionResult.FAIL;

			if (!isLinked(hand)) {
				this.link(hand, be.getStargate().get());
				return ActionResult.SUCCESS;
			}

			Stargate target = StargateLinkableItem.getStargate(world, hand);
			if (target == null) return ActionResult.FAIL;

			be.getStargate().get().dial(target);
			hand.decrement(1);
			return ActionResult.SUCCESS;
		}

		return super.useOnBlock(context);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("tooltip.stargate.dialer.hint").formatted(Formatting.DARK_PURPLE)
				.formatted(Formatting.ITALIC));

		super.appendTooltip(stack, world, tooltip, context);
	}
}
