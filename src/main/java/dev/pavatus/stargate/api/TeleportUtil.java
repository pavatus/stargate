package dev.pavatus.stargate.api;

import dev.pavatus.lib.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class TeleportUtil {
	public static void teleport(LivingEntity entity, GlobalPos pos) {
		teleport(entity, ServerLifecycleHooks.get().getWorld(pos.getDimension()), pos.getPos().toCenterPos(), Direction.NORTH);
	}
	public static void teleport(LivingEntity entity, ServerWorld world, Vec3d pos, Direction direction) {
		world.getServer().execute(() -> {
			if (entity instanceof ServerPlayerEntity player) {
				teleportPlayer(player, world, pos, direction.asRotation(), player.getPitch());
				return;
			}

			teleportNonPlayer(entity, world, pos, direction.asRotation(), entity.getPitch());
		});
	}
	private static void teleportPlayer(ServerPlayerEntity player, ServerWorld world, Vec3d pos, float yaw, float pitch) {
		player.teleport(world, pos.x, pos.y, pos.z, yaw, pitch);
		player.addExperience(0);
		player.getStatusEffects().forEach(effect -> player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), effect)));
		player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
	}
	private static void teleportNonPlayer(LivingEntity entity, ServerWorld world, Vec3d pos, float yaw, float pitch) {
		if (entity.getWorld().getRegistryKey() == world.getRegistryKey()) {
			entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, pitch);
			return;
		}

		entity.teleport(world, pos.x, pos.y, pos.z, Set.of(), yaw, pitch);
	}
}
