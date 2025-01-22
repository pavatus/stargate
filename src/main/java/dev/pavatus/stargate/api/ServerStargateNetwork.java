package dev.pavatus.stargate.api;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.stargate.StargateMod;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Collection;
import java.util.Optional;

public class ServerStargateNetwork extends StargateNetwork{
	@Override
	public Optional<Stargate> remove(Address address) {
		Optional<Stargate> removed = super.remove(address);

		removed.ifPresent(stargate -> {
			this.sync();
		});

		return removed;
	}

	@Override
	public boolean add(Address address, Stargate stargate) {
		boolean success = super.add(address, stargate);

		if (success) {
			this.sync();
		}

		return success;
	}

	/**
	 * Syncs the network with all clients
	 */
	private void sync(Collection<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing {} stargates!", this.lookup.size());

		NbtCompound nbt = this.toNbt();
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt);

		for (ServerPlayerEntity player : targets) {
			ServerPlayNetworking.send(player, PACKET, buf);
		}
	}
	private void sync() {
		if (ServerLifecycleHooks.get() == null) {
			Scheduler.get().runTaskLater(this::sync, TimeUnit.TICKS, 1); // queue the sync when the server exists
			return;
		}

		this.sync(PlayerLookup.all(ServerLifecycleHooks.get()));
	}

	private static ServerStargateNetwork instance;

	public static ServerStargateNetwork getInstance() {
		if (instance == null) {
			StargateMod.LOGGER.info("Creating new SERVER PhoneBook instance");
			instance = new ServerStargateNetwork();
		}
		return instance;
	}
}
