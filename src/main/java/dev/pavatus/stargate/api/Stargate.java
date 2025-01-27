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
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Stargate implements StargateCall.Wiretap, Disposable {
	private final Address address;
	private final List<Subscriber> subscribers;
	private StargateCall call;
	private GateState state;
	private Dialer dialer;

	protected Stargate(Address address) {
		this.address = address;
		this.subscribers = new ArrayList<>();
		this.state = GateState.CLOSED;
		this.dialer = new Dialer(this).onCompleted(this::handleDialerComplete);
	}
	public Stargate(NbtCompound nbt) {
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
	public Optional<StargateCall> dialImmediately(Stargate target) {
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

		this.sync();

		return Optional.of(this.call);
	}

	public void dial(Stargate target, Consumer<Optional<StargateCall>> callback) {
		this.setState(GateState.DIALING);
		this.dialer.dial(target.getAddress());

		this.subscribe(new Subscriber() {
			@Override
			public void onCallCreate(Stargate gate, StargateCall call) {

			}

			@Override
			public void onCallStart(Stargate gate, StargateCall call) {
				callback.accept(Optional.of(call));
			}

			@Override
			public void onCallEnd(Stargate gate, StargateCall call) {
			}

			@Override
			public void onStateChange(Stargate gate, GateState before, GateState after) {

			}
		});
	}

	public void dial(Stargate target) {
		this.dial(target, call -> {});
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
		this.sync();
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
	@Override
	public void dispose() {
		if (this.call != null) {
			this.call.end();
		}
		StargateNetwork.getInstance(true).remove(this.address);
	}

	/**
	 * Teleports an entity to this stargate
	 * @param entity entity to teleport
	 * @param offset offset from the stargate
	 * @return whether the teleport was successful
	 */
	public boolean teleportHere(LivingEntity entity, BlockPos offset) {
		DirectedGlobalPos pos = this.getAddress().pos();

		// ignore offsets in this direction
		Direction dir = pos.getRotationDirection();
		if (dir == Direction.NORTH || dir == Direction.SOUTH) {
			offset = offset.east(offset.getX());
		} else {
			offset = offset.south(offset.getZ());
		}

		pos = pos.offset(offset.getX(), offset.getY(), offset.getZ());
		pos = pos.offset(pos.getRotationDirection());

		// dont bother for now
		// pos = WorldUtil.locateSafe(pos, WorldUtil.GroundSearch.CEILING, true);

		TeleportUtil.teleport(entity, pos);

		this.playSound(StargateSounds.GATE_TELEPORT, 0.25f, 1f);

		return true;
	}

	/**
	 * Teleports an entity to this stargate
	 * @param entity entity to teleport
	 * @return whether the teleport was successful
	 */
	public boolean teleportHere(LivingEntity entity) {
		return this.teleportHere(entity, BlockPos.ORIGIN);
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

	public void sync() {
		ServerStargateNetwork.getInstance().sync(this);
	}

	public double distanceFrom(BlockPos pos) {
		return Math.sqrt(this.address.pos().getPos().getSquaredDistance(pos.getX(), pos.getY(), pos.getZ()));
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
	public void loadNbt(NbtCompound nbt) {
		if (nbt.contains("State")) {
			this.state = GateState.values()[nbt.getInt("State")];
		}

		if (nbt.contains("Dialer")) {
			this.dialer = new Dialer(this, nbt.getCompound("Dialer")).onCompleted(this::handleDialerComplete);
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
		DIALING,
		BROKEN
	}

	public void subscribe(Subscriber subscriber) {
		this.subscribers.add(subscriber);
	}

	public interface Subscriber {
		void onCallCreate(Stargate gate, StargateCall call);
		void onCallStart(Stargate gate, StargateCall call);
		void onCallEnd(Stargate gate, StargateCall call);
		void onStateChange(Stargate gate, GateState before, GateState after);
	}
}
