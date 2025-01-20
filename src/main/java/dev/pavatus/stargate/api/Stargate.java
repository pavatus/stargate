package dev.pavatus.stargate.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.Optional;

public class Stargate implements StargateCall.Wiretap {
	private final Address address;
	private StargateCall call;

	protected Stargate(Address address) {
		this.address = address;
	}
	protected Stargate(NbtCompound nbt) {
		this.address = Address.fromNbt(nbt.getCompound("Address"));
		this.call = null;
	}

	/**
	 * @return whether this stargate is available for dialing
	 */
	public boolean isAvailable() {
		return this.call != null;
	}

	/**
	 * Attempts to call another stargate
	 * This creates and links a call, but does not start it.
	 * @param target star gate to call
	 * @return the call if successful
	 */
	public Optional<StargateCall> call(Stargate target) {
		if (!this.isAvailable()) {
			return Optional.empty();
		}
		if (!target.isAvailable()) {
			return Optional.empty();
		}

		this.call = new StargateCall(this, target);
		target.call = this.call;

		this.call.subscribe(this);
		this.call.subscribe(target);

		return Optional.of(this.call);
	}

	/**
	 * Attempts to call another stargate
	 * This creates and links a call, but does not start it.
	 * @param target address to call
	 * @return the call if successful
	 */
	public Optional<StargateCall> call(Address target) {
		return PhoneBook.getInstance().getOptional(target)
				.flatMap(this::call);
	}

	public Optional<StargateCall> getCurrentCall() {
		return Optional.ofNullable(this.call);
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public void onCallStart(StargateCall call) {
		if (this.call != call) return;
	}

	@Override
	public void onCallEnd(StargateCall call) {
		if (this.call != call) return;
		this.call = null;
	}

	/**
	 * Disposes of the stargate, unlinks it from everything and prepares it for garbage collection
	 */
	public void dispose() {
		if (this.call != null) {
			this.call.end();
		}
		PhoneBook.getInstance().remove(this.address);
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.put("Address", address.toNbt());

		return nbt;
	}

	public static Stargate fromNbt(NbtCompound nbt) {
		return new Stargate(nbt);
	}

	public PacketByteBuf writeToPacket(PacketByteBuf buf) {
		buf.writeNbt(this.toNbt());
		return buf;
	}
	public Stargate readFromPacket(PacketByteBuf buf) {
		return new Stargate(buf.readNbt());
	}

	public static Stargate create(Address address) {
		Stargate created = new Stargate(address);
		PhoneBook.getInstance().add(created);
		return created;
	}
}
