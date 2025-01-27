package dev.pavatus.stargate.sakitus;

import dev.pavatus.lib.api.SakitusModInitializer;
import dev.pavatus.lib.register.api.RegistryEvents;
import dev.pavatus.stargate.api.PointOfOriginRegistry;

public class SakitusHandler implements SakitusModInitializer {
	@Override
	public void onInitializeSakitus() {
		RegistryEvents.INIT.register((registries, b) -> {
			if (b) return;

			registries.register(PointOfOriginRegistry.getInstance());
		});
	}
}
