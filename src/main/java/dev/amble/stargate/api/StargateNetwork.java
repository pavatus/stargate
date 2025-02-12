package dev.amble.stargate.api;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A list of all the known addresses and their corresponding Stargates.
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
	public Optional<Stargate> get(DirectedGlobalPos pos) {
		// find an address that matches
		return lookup.keySet().stream()
				.filter(address -> address.pos().equals(pos))
				.map(this::get)
				.findFirst();
	}
	public Optional<Address> getAddress(String text) {
		return lookup.keySet().stream()
				.filter(address -> address.text().equals(text))
				.findFirst();
	}
	public @Nullable Stargate get(String address) {
		Address addr = lookup.keySet().stream()
				.filter(a -> a.text().equals(address))
				.findFirst().orElse(null);

		return addr == null ? null : this.get(addr);
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

	/**
	 * Gets the nearest Stargate to the source within the given radius.
	 * @param source the source position
	 * @param radius the radius to search within
	 * @return the nearest Stargate, if one exists
	 */
	public Optional<Stargate> getNearTo(GlobalPos source, int radius) {
		for (Stargate gate : this.lookup.values()) {
			DirectedGlobalPos pos = gate.getAddress().pos();

			if (pos.getDimension() != source.getDimension()) continue;

			// check if the blockpos is within radius
			if (pos.getPos().isWithinDistance(source.getPos(), radius)) {
				return Optional.of(gate);
			}
		}

		return Optional.empty();
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		NbtList list = new NbtList();
		lookup.values().forEach(stargate -> list.add(stargate.toNbt()));
		nbt.put("Stargates", list);

		return nbt;
	}
	public StargateNetwork fromNbt(NbtCompound nbt, boolean clear) {
		if (clear) {
			this.lookup.clear();
		}
		NbtList list = nbt.getList("Stargates", NbtElement.COMPOUND_TYPE);
		list.forEach(tag -> {
			Stargate stargate = this.fromNbt((NbtCompound) tag);
			this.lookup.put(stargate.getAddress(), stargate);
		});
		return this;
	}
	protected <T extends Stargate> T fromNbt(NbtCompound nbt) {
		return (T) new Stargate(nbt);
	}

	public static <C, R> R with(BlockEntity entity, ContextManager<C, R> consumer) {
		return StargateNetwork.with(entity.getWorld(), consumer);
	}

	public static <C, R> R with(Entity entity, ContextManager<C, R> consumer) {
		return StargateNetwork.with(entity.getWorld(), consumer);
	}

	public static <C, R> R with(World world, ContextManager<C, R> consumer) {
		return StargateNetwork.with(world.isClient(), consumer, world::getServer);
	}

	@SuppressWarnings("unchecked")
	public static <C, R> R with(boolean isClient, ContextManager<C, R> consumer, Supplier<MinecraftServer> server) {
		StargateNetwork manager = StargateNetwork.getInstance(!isClient);

		if (isClient) {
			return consumer.run((C) MinecraftClient.getInstance(), manager);
		} else {
			return consumer.run((C) server.get(), manager);
		}
	}

	public boolean isServer() {
		return this instanceof ServerStargateNetwork;
	}

	@Override
	public String toString() {
		return "StargateNetwork{" +
				"lookup=" + lookup +
				'}';
	}

	public static StargateNetwork getInstance(boolean isServer) {
		return isServer ? ServerStargateNetwork.getInstance() : ClientStargateNetwork.getInstance();
	}
	public static StargateNetwork getInstance(World world) {
		return getInstance(!world.isClient());
	}
	@FunctionalInterface
	public interface ContextManager<C, R> {
		R run(C c, StargateNetwork manager);
	}
}
