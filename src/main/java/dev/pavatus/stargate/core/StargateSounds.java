package dev.pavatus.stargate.core;

import dev.pavatus.lib.container.impl.SoundContainer;
import dev.pavatus.stargate.StargateMod;
import net.minecraft.client.sound.Sound;
import net.minecraft.sound.SoundEvent;

public class StargateSounds implements SoundContainer {
	public static final SoundEvent GATE_OPEN = create("gate_open");
	public static final SoundEvent GATE_CLOSE = create("gate_close");
	public static final SoundEvent GATE_FAIL = create("gate_fail");
	public static final SoundEvent GATE_TELEPORT = create("gate_teleport");

	public static SoundEvent create(String name) {
		return SoundEvent.of(StargateMod.id(name));
	}
}
