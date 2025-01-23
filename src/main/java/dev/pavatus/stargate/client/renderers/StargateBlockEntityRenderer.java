package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.client.models.StargateModel;
import dev.pavatus.stargate.client.portal.PortalRendering;
import dev.pavatus.stargate.core.block.StargateBlock;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
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
        matrices.translate(0.5f, 2.65f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1.75f, 1.75f, 1.75f);
        Stargate.GateState state = entity.getStargate() != null ? entity.getGateState() : Stargate.GateState.CLOSED;
        this.model.animateStargateModel(entity, state, entity.age);
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up().up().up().up());
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), lightAbove, overlay, 1, 1, 1, 1);
        PortalRendering.renderPortal(entity, state, matrices, EMISSION, this.model.portal);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        this.model.portal.visible =       state == Stargate.GateState.OPEN;
        this.model.chevron_one.visible =  true;//state == Stargate.GateState.OPEN;
        this.model.chevron_two.visible =  state == Stargate.GateState.OPEN;
        this.model.chevron_three.visible= state == Stargate.GateState.OPEN;
        this.model.chevron_four.visible = state == Stargate.GateState.OPEN;
        this.model.chevron_five.visible = state == Stargate.GateState.OPEN;
        this.model.chevron_six.visible =  state == Stargate.GateState.OPEN;
        this.model.chevron_seven.visible =state == Stargate.GateState.OPEN;
        this.model.chevron_eight.visible =state == Stargate.GateState.OPEN;
        this.model.chevron_nine.visible = state == Stargate.GateState.OPEN;
        matrices.pop();
    }
}