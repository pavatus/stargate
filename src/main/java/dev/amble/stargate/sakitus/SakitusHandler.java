package dev.amble.stargate.sakitus;

import dev.pavatus.lib.api.SakitusModInitializer;
import dev.pavatus.lib.register.api.RegistryEvents;
import dev.amble.stargate.api.PointOfOriginRegistry;

public class SakitusHandler implements SakitusModInitializer {
	@Override
	public void onInitializeSakitus() {
		RegistryEvents.INIT.register((registries, b) -> {
			if (b) return;

			registries.register(PointOfOriginRegistry.getInstance());
		});
	}
}
