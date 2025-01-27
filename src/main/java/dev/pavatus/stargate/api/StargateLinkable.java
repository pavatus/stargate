package dev.pavatus.stargate.api;

import net.minecraft.world.World;

/**
 * Indicates that this class holds a stargate
 */
public interface StargateLinkable {
	StargateRef getStargate();
	default void setGateState(Stargate.GateState state) {
		if (!this.hasStargate()) return;

		getStargate().get().setState(state);
	}
	default Stargate.GateState getGateState() {
		if (!this.hasStargate()) return Stargate.GateState.CLOSED;

		return getStargate().get().getState();
	}

	default boolean hasStargate() {
		return this.getStargate() != null && this.getStargate().isPresent();
	}
	default boolean isLinked() {
		return this.hasStargate();
	}

	void setStargate(StargateRef gate);
	default void link(StargateRef gate) {
		this.setStargate(gate);
		this.onLinked();
	}
	default void link(String id, World world) {
		this.link(StargateRef.createAs(world, id));
	}

	default void onLinked() {

	}
}
