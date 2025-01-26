package dev.pavatus.stargate.api;

/**
 * Indicates that this class holds a stargate
 */
public interface StargateWrapper {
	Stargate getStargate();
	default void setGateState(Stargate.GateState state) {
		getStargate().setState(state);
	}
	default Stargate.GateState getGateState() {
		if (!this.hasStargate()) return Stargate.GateState.CLOSED;

		return getStargate().getState();
	}

	void setStargate(Stargate gate);
	default boolean hasStargate() {
		return this.getStargate() != null;
	}
}
