package dev.pavatus.stargate.compat;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {
    private static final boolean HAS_TECH_ENERGY = doesModExist("team_reborn_energy");

    public static boolean doesModExist(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public static boolean hasTechEnergy() {
        return HAS_TECH_ENERGY;
    }
}