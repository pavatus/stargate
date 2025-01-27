package dev.pavatus.stargate.api;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.core.StargateSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;

/**
 * For tracking a dialing sequence in progress
 */
public class Dialer {
	public static final char[] GLYPHS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private final Stargate parent;
	private String target;
	private char selected;
	private List<Consumer<Dialer>> subscribers;
	private boolean firstMove;
	private boolean isAutoDialing;

	public Dialer(Stargate parent) {
		this.selected = GLYPHS[0];
		this.target = "";
		this.subscribers = new ArrayList<>();
		this.parent = parent;
	}
	public Dialer(Stargate parent, NbtCompound nbt) {
		this(parent);

		this.target = nbt.getString("Target");
		this.selected = nbt.getString("Selected").charAt(0);
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

		return source.dialImmediately(target);
	}

	protected void append(char c) {
		if (this.target.length() < 7) {
			this.target += c;

			if (this.isComplete()) {
				this.subscribers.forEach(consumer -> consumer.accept(this));
			}

			this.parent.sync();
		}
	}

	public String clear() {
		String old = this.target;
		this.target = "";
		this.firstMove = true;
		this.parent.sync();
		return old;
	}

	/**
	 * Locks a chevron in place
	 * Adds it to the target address
	 */
	public void lock() {
		this.append(this.selected);

		this.parent.playSound(StargateSounds.CHEVRON_LOCK, 0.25f, StargateMod.RANDOM.nextFloat(0.9f, 1.1f));
	}
	public char getSelected() {
		return this.selected;
	}

	/**
	 * @return the index of the selected glyph in GLYPHS
	 */
	public int getSelectedIndex() {
		return this.indexOf(this.selected);
	}
	public int indexOf(char c) {
		for (int i = 0; i < GLYPHS.length; i++) {
			if (GLYPHS[i] == c) {
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

		this.onMove(true);

		return this.selected;
	}
	public char previous() {
		int index = this.getSelectedIndex();
		this.selected = GLYPHS[(index + GLYPHS.length - 1) % GLYPHS.length];

		this.onMove(false);

		return this.selected;
	}

	protected void onMove(boolean next) {
		this.parent.sync();
		this.parent.playSound(StargateSounds.RING_LOOP, 1.25f, StargateMod.RANDOM.nextFloat(1.0f, 1.25f));

		if (this.firstMove) {
			this.parent.playSound(StargateSounds.RING_START, 1.5f, 1.0f);
			this.firstMove = false;
		}
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
		nbt.putString("Selected", String.valueOf(this.selected));
		return nbt;
	}

	/**
	 * Dials the target address over a period of time
	 * @param address the address to dial
	 * @param unit units of time
	 * @param delay the amount of delay between each glyph being appended
	 * @return whether the dialing was successful
	 */
	public boolean dial(Address address, TimeUnit unit, long delay) {
		if (this.isAutoDialing) return false;

		this.clear();

		this.internalDial(address, unit, delay, 0);
		this.isAutoDialing = true;

		return true;
	}

	/**
	 * Dials the target glyph over a period of time
	 * @param target the target glyph
	 * @param unit units of time
	 * @param delay the amount of delay between each glyph being appended
	 * @return whether the dialing was successful
	 */
	public boolean dial(char target, TimeUnit unit, long delay) {
		if (this.isAutoDialing) return false;

		this.internalDial(target, unit, delay);

		this.isAutoDialing = true;
		return true;
	}
	private void internalDial(char target, TimeUnit unit, long delay) {
		if (this.selected != target) {
			this.rotateTowards(target);
		} else {
			this.lock();
			this.isAutoDialing = false;
			return;
		}

		Scheduler.get().runTaskLater(() -> this.internalDial(target, unit, delay), unit, delay);
	}

	private void internalDial(Address address, TimeUnit unit, long delay, int i) {
		if (i == 7) {
			this.isAutoDialing = false;
			return;
		}

		if (this.selected != address.text().charAt(i)) {
			this.rotateTowards(address.text().charAt(i));
		} else {
			this.lock();
			i++;
		}

		int finalI = i;
		Scheduler.get().runTaskLater(() -> this.internalDial(address, unit, delay, finalI), unit, delay);
	}

	/**
	 * @param current the current glyph
	 * @param target the target glyph
	 * @return whether it is faster to call next or previous
	 */
	private boolean isNextFaster(char current, char target) {
		int currentIndex = this.indexOf(current);
		int targetIndex = this.indexOf(target);

		int next = (currentIndex + 1) % GLYPHS.length;
		int previous = (currentIndex + GLYPHS.length - 1) % GLYPHS.length;

		int nextDistance = Math.abs(next - targetIndex);
		int previousDistance = Math.abs(previous - targetIndex);

		return nextDistance < previousDistance;
	}
	public void rotateTowards(char target) {
		if (this.selected == target) return;

		if (this.isNextFaster(this.selected, target)) {
			this.next();
		} else {
			this.previous();
		}
	}

	/**
	 * Dials the target address over a period of time
	 * @param address the address to dial
	 */
	public boolean dial(Address address) {
		return this.dial(address, TimeUnit.TICKS, 10);
	}

	/**
	 * Dials the target glyph over a period of time
	 * @param c the target glyph
	 */
	public boolean dial(char c) {
		return this.dial(c, TimeUnit.TICKS, 10);
	}

	public boolean contains(char glyph) {
		return this.target.contains(String.valueOf(glyph));
	}
}
