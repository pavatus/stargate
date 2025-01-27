package dev.pavatus.stargate.compat.ait;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.stargate.api.Address;
import dev.pavatus.stargate.api.ServerStargateNetwork;
import dev.pavatus.stargate.api.Stargate;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.data.DirectedBlockPos;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class AITHandler {
	private final HashMap<UUID, Stargate> tardisLookup = new HashMap<>();

	static {
		TardisEvents.DOOR_MOVE.register((tardis, newPos, oldPos) -> {
			AITHandler handler = AITHandler.getInstance();

			// remove old stargate and create a new one
			// this does mean the address changes, since their positions arent mutable but oh well
			handler.getStargate(tardis).ifPresent(existing -> handler.remove(tardis));
			handler.getOrCreate(tardis);
		});
	}

	public void init() {

	}

	private Optional<Stargate> loadExisting(ServerTardis tardis) {
		// we just need to find a stargate with the same world
		return ServerStargateNetwork.getInstance().stream()
				.filter(stargate -> stargate.getAddress().pos().getDimension().equals(tardis.getInteriorWorld().getRegistryKey()))
				.findFirst();
	}
	private Stargate createStargate(ServerTardis tardis) {
		Stargate gate = loadExisting(tardis).orElse(Stargate.create(createAddress(tardis)));

		// do stuff

		return gate;
	}
	private Address createAddress(ServerTardis tardis) {
		DirectedBlockPos pos = tardis.getDesktop().getDoorPos();

		return new Address(DirectedGlobalPos.create(tardis.getInteriorWorld().getRegistryKey(), pos.getPos(), pos.getRotation()));
	}
	public Stargate getOrCreate(ServerTardis tardis) {
		return tardisLookup.computeIfAbsent(tardis.getUuid(), uuid -> createStargate(tardis));
	}
	public Optional<Stargate> getStargate(ServerTardis tardis) {
		return Optional.ofNullable(tardisLookup.get(tardis.getUuid()));
	}
	public Optional<ServerTardis> getTardis(Stargate stargate) {
		UUID found = null;

		for (UUID uuid : tardisLookup.keySet()) {
			if (tardisLookup.get(uuid).equals(stargate)) {
				found = uuid;
				break;
			}
		}

		if (found == null) {
			return Optional.empty();
		}

		return Optional.ofNullable(ServerTardisManager.getInstance().demandTardis(ServerLifecycleHooks.get(), found));
	}
	private void remove(ServerTardis tardis) {
		Stargate stargate = tardisLookup.remove(tardis.getUuid());
		if (stargate == null) return;

		stargate.dispose();
	}

	private static AITHandler instance;

	public static AITHandler getInstance() {
		if (instance == null) {
			instance = new AITHandler();
		}

		return instance;
	}
}
