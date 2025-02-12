package dev.amble.stargate.client.util;

import dev.amble.stargate.api.Stargate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ShakeUtil {
	private static final float SHAKE_CLAMP = 45.0f; // Adjust this value to set the maximum shake angle
	private static final float SHAKE_INTENSITY = 0.5f; // Adjust this value to control the intensity of the shake
	private static final int MAX_DISTANCE = 16; // The radius from the stargate where the player will feel the shake

	/**
	 * Shakes based off the distance of the player from the stargate
	 */
	public static void shakeFromGate() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null)
			return;

		Stargate nearest = ClientStargateUtil.getNearest().orElse(null);
		if (nearest == null)
			return;

		float multiplier = (nearest.getDialer().getAmountLocked() / 7f);

		if (nearest.getState() == Stargate.GateState.PREOPEN) {
			multiplier = 1f;
		}

		if (multiplier == 0f) return;


		shake((1f - ((float) (nearest.distanceFrom(player.getBlockPos()) / MAX_DISTANCE))) * multiplier);
	}

	public static void shakeFromEverywhere() {
		shake(0.1f);
	}

	public static void shake(float scale) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null)
			return;

		float targetPitch = getShakeX(client.player.getPitch(), scale);
		float targetYaw = getShakeY(client.player.getYaw(), scale);

		client.player.setPitch(MathHelper.lerp(SHAKE_INTENSITY, client.player.getPitch(), targetPitch));
		client.player.setYaw(MathHelper.lerp(SHAKE_INTENSITY, client.player.getYaw(), targetYaw));
	}

	private static float getShakeY(float baseYaw, float scale) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null)
			return baseYaw;

		float temp = (client.player.getRandom().nextFloat() * scale);
		float shakeYaw = baseYaw + (client.player.getRandom().nextBoolean() ? temp : -temp);

		return MathHelper.clamp(shakeYaw, baseYaw - SHAKE_CLAMP, baseYaw + SHAKE_CLAMP);
	}

	private static float getShakeX(float basePitch, float scale) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null)
			return basePitch;

		float temp = (client.player.getRandom().nextFloat() * (scale / 2));
		float shakePitch = basePitch + (client.player.getRandom().nextBoolean() ? temp : -temp);

		return MathHelper.clamp(shakePitch, basePitch - SHAKE_CLAMP, basePitch + SHAKE_CLAMP);
	}
}
