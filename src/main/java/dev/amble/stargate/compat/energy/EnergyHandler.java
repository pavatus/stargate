package dev.amble.stargate.compat.energy;

import dev.amble.stargate.core.StargateBlockEntities;
import team.reborn.energy.api.EnergyStorage;

public class EnergyHandler {
	public static void init() {
		EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> (be.hasStargate() && (be.getStargate().get() instanceof StargateRebornEnergy energy) ? energy.getStorage() : null), StargateBlockEntities.STARGATE);
	}
}
