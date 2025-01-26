package dev.pavatus.stargate.api;

import dev.pavatus.stargate.core.StargateSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;

/**
 * For tracking a dialing sequence in progress
 */
public class Dialer {
	private static final char[] GLYPHS = "ABCDEFG".toCharArray();
	private String target;
	private char selected;
	private List<Consumer<Dialer>> subscribers;

	public Dialer() {
		this.target = "";
		this.subscribers = new ArrayList<>();
	}
	public Dialer(NbtCompound nbt) {
		this();
		this.target = nbt.getString("Target");
	}

	/**
	 * @return whether the dialer is filled
	 */
	public boolean isComplete() {
		return this.target.length() == 7;
	}

	public Optional<Stargate> toStargate(World world) {
		if (!this.isComplete()) return Optional.empty();

		Address found = StargateNetwork.getInstance(world).getAddress(this.target).orElse(null);
		if (found == null) return Optional.empty();

		return StargateNetwork.getInstance(world).getOptional(found);
	}
	public Optional<StargateCall> complete(Stargate source, World world) {
		Stargate target = this.toStargate(world).orElse(null);

		this.clear();

		if (target == null) {
			source.playSound(StargateSounds.GATE_FAIL, 0.25f, 1f);
			return Optional.empty();
		}

		return source.dial(target);
	}

	protected void append(char c) {
		if (this.target.length() < 7) {
			this.target += c;

			if (this.isComplete()) {
				this.subscribers.forEach(consumer -> consumer.accept(this));
			}
		}
	}

	public String clear() {
		String old = this.target;
		this.target = "";
		this.selected = GLYPHS[0];
		return old;
	}

	/**
	 * Locks a chevron in place
	 * Adds it to the target address
	 */
	public void lock() {
		this.append(this.selected);
	}
	public char getSelected() {
		return this.selected;
	}

	/**
	 * @return the index of the selected glyph in GLYPHS
	 */
	public int getSelectedIndex() {
		for (int i = 0; i < GLYPHS.length; i++) {
			if (GLYPHS[i] == this.selected) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * @return The amount of glyphs (7)
	 */
	public int getGlyphCount() {
		return GLYPHS.length;
	}

	public char next() {
		int index = this.getSelectedIndex();
		this.selected = GLYPHS[(index + 1) % GLYPHS.length];
		return this.selected;
	}
	public char previous() {
		int index = this.getSelectedIndex();
		this.selected = GLYPHS[(index + GLYPHS.length - 1) % GLYPHS.length];
		return this.selected;
	}

	public int getAmountLocked() {
		return this.target.length();
	}

	public Dialer onCompleted(Consumer<Dialer> consumer) {
		this.subscribers.add(consumer);
		return this;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("Target", this.target);
		return nbt;
	}
}
