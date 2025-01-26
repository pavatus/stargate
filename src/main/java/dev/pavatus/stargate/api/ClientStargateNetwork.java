package dev.pavatus.stargate.api;

import dev.pavatus.stargate.StargateMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ClientStargateNetwork extends StargateNetwork {
	static {
		ClientPlayNetworking.registerGlobalReceiver(PACKET,
				(client, handler, buf, responseSender) -> {
					ClientStargateNetwork.getInstance().receive(buf);
				});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			ClientStargateNetwork.getInstance().reset();
		});
	}

	private void receive(PacketByteBuf buf) {
		NbtCompound nbt = buf.readNbt();

		boolean isAll = buf.readBoolean();
		if (isAll) {
			this.fromNbt(nbt, true);

			StargateMod.LOGGER.debug("Received {} stargates!", this.lookup.size());
			return;
		}

		// a single stargate is received
		Address address = Address.fromNbt(nbt.getCompound("Address"));
		ClientStargate existing = (ClientStargate) this.get(address);
		if (existing != null) {
			existing.age();
		}

		ClientStargate gate = new ClientStargate(nbt);
		this.lookup.put(gate.getAddress(), gate);
		StargateMod.LOGGER.debug("Received stargate {}", gate.getAddress());
	}

	@Override
	protected <T extends Stargate> T fromNbt(NbtCompound nbt) {
		return (T) new ClientStargate(nbt);
	}

	private void reset() {
		this.lookup.clear();
	}

	private static ClientStargateNetwork instance;

	public static ClientStargateNetwork getInstance() {
		if (instance == null) {
			StargateMod.LOGGER.info("Creating new CLIENT PhoneBook instance");
			instance = new ClientStargateNetwork();
		}
		return instance;
	}
}
