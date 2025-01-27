package dev.pavatus.stargate.core.entities.base;

import java.util.Optional;

import dev.pavatus.stargate.api.StargateRef;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public abstract class LinkableDummyLivingEntity extends DummyLivingEntity implements AbstractLinkableEntity {

    private static final TrackedData<String> STARGATE = AbstractLinkableEntity
            .register(LinkableDummyLivingEntity.class);

    private StargateRef stargate;

    public LinkableDummyLivingEntity(EntityType<? extends LivingEntity> type, World world, boolean hasBrain) {
        super(type, world, hasBrain);
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public DataTracker getDataTracker() {
        return super.getDataTracker();
    }

    @Override
    public TrackedData<String> getTracked() {
        return STARGATE;
    }

    @Override
    public StargateRef asRef() {
        return this.stargate;
    }

    @Override
    public void setRef(StargateRef ref) {
        this.stargate = ref;
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        AbstractLinkableEntity.super.initDataTracker();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        AbstractLinkableEntity.super.onTrackedDataSet(data);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        AbstractLinkableEntity.super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        AbstractLinkableEntity.super.writeCustomDataToNbt(nbt);
    }
}
