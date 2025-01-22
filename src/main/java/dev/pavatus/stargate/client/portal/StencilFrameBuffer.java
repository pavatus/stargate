package dev.pavatus.stargate.client.portal;

public interface StencilFrameBuffer {
    boolean stargate$getIsStencilBufferEnabled();

    void stargate$setIsStencilBufferEnabledAndReload(boolean cond);
}
