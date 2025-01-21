package dev.pavatus.stargate.api;

import dev.pavatus.stargate.StargateMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Optional;

/**
 * A list of all the known addresses and their corresponding Stargates.
 * todo - saving/loading
 */
public class PhoneBook {
	private final HashMap<Address, Stargate> lookup;

	public PhoneBook() {
		this.lookup = new HashMap<>();
	}

	/**
	 * Adds a new address to the phone book.
	 * @param address the address to add
	 * @param stargate the Stargate to associate with the address
	 */
	public void add(Address address, Stargate stargate) {
		lookup.put(address, stargate);
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
	public PhoneBook fromNbt(NbtCompound nbt) {
		NbtList list = nbt.getList("Stargates", 10);
		list.forEach(tag -> {
			Stargate stargate = Stargate.fromNbt((NbtCompound) tag);
			this.add(stargate);
		});
		return this;
	}

	private static PhoneBook instance;

	public static PhoneBook getInstance() {
		if (instance == null) {
			StargateMod.LOGGER.info("Creating new PhoneBook instance");
			instance = new PhoneBook();
		}
		return instance;
	}
}
