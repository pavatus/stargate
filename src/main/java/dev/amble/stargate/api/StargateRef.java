package dev.amble.stargate.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

// thanks to theo for his tardis code lol
public class StargateRef implements Disposable {
	private final LoadFunc load;
	private String address;
	private Stargate cached;

	public StargateRef(String address, LoadFunc load) {
		this.address = address;
		this.load = load;
	}
	public StargateRef(Stargate stargate, LoadFunc load) {
		if (stargate != null)
			this.address = stargate.getAddress().text();

		this.load = load;
		this.cached = stargate;
	}
	public StargateRef(Address address, LoadFunc load) {
		this(address.text(), load);
	}

	public Stargate get() {
		if (this.cached != null && !this.shouldInvalidate())
			return this.cached;

		this.cached = this.load.apply(this.address);
		return this.cached;
	}

	private boolean shouldInvalidate() {
		return this.cached.isAged();
	}

	public String getAddress() {
		return address;
	}

	public boolean isPresent() {
		return this.get() != null;
	}

	public boolean isEmpty() {
		return this.get() == null;
	}

	/**
	 * @return the result of the function, {@literal null} otherwise.
	 */
	public <T> Optional<T> apply(Function<Stargate, T> consumer) {
		if (this.isPresent())
			return Optional.of(consumer.apply(this.cached));

		return Optional.empty();
	}

	public void ifPresent(Consumer<Stargate> consumer) {
		if (this.isPresent())
			consumer.accept(this.get());
	}

	public static StargateRef createAs(Entity entity, Stargate gate) {
		return new StargateRef(gate, real -> StargateNetwork.with(entity, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(Entity entity, String uuid) {
		return new StargateRef(uuid, real -> StargateNetwork.with(entity, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(BlockEntity blockEntity, Stargate gate) {
		return new StargateRef(gate,
				real -> StargateNetwork.with(blockEntity, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(BlockEntity blockEntity, String uuid) {
		return new StargateRef(uuid,
				real -> StargateNetwork.with(blockEntity, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(World world, Stargate tardis) {
		return new StargateRef(tardis, real -> StargateNetwork.with(world, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(World world, String uuid) {
		return new StargateRef(uuid, real -> StargateNetwork.with(world, (o, manager) -> manager.get(real)));
	}

	@Override
	public void dispose() {
		this.cached = null;
	}

	public interface LoadFunc extends Function<String, Stargate> {}
}
