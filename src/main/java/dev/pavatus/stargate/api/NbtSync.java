package dev.pavatus.stargate.api;

import net.minecraft.nbt.NbtCompound;

public interface NbtSync {
	void loadNbt(NbtCompound nbt, boolean isSync);
	default void loadNbt(NbtCompound nbt) {
		this.loadNbt(nbt, this.isSync(nbt));
	}
	default boolean isSync(NbtCompound nbt) {
		return nbt.contains("NetworkData");
	}
	default NbtCompound toNbt(boolean sync) {
		NbtCompound nbt = this.toNbt();

		if (sync) {
			NbtCompound syncNbt = this.toSyncNbt();
			nbt.put("NetworkData", syncNbt);
		}

		return nbt;
	}
	NbtCompound toNbt();
	default NbtCompound toSyncNbt() {
		return new NbtCompound();
	}

	default NbtCompound getSyncNbt(NbtCompound nbt) {
		return nbt.getCompound("NetworkData");
	}
}
