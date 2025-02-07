package dev.pavatus.stargate.mixins;

import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.core.block.entities.StargateBlockEntity;
import dev.pavatus.stargate.core.block.entities.StargateLinkableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    public void place(BlockEntity blockEntity, NbtCompound nbt) {
        if (blockEntity instanceof StargateLinkableBlockEntity linkable) {
            StargateMod.LOGGER.debug("Removed link on structure block {}", linkable);

            nbt.remove("Address");
            if (blockEntity instanceof StargateBlockEntity) {
                ((StargateBlockEntity) blockEntity).requiresPlacement = true;
            }
        }

        blockEntity.readNbt(nbt);
    }
}