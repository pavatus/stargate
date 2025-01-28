package dev.pavatus.stargate.client.renderers;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.api.Address;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.client.models.ControlModel;
import dev.pavatus.stargate.core.block.DHDBlock;
import dev.pavatus.stargate.core.entities.DHDControlEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

@Environment(value = EnvType.CLIENT)
public class DHDControlEntityRenderer extends LivingEntityRenderer<DHDControlEntity, ControlModel> {

    public DHDControlEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ControlModel(ControlModel.getNotModelData().createModel()), 0f);
    }

    @Override
    public void render(DHDControlEntity livingEntity, float yaw, float tickDelta, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int light) {
        if (livingEntity.getCustomName() == null) return;

        Text name = Address.toGlyphs(livingEntity.getCustomName().getString());
        double d = this.dispatcher.getSquaredDistanceToCamera(livingEntity);

        TextRenderer textRenderer = this.getTextRenderer();
        float h = (float) -textRenderer.getWidth(name) / 2;
        float f = livingEntity.getNameLabelHeight() - 0.525f;

        Stargate stargate = livingEntity.getStargate().get();

        if (stargate == null) {
            matrixStack.pop();
            return;
        }

        matrixStack.push();
        matrixStack.translate(0.0f, f, 0.0f);

        //float k = livingEntity.getWorld().getBlockState(livingEntity.dhdBlockPos).get(DHDBlock.FACING).asRotation();
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(MinecraftClient.getInstance().player.getHeadYaw()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(65));
        matrixStack.scale(-0.0075f, -0.0075f, 0.0075f);

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        OrderedText orderedText = name.asOrderedText();

        textRenderer.drawWithOutline(orderedText, h, (float) name.getString().length(), livingEntity.shouldGlow() ? 0xedb334 : 0x4f4f4f, 0x000000,
                matrix4f, vertexConsumerProvider, 0xFF);
        matrixStack.pop();
        super.render(livingEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    @Override
    protected void renderLabelIfPresent(DHDControlEntity entity, Text text, MatrixStack matrices,
                                        VertexConsumerProvider vertexConsumers, int light) {
        /*Text name = Address.toGlyphs(entity.getCustomName().getString());

        TextRenderer textRenderer = this.getTextRenderer();
        float h = (float) -textRenderer.getWidth(name) / 2;
        float f = entity.getNameLabelHeight() - 0.525f;

        Stargate stargate = entity.getStargate().get();

        if (stargate == null)
            return;

        matrices.push();
        matrices.translate(0.0f, f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(65));
        matrices.scale(-0.0075f, -0.0075f, 0.0075f);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        OrderedText orderedText = name.asOrderedText();

        textRenderer.drawWithOutline(orderedText, h, (float) name.getString().length(), entity.shouldGlow() ? 0xedb334 : 0x4f4f4f, 0x000000,
                matrix4f, vertexConsumers, 0xFF);
        matrices.pop();*/
    }

    @Override
    public Identifier getTexture(DHDControlEntity controlEntity) {
        return TextureManager.MISSING_IDENTIFIER;
    }

}
