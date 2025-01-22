package dev.pavatus.stargate.client.portal;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.opengl.GL11;
public class PortalRendering {
    public static PortalHandler PORTAL_HANDLER = new PortalHandler();
    public static StargateBufferBuilderStorage STARGATE_BUF_BUILDER_STORAGE = new StargateBufferBuilderStorage();

    public static void renderPortal(StargateBlockEntity stargateBlockEntity, MatrixStack stack, Identifier frameTex, ModelPart mask) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        //Stargate stargate = stargateBlockEntity.getStargate();

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        MinecraftClient.getInstance().getFramebuffer().endWrite();

        PORTAL_HANDLER.setupFramebuffer();

        PortalRendering.copyFramebuffer(MinecraftClient.getInstance().getFramebuffer(), PORTAL_HANDLER.afbo);

        VertexConsumerProvider.Immediate portalProvider = STARGATE_BUF_BUILDER_STORAGE.getPortalVertexConsumer();

        // Enable stencil testing and clear the stencil buffer
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilMask(0xFF);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        RenderSystem.depthMask(true);
        stack.push();
        stack.translate(0, -1, 0.1);
        stack.scale(1, 1, 1);
        Stargate.GateState state = stargateBlockEntity.getStargate() != null ? stargateBlockEntity.getGateState() : Stargate.GateState.CLOSED;
        if (state != Stargate.GateState.OPEN)
            mask.render(stack, portalProvider.getBuffer(RenderLayer.getEndGateway()), 0xf000f0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        else {
            mask.render(stack, portalProvider.getBuffer(RenderLayer.getEntityTranslucentCull(frameTex)), 0xf000f0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        }
        portalProvider.draw();
        stack.pop();
        copyDepth(PORTAL_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        PORTAL_HANDLER.afbo.beginWrite(false);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);

        stack.push();
        //stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) MinecraftClient.getInstance().player.age / 100 * 360f));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        stack.translate(0, 0, 100);
        PortalUtil util = new PortalUtil("watery");
        if (state == Stargate.GateState.OPEN) {
            util.renderPortalInterior(stack);
        }
        portalProvider.draw();
        stack.pop();

        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        PortalRendering.copyColor(PORTAL_HANDLER.afbo, MinecraftClient.getInstance().getFramebuffer());

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        RenderSystem.depthMask(true);

        stack.pop();
    }

    private static void copyFramebuffer(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_DEPTH_BUFFER_BIT | GlConst.GL_COLOR_BUFFER_BIT, GlConst.GL_NEAREST);
    }

    private static void copyColor(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_COLOR_BUFFER_BIT, GlConst.GL_NEAREST);
    }

    private static void copyDepth(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_DEPTH_BUFFER_BIT, GlConst.GL_NEAREST);
    }
}
