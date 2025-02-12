package dev.amble.stargate.core.entities.base;

import dev.amble.stargate.api.StargateLinkable;
import dev.amble.stargate.api.StargateRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public interface AbstractLinkableEntity extends StargateLinkable {

    World getWorld();

    DataTracker getDataTracker();

    TrackedData<String> getTracked();

    StargateRef asRef();

    void setRef(StargateRef ref);


    @Override
    default void setStargate(StargateRef stargate) {
        this.setRef(stargate);
        this.getDataTracker().set(this.getTracked(), stargate.getAddress());
    }

    @Override
    default StargateRef getStargate() {
        StargateRef result = this.asRef();

        if (result == null) {
            this.link(this.getDataTracker().get(this.getTracked()), this.getWorld());
            return this.getStargate();
        }

        return result;
    }

    default void initDataTracker() {
        this.getDataTracker().startTracking(this.getTracked(), "");
    }

    default void onTrackedDataSet(TrackedData<?> data) {
        if (!this.getTracked().equals(data))
            return;

        this.link(this.getDataTracker().get(this.getTracked()), this.getWorld());
    }

    default void readCustomDataFromNbt(NbtCompound nbt) {
        String id = nbt.getString("stargate");

        if (id == null)
            return;

        this.link(id, this.getWorld());

        if (this.getWorld() == null)
            return;

        this.onLinked();
    }

    default void writeCustomDataToNbt(NbtCompound nbt) {
        StargateRef ref = this.asRef();

        if (ref != null && ref.getAddress() != null)
            nbt.putString("stargate", ref.getAddress());
    }

    static <T extends Entity & AbstractLinkableEntity> TrackedData<String> register(Class<T> self) {
        return DataTracker.registerData(self, TrackedDataHandlerRegistry.STRING);
    }
}