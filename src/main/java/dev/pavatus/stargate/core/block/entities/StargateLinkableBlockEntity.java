package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.api.StargateRef;
import dev.pavatus.stargate.api.StargateWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class StargateLinkableBlockEntity extends BlockEntity implements StargateWrapper {
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
	public Stargate getStargate() {
		if (this.ref == null) return null;

		return this.ref.get();
	}

	public void link(Stargate gate) {
		this.ref = StargateRef.createAs(this, gate);
	}

	@Override
	public void setStargate(Stargate gate) {
		this.link(gate);
		this.markDirty();
		this.sync();
	}

	@Override
	public void setGateState(Stargate.GateState state) {
		if (state.equals(this.getGateState())) return;
		this.getStargate().setState(state);

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
