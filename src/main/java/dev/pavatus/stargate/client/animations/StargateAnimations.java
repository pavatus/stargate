package dev.pavatus.stargate.client.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class StargateAnimations {
	public static final Animation PORTAL_ROTATE = Animation.Builder.create(0.3333F).looping()
		.addBoneAnimation("portal", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 360.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PORTAL_OFF = Animation.Builder.create(0.0F).looping()
		.addBoneAnimation("portal", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
}