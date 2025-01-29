package dev.pavatus.stargate.compat;

import com.mojang.blaze3d.platform.GlDebugInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {

    private static final boolean HAS_AIT = doesModExist("ait");

    public static boolean doesModExist(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public static boolean hasAIT() {
        return HAS_AIT;
    }
}