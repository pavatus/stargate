package dev.amble.stargate.client.models;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.client.animations.StargateAnimations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class StargateModel extends BaseStargateModel {
	public final ModelPart gate_tilt;
	public final ModelPart gate;
	public final ModelPart ring;
	public final ModelPart flat;
	public final ModelPart chevrons;
	public final ModelPart lights;
	public final ModelPart chevron_one;
	public final ModelPart chevron_two;
	public final ModelPart chevron_three;
	public final ModelPart chevron_four;
	public final ModelPart chevron_five;
	public final ModelPart chevron_six;
	public final ModelPart chevron_seven;
	public final ModelPart chevron_eight;
	public final ModelPart chevron_nine;
	public final ModelPart symbols;
	public final ModelPart portal;
	public StargateModel(ModelPart root) {
		this.gate_tilt = root.getChild("gate_tilt");
		this.gate = this.gate_tilt.getChild("gate");
		this.ring = this.gate.getChild("ring");
		this.flat = this.ring.getChild("flat");
		this.chevrons = this.gate.getChild("chevrons");
		this.lights = this.chevrons.getChild("lights");
		this.chevron_one = this.lights.getChild("chevron_one");
		this.chevron_two = this.lights.getChild("chevron_two");
		this.chevron_three = this.lights.getChild("chevron_three");
		this.chevron_four = this.lights.getChild("chevron_four");
		this.chevron_five = this.lights.getChild("chevron_five");
		this.chevron_six = this.lights.getChild("chevron_six");
		this.chevron_seven = this.lights.getChild("chevron_seven");
		this.chevron_eight = this.lights.getChild("chevron_eight");
		this.chevron_nine = this.lights.getChild("chevron_nine");
		this.symbols = this.gate.getChild("symbols");
		this.portal = this.gate.getChild("portal");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData gate_tilt = modelPartData.addChild("gate_tilt", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData gate = gate_tilt.addChild("gate", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -38.4647F, 0.0F));

		ModelPartData ring = gate.addChild("ring", ModelPartBuilder.create().uv(0, 68).cuboid(-14.0F, 32.4647F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = ring.addChild("cube_r1", ModelPartBuilder.create().uv(73, 98).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(24.7246F, 29.4657F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r2 = ring.addChild("cube_r2", ModelPartBuilder.create().uv(0, 113).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(37.8803F, 6.6793F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r3 = ring.addChild("cube_r3", ModelPartBuilder.create().uv(73, 113).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(33.3114F, -19.2323F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r4 = ring.addChild("cube_r4", ModelPartBuilder.create().uv(0, 128).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r5 = ring.addChild("cube_r5", ModelPartBuilder.create().uv(0, 98).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r6 = ring.addChild("cube_r6", ModelPartBuilder.create().uv(73, 83).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-33.3114F, -19.2323F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r7 = ring.addChild("cube_r7", ModelPartBuilder.create().uv(0, 83).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-37.8803F, 6.6793F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r8 = ring.addChild("cube_r8", ModelPartBuilder.create().uv(73, 68).cuboid(-14.0F, -6.0F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-24.7246F, 29.4657F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData flat = ring.addChild("flat", ModelPartBuilder.create().uv(73, 182).cuboid(-8.0F, 39.4647F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r9 = flat.addChild("cube_r9", ModelPartBuilder.create().uv(73, 173).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-33.3114F, -19.2323F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r10 = flat.addChild("cube_r10", ModelPartBuilder.create().uv(171, 180).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-24.7246F, 29.4657F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r11 = flat.addChild("cube_r11", ModelPartBuilder.create().uv(0, 188).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(24.7246F, 29.4657F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r12 = flat.addChild("cube_r12", ModelPartBuilder.create().uv(122, 189).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(37.8803F, 6.6793F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r13 = flat.addChild("cube_r13", ModelPartBuilder.create().uv(171, 189).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(33.3114F, -19.2323F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r14 = flat.addChild("cube_r14", ModelPartBuilder.create().uv(49, 191).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r15 = flat.addChild("cube_r15", ModelPartBuilder.create().uv(0, 197).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r16 = flat.addChild("cube_r16", ModelPartBuilder.create().uv(122, 180).cuboid(-8.0F, 1.0F, -4.0F, 16.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-37.8803F, 6.6793F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData chevrons = gate.addChild("chevrons", ModelPartBuilder.create().uv(137, 30).cuboid(-14.0F, 34.7147F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r17 = chevrons.addChild("cube_r17", ModelPartBuilder.create().uv(73, 128).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(-33.3114F, -19.2323F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r18 = chevrons.addChild("cube_r18", ModelPartBuilder.create().uv(137, 0).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(-37.8803F, 6.6793F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r19 = chevrons.addChild("cube_r19", ModelPartBuilder.create().uv(137, 15).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(-24.7246F, 29.4657F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r20 = chevrons.addChild("cube_r20", ModelPartBuilder.create().uv(137, 45).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(24.7246F, 29.4657F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r21 = chevrons.addChild("cube_r21", ModelPartBuilder.create().uv(0, 143).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(37.8803F, 6.6793F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r22 = chevrons.addChild("cube_r22", ModelPartBuilder.create().uv(73, 143).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(33.3114F, -19.2323F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r23 = chevrons.addChild("cube_r23", ModelPartBuilder.create().uv(146, 60).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r24 = chevrons.addChild("cube_r24", ModelPartBuilder.create().uv(146, 75).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(-13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData lights = chevrons.addChild("lights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData chevron_one = lights.addChild("chevron_one", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r25 = chevron_one.addChild("cube_r25", ModelPartBuilder.create().uv(0, 173).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(-13.1557F, -36.145F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData chevron_two = lights.addChild("chevron_two", ModelPartBuilder.create(), ModelTransform.pivot(13.1557F, -36.145F, 0.0F));

		ModelPartData cube_r26 = chevron_two.addChild("cube_r26", ModelPartBuilder.create().uv(146, 165).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData chevron_three = lights.addChild("chevron_three", ModelPartBuilder.create(), ModelTransform.pivot(33.3114F, -19.2323F, 0.0F));

		ModelPartData cube_r27 = chevron_three.addChild("cube_r27", ModelPartBuilder.create().uv(73, 158).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData chevron_four = lights.addChild("chevron_four", ModelPartBuilder.create(), ModelTransform.pivot(37.8803F, 6.6793F, 0.0F));

		ModelPartData cube_r28 = chevron_four.addChild("cube_r28", ModelPartBuilder.create().uv(0, 158).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData chevron_five = lights.addChild("chevron_five", ModelPartBuilder.create(), ModelTransform.pivot(24.7246F, 29.4657F, 0.0F));

		ModelPartData cube_r29 = chevron_five.addChild("cube_r29", ModelPartBuilder.create().uv(146, 150).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData chevron_six = lights.addChild("chevron_six", ModelPartBuilder.create().uv(146, 135).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.pivot(0.0F, 38.4647F, 0.0F));

		ModelPartData chevron_seven = lights.addChild("chevron_seven", ModelPartBuilder.create(), ModelTransform.pivot(-24.7246F, 29.4657F, 0.0F));

		ModelPartData cube_r30 = chevron_seven.addChild("cube_r30", ModelPartBuilder.create().uv(146, 120).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData chevron_eight = lights.addChild("chevron_eight", ModelPartBuilder.create(), ModelTransform.pivot(-37.8803F, 6.6793F, 0.0F));

		ModelPartData cube_r31 = chevron_eight.addChild("cube_r31", ModelPartBuilder.create().uv(146, 105).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData chevron_nine = lights.addChild("chevron_nine", ModelPartBuilder.create(), ModelTransform.pivot(-33.3114F, -19.2323F, 0.0F));

		ModelPartData cube_r32 = chevron_nine.addChild("cube_r32", ModelPartBuilder.create().uv(146, 90).cuboid(-14.0F, -3.75F, -4.0F, 28.0F, 6.0F, 8.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData symbols = gate.addChild("symbols", ModelPartBuilder.create().uv(0, 206).cuboid(-13.0F, 32.4647F, 0.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -4.1F));

		ModelPartData cube_r33 = symbols.addChild("cube_r33", ModelPartBuilder.create().uv(106, 206).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(24.7246F, 29.4657F, 2.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r34 = symbols.addChild("cube_r34", ModelPartBuilder.create().uv(204, 198).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(37.8803F, 6.6793F, 2.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r35 = symbols.addChild("cube_r35", ModelPartBuilder.create().uv(155, 202).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(33.3114F, -19.2323F, 2.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r36 = symbols.addChild("cube_r36", ModelPartBuilder.create().uv(102, 202).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(13.1557F, -36.145F, 2.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r37 = symbols.addChild("cube_r37", ModelPartBuilder.create().uv(49, 202).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-13.1557F, -36.145F, 2.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r38 = symbols.addChild("cube_r38", ModelPartBuilder.create().uv(151, 198).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-33.3114F, -19.2323F, 2.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r39 = symbols.addChild("cube_r39", ModelPartBuilder.create().uv(98, 198).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-37.8803F, 6.6793F, 2.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r40 = symbols.addChild("cube_r40", ModelPartBuilder.create().uv(53, 206).cuboid(-13.0F, -6.0F, -2.0F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-24.7246F, 29.4657F, 2.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData portal = gate.addChild("portal", ModelPartBuilder.create().uv(0, 0).cuboid(-34.0F, -33.5F, 0.0F, 68.0F, 67.0F, 0.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, -1.0353F, -0.1F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		gate_tilt.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return gate_tilt;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public Animation getAnimationForState(Stargate.GateState state) {
		return switch(state) {
			case DIALING, PREOPEN, OPEN -> StargateAnimations.LOCK_SYMBOL;
			default -> Animation.Builder.create(0).build();
		};
	}
}