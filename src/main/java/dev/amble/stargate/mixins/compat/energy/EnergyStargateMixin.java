package dev.amble.stargate.mixins.compat.energy;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.compat.energy.StargateRebornEnergy;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@Mixin(value = Stargate.class, remap = false)
public class EnergyStargateMixin implements StargateRebornEnergy {
	@Unique
	public final SimpleEnergyStorage energy = new SimpleEnergyStorage(100000, 32, 0) {
		@Override
		protected void onFinalCommit() {
			EnergyStargateMixin.this.syncMixin();
		}

		@Override
		public long insert(long maxAmount, TransactionContext transaction) {
			EnergyStargateMixin.this.syncMixin();

			return super.insert(maxAmount, transaction);
		}
	};

	@Unique
	private void syncMixin() {
		((Stargate) (Object) this).sync();
	}

	@Override
	public EnergyStorage getStorage() {
		return energy;
	}

	@Inject(method = "getEnergy", at = @At("HEAD"), cancellable = true)
	private void stargate$getAmount(CallbackInfoReturnable<Long> cir) {
		cir.setReturnValue(energy.getAmount());
	}
	@Inject(method="setEnergy", at=@At("HEAD"))
	private void stargate$setAmount(long amount, CallbackInfo ci) {
		energy.amount = amount;
	}
}
