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
		return getStargate().getState();
	}

	void setStargate(Stargate gate);
}
