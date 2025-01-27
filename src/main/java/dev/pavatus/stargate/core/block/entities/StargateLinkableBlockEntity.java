package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.api.StargateRef;
import dev.pavatus.stargate.api.StargateLinkable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class StargateLinkableBlockEntity extends BlockEntity implements StargateLinkable {
	protected StargateRef ref;

	public StargateLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains("Address")) {
			this.ref = StargateRef.createAs(this, nbt.getString("Address"));
			this.markDirty();
			this.sync();
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		if (this.ref != null) {
			nbt.putString("Address", this.ref.getAddress());
		}
	}

	@Override
	public StargateRef getStargate() {
		if (this.ref == null) return null;

		return this.ref;
	}

	@Override
	public void setStargate(StargateRef gate) {
		this.ref = gate;
		this.markDirty();
		this.sync();
	}

	@Override
	public void setGateState(Stargate.GateState state) {
		if (!this.hasStargate()) return;
		if (state.equals(this.getGateState())) return;
		this.getStargate().get().setState(state);

		this.markDirty();
		if (this.getWorld() instanceof ServerWorld serverWorld) {
			serverWorld.getChunkManager().markForUpdate(this.pos);
		}
	}

	protected void sync() {
		if (this.world != null && this.world.getChunkManager() instanceof ServerChunkManager chunkManager)
			chunkManager.markForUpdate(this.pos);
	}
}
