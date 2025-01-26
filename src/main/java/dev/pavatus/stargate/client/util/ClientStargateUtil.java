package dev.pavatus.stargate.client.util;

import dev.pavatus.stargate.api.ClientStargate;
import dev.pavatus.stargate.api.ClientStargateNetwork;
import dev.pavatus.stargate.api.Stargate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClientStargateUtil {
	static {
		ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
	}

	private static Stargate nearest;
	private static int ticks = 0;

	private static Stargate findNearestStargate() {
		nearest = null;

		ClientStargateNetwork network = ClientStargateNetwork.getInstance();
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return null;

		nearest = network.getNearTo(GlobalPos.create(player.clientWorld.getRegistryKey(), player.getBlockPos()), 16).orElse(null);
		return nearest;
	}

	public static void tick() {
		ticks++;

		if (ticks % 20 == 0) {
			findNearestStargate();
		}

		ShakeUtil.shakeFromGate();
	}

	public static Optional<Stargate> getNearest() {
		return Optional.ofNullable(nearest);
	}

	public static void init() {

	}
}

