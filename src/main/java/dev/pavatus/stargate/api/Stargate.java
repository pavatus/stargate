package dev.pavatus.stargate.api;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.lib.util.TeleportUtil;
import dev.pavatus.stargate.core.StargateSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stargate implements StargateCall.Wiretap {
	private final Address address;
	private final List<Subscriber> subscribers;
	private StargateCall call;
	private GateState state;
	private Dialer dialer;

	protected Stargate(Address address) {
		this.address = address;
		this.subscribers = new ArrayList<>();
		this.state = GateState.CLOSED;
		this.dialer = new Dialer().onCompleted(this::handleDialerComplete);
	}
	protected Stargate(NbtCompound nbt) {
		this(Address.fromNbt(nbt.getCompound("Address")));

		this.loadNbt(nbt);
	}

	/**
	 * @return whether this stargate is available for dialing
	 */
	public boolean isAvailable() {
		return this.call == null;
	}

	/**
	 * Attempts to call another stargate
	 * This creates and links a call, but does not start it.
	 * @param target star gate to call
	 * @return the call if successful
	 */
	public Optional<StargateCall> dial(Stargate target) {
		if (this == target || this.address.equals(target.address)) {
			// cannot call self
			return Optional.empty();
		}
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

		this.subscribers.forEach(s -> s.onCallCreate(this, this.call));

		return Optional.of(this.call);
	}

	/**
	 * Attempts to call another stargate
	 * This creates and links a call, but does not start it.
	 * @param target address to call
	 * @return the call if successful
	 */
	public Optional<StargateCall> dial(Address target) {
		return StargateNetwork.getInstance(true).getOptional(target)
				.flatMap(this::dial);
	}

	public Optional<StargateCall> getCurrentCall() {
		return Optional.ofNullable(this.call);
	}

	public Address getAddress() {
		return address;
	}

	public GateState getState() {
		return state;
	}
	public Dialer getDialer() {
		return dialer;
	}

	public void setState(GateState state) {
		GateState before = this.state;
		this.state = state;
		this.subscribers.forEach(s -> s.onStateChange(this, before, state));
	}

	@Override
	public void onCallStart(StargateCall call) {
		if (this.call != call) return;

		this.subscribers.forEach(s -> s.onCallStart(this, call));
	}

	@Override
	public void onCallEnd(StargateCall call) {
		if (this.call != call) return;

		this.subscribers.forEach(s -> s.onCallEnd(this, call));

		this.call = null;
	}

	/**
	 * Disposes of the stargate, unlinks it from everything and prepares it for garbage collection
	 */
	public void dispose() {
		if (this.call != null) {
			this.call.end();
		}
		StargateNetwork.getInstance(true).remove(this.address);
	}

	/**
	 * Teleports an entity to this stargate
	 * @param entity entity to teleport
	 * @return whether the teleport was successful
	 */
	public boolean teleportHere(LivingEntity entity) {
		DirectedGlobalPos pos = this.getAddress().pos();
		TeleportUtil.teleport(entity, pos.offset(pos.getRotationDirection()));

		this.playSound(StargateSounds.GATE_TELEPORT, 0.25f, 1f);

		return true;
	}

	/**
	 * plays a sound at this stargates position
	 * serverside only
	 */
	public void playSound(SoundEvent sound, float volume, float pitch) {
		MinecraftServer server = ServerLifecycleHooks.get();
		if (server == null) return;
		ServerWorld world = server.getWorld(this.address.pos().getDimension());
		if (world == null) return;

		world.playSound(null, this.address.pos().getPos(), sound, SoundCategory.BLOCKS, volume, pitch);
	}

	private void handleDialerComplete(Dialer d) {
		if (this.call != null) {
			this.call.end();
		}

		StargateCall call = d.complete(this, ServerLifecycleHooks.get().getOverworld()).orElse(null);
		if (call == null) return;
		call.start();
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.put("Address", address.toNbt());
		nbt.putInt("State", state.ordinal());
		nbt.put("Dialer", dialer.toNbt());

		return nbt;
	}

	public static Stargate fromNbt(NbtCompound nbt) {
		return new Stargate(nbt);
	}
	private void loadNbt(NbtCompound nbt) {
		if (nbt.contains("State")) {
			this.state = GateState.values()[nbt.getInt("State")];
		}

		if (nbt.contains("Dialer")) {
			this.dialer = new Dialer(nbt.getCompound("Dialer")).onCompleted(this::handleDialerComplete);
		}
	}

	public PacketByteBuf writeToPacket(PacketByteBuf buf) {
		buf.writeNbt(this.toNbt());
		return buf;
	}
	public Stargate readFromPacket(PacketByteBuf buf) {
		return new Stargate(buf.readNbt());
	}

	@Override
	public String toString() {
		return "Stargate{" +
				"address=" + address +
				", call=" + call +
				", state=" + state +
				'}';
	}

	public static Stargate create(Address address) {
		Stargate created = new Stargate(address);
		StargateNetwork.getInstance(true).add(created);
		return created;
	}

	public enum GateState {
		CLOSED,
		OPEN,
		PREOPEN,
		BROKEN
	}

	public interface Subscriber {
		void onCallCreate(Stargate gate, StargateCall call);
		void onCallStart(Stargate gate, StargateCall call);
		void onCallEnd(Stargate gate, StargateCall call);
		void onStateChange(Stargate gate, GateState before, GateState after);
	}
}
