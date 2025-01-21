package dev.pavatus.stargate.core.block.entities;

import dev.pavatus.stargate.api.*;
import dev.pavatus.stargate.core.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

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
			PhoneBook.getInstance().add(this.stargate);
		}
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (world.isClient()) return ActionResult.SUCCESS;

		// temporary test code

		// dial a random address that isnt us
		Stargate chosen = this.getStargate();
		int counter = 0;
		while (chosen == this.getStargate() && counter < 10) {
			chosen = PhoneBook.getInstance().getRandom();
			counter++;
		}

		StargateCall call = this.getStargate().dial(chosen).orElse(null);

		if (call == null) {
			player.sendMessage(Text.literal("TARGET UNAVAILABLE"), true);

			return ActionResult.FAIL;
		}

		call.start();
		player.sendMessage(Text.literal("CALL CONNECTED TO ").append(chosen.getAddress().toGlyphs()), true);

		call.onEnd(c -> {
			player.sendMessage(Text.literal("WORMHOLE CLOSED"), true);
		});

		return ActionResult.SUCCESS;
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!(e instanceof LivingEntity entity)) return;

		// teleport the player to the stargate
		StargateCall existing = this.getStargate().getCurrentCall().orElse(null);
		if (existing != null && existing.to != this.getStargate()) {
			existing.to.teleportHere(entity);

			if (entity instanceof ServerPlayerEntity player) {
				player.sendMessage(Text.literal("HAVE A GOOD TRIP"), true);
			}
		}
	}

	public void onBreak() {
		this.getStargate().dispose();
		this.stargate = null;
	}
}
