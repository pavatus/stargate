package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.Address;
import dev.pavatus.stargate.api.Dialer;
import dev.pavatus.stargate.api.Stargate;
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
        Stargate possibleGate = entity.getStargate();
        Stargate.GateState state = possibleGate != null ? entity.getGateState() : Stargate.GateState.CLOSED;

        matrices.translate(0.5f, 2.65f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1.75f, 1.75f, 1.75f);


        if (possibleGate != null) {
            Dialer dialer = possibleGate.getDialer();
            this.setFromDialer(dialer, state);

            this.renderGlyphs(matrices, vertexConsumers, possibleGate);
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
        matrices.translate(xOffset, 0, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);
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

            int colour = 0x4f3909;

            if (isInDial) {
                colour = 0xc2a159;
            }
            if (isSelected) {
                colour = 0xedb334;
            }

            matrices.push();
            double angle = 2 * Math.PI * j / Dialer.GLYPHS.length;
            matrices.translate(Math.sin(angle) * 90, Math.cos(angle) * 89, 0);
            OrderedText text = Address.toGlyphs(String.valueOf(Dialer.GLYPHS[i])).asOrderedText();
            renderer.drawWithOutline(text, -renderer.getWidth(text) / 2f, 0, colour, 0x000000,
                    matrices.peek().getPositionMatrix(), vertexConsumers, 0xF000F0);
            matrices.pop();
        }
        matrices.pop();
    }
}