package dev.pavatus.stargate.compat;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.compat.energy.EnergyHandler;
import net.fabricmc.api.ModInitializer;

public class Compat implements ModInitializer {
	@Override
	public void onInitialize() {
		if (DependencyChecker.hasTechEnergy()) {
			EnergyHandler.init();
		}

		StargateMod.LOGGER.info("STARGATE COMPAT LOADED");
	}
}