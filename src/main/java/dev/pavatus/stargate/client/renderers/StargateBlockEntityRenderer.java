package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.StargateCall;
import dev.pavatus.stargate.client.models.StargateModel;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {
    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargate.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargate_emission.png");
    private final StargateModel model;
    public StargateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new StargateModel(StargateModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(StargateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tickDelta / 200f * 360f));
        this.model.portal.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(TEXTURE)), 0xf000f0, overlay, 1, 1, 1, 1);
        matrices.pop();
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }
}