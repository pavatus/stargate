package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.Address;
import dev.pavatus.stargate.api.Dialer;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.client.models.DHDModel;
import dev.pavatus.stargate.client.models.StargateModel;
import dev.pavatus.stargate.client.portal.PortalRendering;
import dev.pavatus.stargate.core.block.DHDBlock;
import dev.pavatus.stargate.core.block.StargateBlock;
import dev.pavatus.stargate.core.block.entities.DHDBlockEntity;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class DHDBlockEntityRenderer implements BlockEntityRenderer<DHDBlockEntity> {
    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd_emission.png");
    private final DHDModel model;
    public DHDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DHDModel(DHDModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(DHDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        if (!entity.hasStargate()) {
            matrices.pop();
            return;
        }

        Dialer dialer = entity.getStargate().get().getDialer();

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int middleIndex = Dialer.GLYPHS.length / 2;
        for (int i = 0; i < Dialer.GLYPHS.length; i++) {
            int j = Dialer.GLYPHS.length - i + dialer.getSelectedIndex() - middleIndex;

            if (j < 0) {
                j += Dialer.GLYPHS.length;
            } else if (j >= Dialer.GLYPHS.length) {
                j -= Dialer.GLYPHS.length;
            }

            boolean isInDial = dialer.contains(Dialer.GLYPHS[i]);
            boolean isSelected = i == dialer.getSelectedIndex();

            int colour = 0x4f4f4f;

            if (isInDial) {
                colour = 0xedc093;
            }
            if (isSelected) {
                colour = 0xedb334;
            }

            matrices.push();
            double angle = 2 * Math.PI * j / Dialer.GLYPHS.length;
            matrices.translate(Math.sin(angle) * 45, Math.cos(angle) * 44, 0);
            OrderedText text = Address.toGlyphs(String.valueOf(Dialer.GLYPHS[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, 0, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, 0xF000F0);
            matrices.pop();
        }
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }
}