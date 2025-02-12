package dev.amble.stargate.mixins;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import dev.amble.stargate.core.block.entities.StargateLinkableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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