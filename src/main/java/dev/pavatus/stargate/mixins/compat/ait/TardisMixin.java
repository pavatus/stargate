package dev.pavatus.stargate.mixins.compat.ait;

import dev.pavatus.stargate.api.StargateLinkable;
import dev.pavatus.stargate.api.StargateRef;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.data.Exclude;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.transformer.meta.MixinInner;

@Mixin(Tardis.class)
public class TardisMixin implements StargateLinkable {
	@Unique
	@Exclude
	private StargateRef ref;

	@Override
	public StargateRef getStargate() {
		return ref;
	}

	@Override
	public void setStargate(StargateRef gate) {
		this.ref = gate;
	}
}
