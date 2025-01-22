package dev.pavatus.stargate.client.portal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.util.Window;

public class PortalHandler {
    public Framebuffer afbo;

    public void setupFramebuffer() {
        Window window = MinecraftClient.getInstance().getWindow();

        if (afbo == null || afbo.textureWidth != window.getFramebufferWidth() || afbo.textureHeight != window.getFramebufferHeight()) {
            afbo = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);;
        }

        afbo.beginWrite(false);
        afbo.checkFramebufferStatus();

        if (!StargateRenderHelper.getIsStencilEnabled(afbo)) {
            StargateRenderHelper.setIsStencilEnabled(afbo, true);
        }
    }
}
