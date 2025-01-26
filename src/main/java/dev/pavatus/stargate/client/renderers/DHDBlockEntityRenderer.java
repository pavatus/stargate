package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.client.models.DHDModel;
import dev.pavatus.stargate.client.models.StargateModel;
import dev.pavatus.stargate.client.portal.PortalRendering;
import dev.pavatus.stargate.core.block.DHDBlock;
import dev.pavatus.stargate.core.block.StargateBlock;
import dev.pavatus.stargate.core.block.entities.DHDBlockEntity;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
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
        //Stargate.GateState state = entity.getStargate() != null ? entity.getGateState() : Stargate.GateState.CLOSED;
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        /*if (state.equals(Stargate.GateState.OPEN)) */this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }
}