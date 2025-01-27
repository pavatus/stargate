package dev.pavatus.stargate.compat.ait;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.stargate.api.*;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.data.DirectedBlockPos;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class AITHandler {
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
		gate.subscribe(new Stargate.Subscriber() {
			@Override
			public void onCallCreate(Stargate gate, StargateCall call) {

			}

			@Override
			public void onCallStart(Stargate gate, StargateCall call) {
				tardis.travel().dematerialize();
			}

			@Override
			public void onCallEnd(Stargate gate, StargateCall call) {

			}

			@Override
			public void onStateChange(Stargate gate, Stargate.GateState before, Stargate.GateState after) {
				if (after == Stargate.GateState.PREOPEN) {
					tardis.alarm().enabled().set(true);
				}
				if (after == Stargate.GateState.OPEN) {
					tardis.alarm().enabled().set(false);
				}
			}
		});

		return gate;
	}
	private Address createAddress(ServerTardis tardis) {
		DirectedBlockPos pos = tardis.getDesktop().getDoorPos();

		return new Address(DirectedGlobalPos.create(tardis.getInteriorWorld().getRegistryKey(), pos.getPos(), pos.getRotation()));
	}
	public StargateRef getOrCreate(ServerTardis tardis) {
		if (!(tardis instanceof StargateLinkable link)) throw new IllegalStateException("Tardis does not implement StargateLinkable");
		if (link.hasStargate()) {
			return link.getStargate();
		}

		Stargate stargate = createStargate(tardis);
		link.setStargate(StargateRef.createAs(ServerLifecycleHooks.get().getOverworld(), stargate));
		return link.getStargate();
	}
	public Optional<Stargate> getStargate(ServerTardis tardis) {
		if (!(tardis instanceof StargateLinkable link)) return Optional.empty();
		if (!link.hasStargate()) return Optional.empty();

		return Optional.of(link.getStargate().get());
	}
	private void remove(ServerTardis tardis) {
		Stargate stargate = getStargate(tardis).orElse(null);
		if (stargate == null) return;

		stargate.dispose();
		((StargateLinkable) tardis).setStargate(null);
	}

	private static AITHandler instance;

	public static AITHandler getInstance() {
		if (instance == null) {
			instance = new AITHandler();
		}

		return instance;
	}
}
