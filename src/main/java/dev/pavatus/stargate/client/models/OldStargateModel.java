package dev.pavatus.stargate.client.models;

import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.client.animations.StargateAnimations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class OldStargateModel extends BaseStargateModel {
	public final ModelPart gate;
	public final ModelPart portal;
	public OldStargateModel(ModelPart root) {
		this.gate = root.getChild("gate");
		this.portal = this.gate.getChild("portal");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData gate = modelPartData.addChild("gate", ModelPartBuilder.create().uv(0, 81).cuboid(-8.0F, 32.2187F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.01F))
				.uv(147, 115).cuboid(-40.2187F, -8.0F, -4.0F, 8.0F, 16.0F, 8.0F, new Dilation(0.01F))
				.uv(147, 140).cuboid(32.2187F, -8.0F, -4.0F, 8.0F, 16.0F, 8.0F, new Dilation(0.01F))
				.uv(98, 132).cuboid(-8.0F, -40.2187F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, -16.2187F, 0.0F));

		ModelPartData cube_r1 = gate.addChild("cube_r1", ModelPartBuilder.create().uv(147, 98).cuboid(-8.0F, 0.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-37.1572F, -15.391F, 0.0F, 0.0F, 0.0F, -1.1781F));

		ModelPartData cube_r2 = gate.addChild("cube_r2", ModelPartBuilder.create().uv(147, 81).cuboid(-8.0F, 0.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.01F)), ModelTransform.of(-28.4389F, -28.4389F, 0.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData cube_r3 = gate.addChild("cube_r3", ModelPartBuilder.create().uv(49, 132).cuboid(-8.0F, 0.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-15.391F, -37.1572F, 0.0F, 0.0F, 0.0F, -0.3927F));

		ModelPartData cube_r4 = gate.addChild("cube_r4", ModelPartBuilder.create().uv(0, 132).cuboid(-8.0F, 0.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(15.391F, -37.1572F, 0.0F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r5 = gate.addChild("cube_r5", ModelPartBuilder.create().uv(98, 115).cuboid(-8.0F, 0.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.01F)), ModelTransform.of(28.4389F, -28.4389F, 0.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData cube_r6 = gate.addChild("cube_r6", ModelPartBuilder.create().uv(49, 115).cuboid(-8.0F, 0.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(37.1572F, -15.391F, 0.0F, 0.0F, 0.0F, 1.1781F));

		ModelPartData cube_r7 = gate.addChild("cube_r7", ModelPartBuilder.create().uv(0, 115).cuboid(-8.0F, -8.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(37.1572F, 15.391F, 0.0F, 0.0F, 0.0F, -1.1781F));

		ModelPartData cube_r8 = gate.addChild("cube_r8", ModelPartBuilder.create().uv(98, 98).cuboid(-8.0F, -8.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-37.1572F, 15.391F, 0.0F, 0.0F, 0.0F, 1.1781F));

		ModelPartData cube_r9 = gate.addChild("cube_r9", ModelPartBuilder.create().uv(98, 81).cuboid(-8.0F, -8.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.01F)), ModelTransform.of(28.4389F, 28.4389F, 0.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData cube_r10 = gate.addChild("cube_r10", ModelPartBuilder.create().uv(49, 98).cuboid(-8.0F, -8.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.01F)), ModelTransform.of(-28.4389F, 28.4389F, 0.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData cube_r11 = gate.addChild("cube_r11", ModelPartBuilder.create().uv(0, 98).cuboid(-8.0F, -8.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(15.391F, 37.1572F, 0.0F, 0.0F, 0.0F, -0.3927F));

		ModelPartData cube_r12 = gate.addChild("cube_r12", ModelPartBuilder.create().uv(49, 81).cuboid(-8.0F, -8.0F, -4.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-15.391F, 37.1572F, 0.0F, 0.0F, 0.0F, 0.3927F));

		ModelPartData portal = gate.addChild("portal", ModelPartBuilder.create().uv(0, 0).cuboid(-40.0F, -40.0F, 0.0F, 80.0F, 80.0F, 0.0F, new Dilation(0.1F))
				.uv(0, 176).cuboid(-40.0F, -40.0F, -1.0F, 80.0F, 80.0F, 0.0F, new Dilation(0.1F))
				.uv(0, 176).cuboid(-40.0F, -40.0F, 1.0F, 80.0F, 80.0F, 0.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, -0.0187F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		gate.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return gate;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public Animation getAnimationForState(Stargate.GateState state) {
		return switch(state) {
			case DIALING -> StargateAnimations.LOCK_SYMBOL;
			default -> Animation.Builder.create(0).build();
		};
	}
}