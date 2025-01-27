package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.Address;
import dev.pavatus.stargate.api.Dialer;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.api.StargateRef;
import dev.pavatus.stargate.client.models.StargateModel;
import dev.pavatus.stargate.client.portal.PortalRendering;
import dev.pavatus.stargate.core.block.StargateBlock;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

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
        Stargate.GateState state = entity.hasStargate() ? entity.getGateState() : Stargate.GateState.CLOSED;

        matrices.translate(0.5f, 2.65f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1.75f, 1.75f, 1.75f);


        if (entity.hasStargate()) {
            Stargate gate = entity.getStargate().get();
            Dialer dialer = gate.getDialer();
            this.setFromDialer(dialer, state);

            this.renderGlyphs(matrices, vertexConsumers, gate);
        }

        this.model.animateStargateModel(entity, state, entity.age);
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up().up().up().up());
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), lightAbove, overlay, 1, 1, 1, 1);
        PortalRendering.renderPortal(entity, state, matrices, EMISSION, this.model.portal);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        this.model.portal.visible = state == Stargate.GateState.OPEN;

        matrices.pop();
    }

    private void setFromDialer(Dialer dialer, Stargate.GateState state) {
        List<ModelPart> chevrons = List.of(model.chevron_two, model.chevron_three, model.chevron_four, model.chevron_seven, model.chevron_eight, model.chevron_nine, model.chevron_one, model.chevron_five, model.chevron_six);

        chevrons.forEach(chevron -> chevron.visible = state == Stargate.GateState.OPEN);

        for (int i = 0; i < dialer.getAmountLocked(); i++) {
            chevrons.get(i).visible = true;
        }
    }

    private void renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Stargate gate) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        Direction direction = gate.getAddress().pos().getRotationDirection();
        boolean northern = direction == Direction.NORTH || direction == Direction.SOUTH;
        int multiplier = (direction == Direction.WEST || direction == Direction.NORTH) ? 1 : -1;
        float xOffset = northern ? direction.getOffsetX() * 0.3f * multiplier : direction.getOffsetZ() * 0.3f * multiplier;
        float zOffset = northern ? direction.getOffsetZ() * 0.3f * multiplier : direction.getOffsetX() * 0.3f * multiplier;

        Dialer dialer = gate.getDialer();
        matrices.push();
        matrices.translate(0, -0.95f, 0);
        matrices.translate(xOffset, 0.05f, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);
        // TODO fix the rotation stuff here. - Loqor
        int middleIndex = Dialer.GLYPHS.length / 2;
        float selectedRot = 180 + (float) (27.7f * (0.5 * dialer.getSelectedIndex()));
        float rot = dialer.getSelectedIndex() > -1 ? selectedRot :
                MathHelper.wrapDegrees(MinecraftClient.getInstance().player.age / 100f * 360f);
        rot = rot + (14f * dialer.getRotationProgress());
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rot));
        //System.out.println(rot);
        for (int i = 0; i < Dialer.GLYPHS.length; i++) {
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
            double angle = 2 * Math.PI * i / Dialer.GLYPHS.length;
            matrices.translate(Math.sin(angle) * 88, Math.cos(angle) * 88, 0);
            // TODO fix the rotation stuff here. - Loqor
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(rot));
            OrderedText text = Address.toGlyphs(String.valueOf(Dialer.GLYPHS[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, 0xF000F0);
            matrices.pop();
        }
        matrices.pop();
    }
}