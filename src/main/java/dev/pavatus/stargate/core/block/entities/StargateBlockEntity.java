package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.api.Address;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.api.StargateWrapper;
import dev.pavatus.stargate.core.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

public class StargateBlockEntity extends BlockEntity implements StargateWrapper {
	private Stargate stargate;

	public StargateBlockEntity(BlockPos pos, BlockState state) {
		super(StargateBlockEntities.STARGATE, pos, state);
	}

	@Override
	public Stargate getStargate() {
		if (this.stargate == null) {
			this.stargate = Stargate.create(new Address(GlobalPos.create(this.getWorld().getRegistryKey(), this.getPos())));
		}

		return this.stargate;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		nbt.put("Stargate", this.getStargate().toNbt());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains("Stargate")) {
			this.stargate = Stargate.fromNbt(nbt.getCompound("Stargate"));
		}
	}
}
