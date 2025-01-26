package dev.pavatus.stargate.api;

import net.minecraft.nbt.NbtCompound;

public class ClientStargate extends Stargate {
	private boolean aged = false;

	protected ClientStargate(Address address) {
		super(address);
	}

	public ClientStargate(NbtCompound nbt) {
		super(nbt);
	}

	@Override
	public void age() {
		this.aged = true;
	}

	@Override
	public boolean isAged() {
		return aged;
	}
}
