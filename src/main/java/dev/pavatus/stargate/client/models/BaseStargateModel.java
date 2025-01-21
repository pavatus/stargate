package dev.pavatus.stargate.client.models;

import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public abstract class BaseStargateModel extends SinglePartEntityModel {

    public BaseStargateModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    public BaseStargateModel(Function<Identifier, RenderLayer> function) {
        super(function);
    }

    public void animateStargateModel(StargateBlockEntity stargateBlockEntity, StargateBlockEntity.GateState state, int age) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);


        this.updateAnimation(stargateBlockEntity.ANIM_STATE, this.getAnimationForState(state), age);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
                          float headPitch) {
    }

    public abstract Animation getAnimationForState(StargateBlockEntity.GateState state);
}
