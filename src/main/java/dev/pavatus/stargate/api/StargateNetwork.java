package dev.pavatus.stargate.api;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

/**
 * A list of all the known addresses and their corresponding Stargates.
 * todo - saving/loading
 */
public abstract class StargateNetwork {
	public static Identifier PACKET = StargateMod.id("sync_books");
	protected final HashMap<Address, Stargate> lookup;

	protected StargateNetwork() {
		this.lookup = new HashMap<>();
	}

	/**
	 * Adds a new address to the phone book.
	 * @param address the address to add
	 * @param stargate the Stargate to associate with the address
	 */
	public boolean add(Address address, Stargate stargate) {
		if (this.lookup.containsKey(address)) {
			StargateMod.LOGGER.warn("Address {} already exists in the phone book!", address);
			return false;
		}

		lookup.put(address, stargate);
		return true;
	}
	/**
	 * Adds a new stargate to the phone book
	 * @param stargate the Stargate to add
	 */
	public void add(Stargate stargate) {
		this.add(stargate.getAddress(), stargate);
	}

	public Stargate get(Address address) {
		return lookup.get(address);
	}
	public Optional<Stargate> getOptional(Address address) {
		return Optional.ofNullable(this.get(address));
	}
	public Optional<Stargate> get(GlobalPos pos) {
		// find an address that matches
		return lookup.keySet().stream()
				.filter(address -> address.pos().equals(pos))
				.map(this::get)
				.findFirst();
	}

	/**
	 * Gets the Stargate associated with the address, or adds it if it doesn't exist.
	 * @param address the address to look up
	 * @param stargate the Stargate to add if the address doesn't exist
	 * @return the Stargate associated with the address
	 */
	public Stargate getOrAdd(Address address, Stargate stargate) {
		return lookup.computeIfAbsent(address, k -> stargate);
	}

	/**
	 * Removes an address from the phone book.
	 * @param address the address to remove
	 * @return the Stargate associated with the address
	 */
	public Optional<Stargate> remove(Address address) {
		return Optional.ofNullable(lookup.remove(address));
	}

	public Stargate getRandom() {
		int chosen = (int) (Math.random() * lookup.size());
		return (Stargate) lookup.values().toArray()[chosen];
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		NbtList list = new NbtList();
		lookup.values().forEach(stargate -> list.add(stargate.toNbt()));
		nbt.put("Stargates", list);

		return nbt;
	}
	public StargateNetwork fromNbt(NbtCompound nbt) {
		this.lookup.clear();
		NbtList list = nbt.getList("Stargates", 10);
		list.forEach(tag -> {
			Stargate stargate = Stargate.fromNbt((NbtCompound) tag);
			lookup.put(stargate.getAddress(), stargate);
		});
		return this;
	}

	public boolean isServer() {
		return this instanceof ServerStargateNetwork;
	}

	public static StargateNetwork getInstance(boolean isServer) {
		return isServer ? ServerStargateNetwork.getInstance() : ClientStargateNetwork.getInstance();
	}
	public static StargateNetwork getInstance(World world) {
		return getInstance(!world.isClient());
	}
}
