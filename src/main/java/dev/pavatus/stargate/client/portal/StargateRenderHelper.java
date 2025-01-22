package dev.pavatus.stargate.client.portal;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;

public class StargateRenderHelper {

    @Environment(EnvType.CLIENT)
    public static boolean getIsStencilEnabled(Framebuffer renderTarget) {
        return ((StencilFrameBuffer) renderTarget).stargate$getIsStencilBufferEnabled();
    }

    @Environment(EnvType.CLIENT)
    public static void setIsStencilEnabled(Framebuffer renderTarget, boolean cond) {
        ((StencilFrameBuffer) renderTarget).stargate$setIsStencilBufferEnabledAndReload(cond);
    }

}