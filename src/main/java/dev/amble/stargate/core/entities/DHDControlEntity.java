package dev.amble.stargate.core.entities;

import java.util.List;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.StargateRef;
import dev.amble.stargate.core.dhd.DHDArrangement;
import dev.amble.stargate.core.dhd.SymbolArrangement;
import dev.amble.stargate.core.dhd.control.SymbolControl;
import dev.amble.stargate.core.StargateEntities;
import dev.amble.stargate.core.block.entities.DHDBlockEntity;
import dev.amble.stargate.core.entities.base.LinkableDummyLivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DHDControlEntity extends LinkableDummyLivingEntity {

    private static final TrackedData<String> IDENTITY = DataTracker.registerData(DHDControlEntity.class,
            TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(DHDControlEntity.class,
            TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(DHDControlEntity.class,
            TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Vector3f> OFFSET = DataTracker.registerData(DHDControlEntity.class,
            TrackedDataHandlerRegistry.VECTOR3F);

    public BlockPos dhdBlockPos;
    private SymbolControl control;

    public DHDControlEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world, false);
    }

    private DHDControlEntity(World world, Stargate stargate) {
        this(StargateEntities.DHD_CONTROL_TYPE, world);
        this.setStargate(StargateRef.createAs(world, stargate));
    }

    @Override
    public boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source) {
        return false;
    }

    public static SymbolArrangement[] getSymbolArrangement() {
        return DHDArrangement.getSymbolArrangement();
    }

    public static DHDControlEntity create(World world, Stargate stargate) {
        return new DHDControlEntity(world, stargate);
    }

    @Override
    public void remove(RemovalReason reason) {
        StargateMod.LOGGER.debug("Control entity discarded as {}", reason);
        this.setRemoved(reason);
    }

    @Override
    public void onRemoved() {
        if (this.dhdBlockPos == null) {
            super.onRemoved();
            return;
        }

        if (this.getWorld().getBlockEntity(this.dhdBlockPos) instanceof DHDBlockEntity dhd)
            dhd.markNeedsControl();
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(IDENTITY, "");
        this.dataTracker.startTracking(WIDTH, 0.125f);
        this.dataTracker.startTracking(HEIGHT, 0.125f);
        this.dataTracker.startTracking(OFFSET, new Vector3f(0));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        StargateRef ref = this.asRef();

        if (ref != null && ref.getAddress() != null)
            nbt.putString("stargate", ref.getAddress());

        if (dhdBlockPos != null)
            nbt.put("dhd", NbtHelper.fromBlockPos(this.dhdBlockPos));

        nbt.putString("identity", this.getIdentity());
        nbt.putFloat("width", this.getControlWidth());
        nbt.putFloat("height", this.getControlHeight());
        nbt.putFloat("offsetX", this.getOffset().x());
        nbt.putFloat("offsetY", this.getOffset().y());
        nbt.putFloat("offsetZ", this.getOffset().z());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        NbtCompound dhd = nbt.getCompound("dhd");

        if (dhd != null)
            this.dhdBlockPos = NbtHelper.toBlockPos(dhd);

        if (nbt.contains("identity"))
            this.setIdentity(nbt.getString("identity"));

        if (nbt.contains("width") && nbt.contains("height")) {
            this.setControlWidth(nbt.getFloat("width"));
            this.setControlWidth(nbt.getFloat("height"));
            this.calculateDimensions();
        }

        if (nbt.contains("offsetX") && nbt.contains("offsetY") && nbt.contains("offsetZ"))
            this.setOffset(new Vector3f(nbt.getFloat("offsetX"), nbt.getFloat("offsetY"), nbt.getFloat("offsetZ")));
    }

    @Override
    public void onDataTrackerUpdate(List<DataTracker.SerializedEntry<?>> dataEntries) {
        this.setScaleAndCalculate(this.getDataTracker().get(WIDTH), this.getDataTracker().get(HEIGHT));
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);

        if (player.getOffHandStack().getItem() == Items.COMMAND_BLOCK) {
            controlEditorHandler(player);
            return ActionResult.SUCCESS;
        }

        handStack.useOnEntity(player, this, hand);

        if (hand == Hand.MAIN_HAND)
            this.run(player, player.getWorld(), false);

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (source.getAttacker() instanceof TntEntity) {
            return false;
        }

        if (source.getAttacker() instanceof PlayerEntity player) {
            if (player.getOffHandStack().getItem() == Items.COMMAND_BLOCK) {
                controlEditorHandler(player);
            } else
                this.run((PlayerEntity) source.getAttacker(), source.getAttacker().getWorld(), true);
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public Text getName() {
        if (this.control != null)
            return Text.translatable(String.valueOf(this.control.getGlyph()));
        else
            return super.getName();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        if (this.getDataTracker().containsKey(WIDTH) && this.getDataTracker().containsKey(HEIGHT))
            return EntityDimensions.changing(this.getControlWidth(), this.getControlHeight());

        return super.getDimensions(pose);
    }

    @Override
    public void tick() {
        if (this.getWorld().isClient())
            return;

        if (this.control == null && this.dhdBlockPos != null)
            this.discard();
    }

    @Override
    public boolean shouldRenderName() {
        return true;
    }

    public String getIdentity() {
        return this.dataTracker.get(IDENTITY);
    }

    public void setIdentity(String string) {
        this.dataTracker.set(IDENTITY, string);
    }

    public float getControlWidth() {
        return this.dataTracker.get(WIDTH);
    }

    public float getControlHeight() {
        return this.dataTracker.get(HEIGHT);
    }

    public void setControlWidth(float width) {
        this.dataTracker.set(WIDTH, width);
    }

    public void setControlHeight(float height) {
        this.dataTracker.set(HEIGHT, height);
    }

    public SymbolControl getControl() {
        return control;
    }

    public Vector3f getOffset() {
        return this.dataTracker.get(OFFSET);
    }

    public void setOffset(Vector3f offset) {
        this.dataTracker.set(OFFSET, offset);
    }

    public boolean shouldGlow() {
        if (!this.hasStargate()) return false;
        return this.getStargate().get().getDialer().contains(this.getCustomName().getString().charAt(0));
    }

    public void run(PlayerEntity player, World world, boolean leftClick) {
        if (world.getRandom().nextBetween(1, 10_000) == 72)
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.MASTER,
                    1F, 1F);

        if (world.isClient())
            return;

        Stargate stargate = this.getStargate().get();

        if (stargate == null) {
            StargateMod.LOGGER.warn("Discarding invalid control entity at {}; dhd pos: {}", this.getPos(),
                    this.dhdBlockPos);

            this.discard();
            return;
        }

        if (this.dhdBlockPos != null)
            this.getWorld().playSound(null, this.getBlockPos(), this.control.getSound(), SoundCategory.BLOCKS, 0.7f,
                    1f);

        if (!this.control.canRun(stargate, (ServerPlayerEntity) player)) return;

        this.control.runServer(stargate, (ServerPlayerEntity) player, (ServerWorld) world, this.dhdBlockPos,
                leftClick);
    }

    public void setScaleAndCalculate(float width, float height) {
        this.setControlWidth(width);
        this.setControlHeight(height);
        this.calculateDimensions();
    }

    public void setControlData(SymbolArrangement type, BlockPos dhdBlockPosition) {
        this.dhdBlockPos = dhdBlockPosition;
        this.control = type.getControl();

        this.setIdentity(this.control.getClass().getSimpleName());
        this.setControlWidth(type.getScale().width);
        this.setControlHeight(type.getScale().height);
        this.setCustomName(Text.translatable(String.valueOf(type.getControl().glyph)));
    }

    public void controlEditorHandler(PlayerEntity player) {
        float increment = 0.0125f;
        if (player.getMainHandStack().getItem() == Items.EMERALD_BLOCK)
            this.setPosition(this.getPos().add(player.isSneaking() ? -increment : increment, 0, 0));

        if (player.getMainHandStack().getItem() == Items.DIAMOND_BLOCK)
            this.setPosition(this.getPos().add(0, player.isSneaking() ? -increment : increment, 0));

        if (player.getMainHandStack().getItem() == Items.REDSTONE_BLOCK)
            this.setPosition(this.getPos().add(0, 0, player.isSneaking() ? -increment : increment));

        if (player.getMainHandStack().getItem() == Items.COD)
            this.setScaleAndCalculate(player.isSneaking()
                    ? this.getDataTracker().get(WIDTH) - increment
                    : this.getDataTracker().get(WIDTH) + increment, this.getDataTracker().get(HEIGHT));

        if (player.getMainHandStack().getItem() == Items.COOKED_COD)
            this.setScaleAndCalculate(this.getDataTracker().get(WIDTH),
                    player.isSneaking()
                            ? this.getDataTracker().get(HEIGHT) - increment
                            : this.getDataTracker().get(HEIGHT) + increment);

        if (this.dhdBlockPos != null) {
            Vec3d centered = this.getPos().subtract(this.dhdBlockPos.toCenterPos());
            if (this.control != null)
                player.sendMessage(Text.literal(/*"EntityDimensions.changing(" + this.getControlWidth() + "f, "
                        + this.getControlHeight() + "f), */"new Vector3f(" + centered.getX() + "f, " + centered.getY()
                        + "f, " + centered.getZ() + "f))," + "    " + this.getCustomName().getString()));
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        if (name == null) return;
        if (this.getControl() == null) return;
        Text text = Text.translatable(String.valueOf(this.control.glyph));
        if (name.equals(text)) {
            super.setCustomName(name);
        }
    }
}
