package dev.pavatus.stargate.api;

import dev.pavatus.stargate.StargateMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ClientStargateNetwork extends StargateNetwork{
	static {
		ClientPlayNetworking.registerGlobalReceiver(PACKET,
				(client, handler, buf, responseSender) -> {
					ClientStargateNetwork.getInstance().receive(buf);
				});
	}

	private void receive(PacketByteBuf buf) {
		NbtCompound nbt = buf.readNbt();
		this.fromNbt(nbt);

		StargateMod.LOGGER.debug("Received {} stargates!", this.lookup.size());
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
