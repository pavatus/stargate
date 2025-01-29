package dev.pavatus.stargate.compat;

import dev.pavatus.stargate.StargateMod;
import net.fabricmc.api.ModInitializer;

public class Compat implements ModInitializer {
	@Override
	public void onInitialize() {
		

		StargateMod.LOGGER.info("STARGATE COMPAT LOADED");
	}
}