package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.api.ServerStargateNetwork;
import dev.pavatus.stargate.api.Stargate;
import dev.pavatus.stargate.api.StargateNetwork;
import dev.pavatus.stargate.core.StargateBlockEntities;
import dev.pavatus.stargate.core.block.DHDBlock;
import dev.pavatus.stargate.core.dhd.SymbolArrangement;
import dev.pavatus.stargate.core.entities.DHDControlEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DHDBlockEntity extends NearestLinkingBlockEntity implements BlockEntityTicker<DHDBlockEntity> {
    public final List<DHDControlEntity> symbolControlEntities = new ArrayList<>();
    private boolean needsSymbols = true;
    public DHDBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.DHD, pos, state, true);
    }

    @Override
    public void onLinked() {
        super.onLinked();
        this.markNeedsControl();
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!this.hasStargate()) return ActionResult.FAIL;
        if (world.isClient()) return ActionResult.SUCCESS;

        StargateNetwork network = ServerStargateNetwork.getInstance();
        Stargate target = this.getStargate().get();

        player.sendMessage(target.getAddress().toGlyphs(), true);

        /*
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
        */

        return ActionResult.SUCCESS;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        this.markNeedsControl();
        return super.toInitialChunkDataNbt();
    }

    @Override
    public void setGateState(Stargate.GateState state) {
        super.setGateState(state);
    }

    @Override
    public void markRemoved() {
        this.killControls();
        super.markRemoved();
    }

    public void onBroken() {
        this.killControls();
    }

    public void killControls() {
        symbolControlEntities.forEach(Entity::discard);
        symbolControlEntities.clear();
    }

    public void spawnControls() {
        BlockPos current = this.getPos();

        if (!(this.world instanceof ServerWorld serverWorld))
            return;

        this.killControls();
        SymbolArrangement[] controls = DHDControlEntity.getSymbolArrangement();

        for (SymbolArrangement control : controls) {
            DHDControlEntity controlEntity = DHDControlEntity.create(this.world, this.getStargate().get());

            Vector3f position = current.toCenterPos().toVector3f();
            Direction direction = this.world.getBlockState(this.getPos()).get(DHDBlock.FACING);
            position = new Vector3f(
                    position.x + control.getOffset().x() * direction.getOffsetZ() + (-control.getOffset().z() * direction.getOffsetX()),
                    position.y + control.getOffset().y(),
                    position.z + control.getOffset().x() * direction.getOffsetX() - (control.getOffset().z() * direction.getOffsetZ())
            );
            controlEntity.setPosition(position.x(), position.y(), position.z());
            controlEntity.setYaw(0.0f);
            controlEntity.setPitch(0.0f);

            controlEntity.setControlData(control, this.getPos());

            serverWorld.spawnEntity(controlEntity);
            this.symbolControlEntities.add(controlEntity);
        }

        this.needsSymbols = false;
    }

    public void markNeedsControl() {
        this.needsSymbols = true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, DHDBlockEntity blockEntity) {
        if (this.needsSymbols)
            this.spawnControls();
    }
}
