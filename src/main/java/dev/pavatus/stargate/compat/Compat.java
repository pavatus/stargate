package dev.pavatus.stargate.compat;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.compat.ait.AITHandler;
import net.fabricmc.api.ModInitializer;

public class Compat implements ModInitializer {
	@Override
	public void onInitialize() {
		if (DependencyChecker.hasAIT()) {
			AITHandler.getInstance().init();
		}

		StargateMod.LOGGER.info("STARGATE COMPAT LOADED");
	}
}
