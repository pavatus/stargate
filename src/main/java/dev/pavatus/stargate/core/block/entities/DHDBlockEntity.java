package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.api.ServerStargateNetwork;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.api.StargateNetwork;
import dev.pavatus.stargate.core.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DHDBlockEntity extends NearestLinkingBlockEntity implements BlockEntityTicker<DHDBlockEntity> {
    public DHDBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.DHD, pos, state, true);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, DHDBlockEntity blockEntity) {

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!this.hasStargate()) return ActionResult.FAIL;
        if (world.isClient()) return ActionResult.SUCCESS;

        StargateNetwork network = ServerStargateNetwork.getInstance();
        Stargate target = this.getStargate().get();

        int counter = 0;
        while (target == this.getStargate().get() && counter++ < 10) {
            target = network.getRandom();
        }

        if (target == null) {
            player.sendMessage(Text.literal("No stargates found"), true);
            return ActionResult.FAIL;
        }

        this.getStargate().get().dial(target);
        player.sendMessage(Text.literal("Dialing ").append(target.getAddress().toGlyphs()), true);
        return ActionResult.SUCCESS;
    }
}
