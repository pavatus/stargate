package dev.amble.stargate.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class DHDModel extends SinglePartEntityModel {
	public final ModelPart dhd;
	public final ModelPart main;
	public final ModelPart area;
	public final ModelPart bottom_disc;
	public final ModelPart top_disc;
	public final ModelPart button;
	public final ModelPart base;
	public DHDModel(ModelPart root) {
		this.dhd = root.getChild("dhd");
		this.main = this.dhd.getChild("main");
		this.area = this.main.getChild("area");
		this.bottom_disc = this.main.getChild("bottom_disc");
		this.top_disc = this.main.getChild("top_disc");
		this.button = this.main.getChild("button");
		this.base = this.dhd.getChild("base");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData dhd = modelPartData.addChild("dhd", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData main = dhd.addChild("main", ModelPartBuilder.create(), ModelTransform.of(0.0F, -13.0F, -7.0F, 0.4363F, 0.0F, 0.0F));

		ModelPartData area = main.addChild("area", ModelPartBuilder.create().uv(0, 0).cuboid(-11.0F, -12.3345F, -10.1495F, 26.0F, 0.0F, 21.0F, new Dilation(0.002F))
		.uv(27, 63).cuboid(-5.0F, -12.3345F, 2.8505F, 14.0F, 3.0F, 5.0F, new Dilation(0.0F))
		.uv(41, 22).cuboid(-5.0F, -12.1883F, -5.5833F, 14.0F, 2.0F, 10.0F, new Dilation(-0.001F))
		.uv(27, 72).cuboid(-1.5F, -16.1345F, -4.0995F, 7.0F, 0.0F, 7.0F, new Dilation(0.001F)), ModelTransform.pivot(-2.0F, 11.6345F, 8.1495F));

		ModelPartData cube_r1 = area.addChild("cube_r1", ModelPartBuilder.create().uv(66, 63).cuboid(-7.0F, -2.0F, -4.0F, 14.0F, 3.0F, 5.0F, new Dilation(0.001F)), ModelTransform.of(2.0F, -9.7005F, 4.4845F, 0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r2 = area.addChild("cube_r2", ModelPartBuilder.create().uv(35, 116).cuboid(-10.0F, -2.0F, 12.0F, 8.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(5.8063F, -11.6355F, -5.4244F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r3 = area.addChild("cube_r3", ModelPartBuilder.create().uv(100, 116).cuboid(-9.0F, -2.0F, 0.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.8532F, -11.6355F, -3.2627F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r4 = area.addChild("cube_r4", ModelPartBuilder.create().uv(118, 15).cuboid(-9.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(14.4315F, -11.6355F, 6.6341F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r5 = area.addChild("cube_r5", ModelPartBuilder.create().uv(111, 116).cuboid(-9.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.8532F, -11.6355F, 2.5309F, 0.0F, -1.0908F, 0.0F));

		ModelPartData cube_r6 = area.addChild("cube_r6", ModelPartBuilder.create().uv(54, 116).cuboid(-9.0F, -2.0F, -1.0F, 5.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(15.7044F, -11.6345F, -2.3784F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r7 = area.addChild("cube_r7", ModelPartBuilder.create().uv(106, 77).cuboid(2.0F, -2.0F, 12.0F, 8.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-1.8051F, -11.6365F, -5.4238F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r8 = area.addChild("cube_r8", ModelPartBuilder.create().uv(79, 60).cuboid(-9.0F, -0.69F, -1.0F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.852F, -11.6365F, -3.262F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r9 = area.addChild("cube_r9", ModelPartBuilder.create().uv(68, 60).cuboid(5.0F, -0.69F, -1.0F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(78, 116).cuboid(5.0F, -2.0F, 0.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.852F, -11.6365F, -3.262F, 0.0F, -1.0908F, 0.0F));

		ModelPartData cube_r10 = area.addChild("cube_r10", ModelPartBuilder.create().uv(118, 11).cuboid(5.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-10.4302F, -11.6365F, 6.6348F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r11 = area.addChild("cube_r11", ModelPartBuilder.create().uv(89, 116).cuboid(5.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.852F, -11.6365F, 2.5316F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r12 = area.addChild("cube_r12", ModelPartBuilder.create().uv(27, 59).cuboid(4.0F, -2.0F, -1.0F, 5.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-11.7031F, -11.6355F, -2.3777F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r13 = area.addChild("cube_r13", ModelPartBuilder.create().uv(67, 116).cuboid(-7.867F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(5.6356F, -12.6345F, -9.7887F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r14 = area.addChild("cube_r14", ModelPartBuilder.create().uv(27, 55).cuboid(-4.867F, -1.0F, -0.8643F, 5.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.0984F, -12.6345F, -9.7887F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r15 = area.addChild("cube_r15", ModelPartBuilder.create().uv(93, 59).cuboid(-3.867F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0984F, -12.6345F, -9.7887F, 0.0F, 0.3054F, 0.0F));

		ModelPartData cube_r16 = area.addChild("cube_r16", ModelPartBuilder.create().uv(93, 55).cuboid(-0.133F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(6.0984F, -12.6345F, -9.7887F, 0.0F, -0.3054F, 0.0F));

		ModelPartData bottom_disc = main.addChild("bottom_disc", ModelPartBuilder.create().uv(25, 80).cuboid(-2.0241F, -1.0F, -8.4947F, 4.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0241F, -2.0F, 7.7947F, 0.0F, -0.2182F, 0.0F));

		ModelPartData cube_r17 = bottom_disc.addChild("cube_r17", ModelPartBuilder.create().uv(0, 77).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(5.055F, 1.0F, 5.4067F, 0.0F, -2.4173F, 0.0F));

		ModelPartData cube_r18 = bottom_disc.addChild("cube_r18", ModelPartBuilder.create().uv(56, 72).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(7.127F, 1.0F, 2.4087F, 0.0F, -1.9338F, 0.0F));

		ModelPartData cube_r19 = bottom_disc.addChild("cube_r19", ModelPartBuilder.create().uv(95, 0).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(7.568F, 1.0F, -1.2088F, 0.0F, -1.4504F, 0.0F));

		ModelPartData cube_r20 = bottom_disc.addChild("cube_r20", ModelPartBuilder.create().uv(75, 94).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(6.2768F, 1.0F, -4.6167F, 0.0F, -0.9669F, 0.0F));

		ModelPartData cube_r21 = bottom_disc.addChild("cube_r21", ModelPartBuilder.create().uv(50, 94).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(3.5495F, 1.0F, -7.0339F, 0.0F, -0.4835F, 0.0F));

		ModelPartData cube_r22 = bottom_disc.addChild("cube_r22", ModelPartBuilder.create().uv(93, 44).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(1.8275F, 1.0F, 7.0512F, 0.0F, -2.9234F, 0.0F));

		ModelPartData cube_r23 = bottom_disc.addChild("cube_r23", ModelPartBuilder.create().uv(25, 91).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-1.8398F, 1.0F, 7.0096F, 0.0F, 2.9007F, 0.0F));

		ModelPartData cube_r24 = bottom_disc.addChild("cube_r24", ModelPartBuilder.create().uv(90, 22).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-5.068F, 1.0F, 5.3183F, 0.0F, 2.4173F, 0.0F));

		ModelPartData cube_r25 = bottom_disc.addChild("cube_r25", ModelPartBuilder.create().uv(0, 88).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(-7.14F, 1.0F, 2.3204F, 0.0F, 1.9338F, 0.0F));

		ModelPartData cube_r26 = bottom_disc.addChild("cube_r26", ModelPartBuilder.create().uv(75, 83).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-7.581F, 1.0F, -1.2972F, 0.0F, 1.4504F, 0.0F));

		ModelPartData cube_r27 = bottom_disc.addChild("cube_r27", ModelPartBuilder.create().uv(50, 83).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-6.2898F, 1.0F, -4.7051F, 0.0F, 0.9669F, 0.0F));

		ModelPartData cube_r28 = bottom_disc.addChild("cube_r28", ModelPartBuilder.create().uv(81, 72).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-3.5625F, 1.0F, -7.1223F, 0.0F, 0.4835F, 0.0F));

		ModelPartData top_disc = main.addChild("top_disc", ModelPartBuilder.create().uv(95, 11).cuboid(-1.515F, -1.0F, -6.1124F, 3.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.015F, -3.4F, 7.4124F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r29 = top_disc.addChild("cube_r29", ModelPartBuilder.create().uv(92, 105).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(4.086F, 0.0F, 3.8887F, 0.0F, -2.4173F, 0.0F));

		ModelPartData cube_r30 = top_disc.addChild("cube_r30", ModelPartBuilder.create().uv(69, 105).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(5.409F, 0.0F, 1.5534F, 0.0F, -1.9338F, 0.0F));

		ModelPartData cube_r31 = top_disc.addChild("cube_r31", ModelPartBuilder.create().uv(105, 55).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(5.4949F, 0.0F, -1.1293F, 0.0F, -1.4504F, 0.0F));

		ModelPartData cube_r32 = top_disc.addChild("cube_r32", ModelPartBuilder.create().uv(46, 105).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(4.3239F, 0.0F, -3.5445F, 0.0F, -0.9669F, 0.0F));

		ModelPartData cube_r33 = top_disc.addChild("cube_r33", ModelPartBuilder.create().uv(23, 102).cuboid(5.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-3.148F, 0.0F, -7.9276F, 0.0F, -0.4835F, 0.0F));

		ModelPartData cube_r34 = top_disc.addChild("cube_r34", ModelPartBuilder.create().uv(100, 94).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(1.8348F, 0.0F, 5.3167F, 0.0F, -2.9234F, 0.0F));

		ModelPartData cube_r35 = top_disc.addChild("cube_r35", ModelPartBuilder.create().uv(100, 83).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(-0.8614F, 0.0F, 5.5137F, 0.0F, 2.9007F, 0.0F));

		ModelPartData cube_r36 = top_disc.addChild("cube_r36", ModelPartBuilder.create().uv(115, 105).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-3.3406F, 0.0F, 4.4851F, 0.0F, 2.4173F, 0.0F));

		ModelPartData cube_r37 = top_disc.addChild("cube_r37", ModelPartBuilder.create().uv(115, 22).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-5.0575F, 0.0F, 2.4219F, 0.0F, 1.9338F, 0.0F));

		ModelPartData cube_r38 = top_disc.addChild("cube_r38", ModelPartBuilder.create().uv(107, 33).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-5.6186F, 0.0F, -0.2029F, 0.0F, 1.4504F, 0.0F));

		ModelPartData cube_r39 = top_disc.addChild("cube_r39", ModelPartBuilder.create().uv(106, 66).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(-4.8953F, 0.0F, -2.7877F, 0.0F, 0.9669F, 0.0F));

		ModelPartData cube_r40 = top_disc.addChild("cube_r40", ModelPartBuilder.create().uv(0, 99).cuboid(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-3.0534F, 0.0F, -4.74F, 0.0F, 0.4835F, 0.0F));

		ModelPartData button = main.addChild("button", ModelPartBuilder.create().uv(27, 49).cuboid(-1.5F, -1.0F, -1.25F, 3.0F, 2.0F, 3.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -3.9F, 7.3F));

		ModelPartData base = dhd.addChild("base", ModelPartBuilder.create().uv(0, 22).cuboid(-5.0F, -16.0F, -5.0F, 10.0F, 16.0F, 10.0F, new Dilation(-0.001F))
		.uv(68, 44).cuboid(-5.8787F, -13.0F, -6.1213F, 10.0F, 13.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 113).cuboid(3.8787F, -13.0F, -6.1213F, 2.0F, 13.0F, 2.0F, new Dilation(-0.001F))
		.uv(41, 35).cuboid(-7.0F, -17.0F, -5.0F, 3.0F, 17.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 49).cuboid(4.0F, -17.0F, -5.0F, 3.0F, 17.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r41 = base.addChild("cube_r41", ModelPartBuilder.create().uv(68, 35).cuboid(-5.0F, 0.9033F, 1.2342F, 14.0F, 3.0F, 5.0F, new Dilation(0.002F)), ModelTransform.of(-2.0F, -5.6155F, 4.9995F, -0.9599F, 0.0F, 0.0F));

		ModelPartData cube_r42 = base.addChild("cube_r42", ModelPartBuilder.create().uv(13, 113).cuboid(5.0F, -17.0F, 2.0F, 3.0F, 17.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.1924F, 0.0F, -2.8787F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r43 = base.addChild("cube_r43", ModelPartBuilder.create().uv(0, 110).cuboid(-8.0F, -17.0F, 2.0F, 3.0F, 17.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.1924F, 0.0F, -2.8787F, 0.0F, -0.7854F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		dhd.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return dhd;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}