package dev.amble.stargate.api;


import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class StargateServerData extends PersistentState {
	static {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			get(server).markDirty();
		});
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("Network", ServerStargateNetwork.getInstance().toNbt());

		return nbt;
	}
	public static StargateServerData loadNbt(NbtCompound nbt) {
		StargateServerData data = new StargateServerData();

		if (nbt.contains("Network")) {
			ServerStargateNetwork.getInstance().fromNbt(nbt.getCompound("Network"), true);
		}

		return data;
	}

	public static StargateServerData get(MinecraftServer server) {
		PersistentStateManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
		StargateServerData state = manager.getOrCreate(StargateServerData::loadNbt, StargateServerData::new, StargateMod.MOD_ID);

		state.markDirty();
		return state;
	}
	public static StargateServerData get() {
		if (!ServerLifecycleHooks.isServer()) {
			StargateMod.LOGGER.warn("Attempted to get server data without server!");

			return new StargateServerData();
		}

		return get(ServerLifecycleHooks.get());
	}

	public static void init() {

	}
}
