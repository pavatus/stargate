package dev.pavatus.stargate.compat;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {
    public static boolean doesModExist(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}