package dev.pavatus.stargate.api;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.stargate.StargateMod;
import dev.pavatus.stargate.core.StargateSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
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
	private Rotation lastRotation;
	private int rotationTicks;
	private int maxRotationTicks;
	private boolean isDHD;
	private boolean pointOfOrigin;

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
		this.isAutoDialing = nbt.getBoolean("AutoDialing");
		this.rotationTicks = nbt.getInt("RotationTicks");
		this.maxRotationTicks = nbt.getInt("MaxRotationTicks");
		if (nbt.contains("LastRotation")) {
			this.lastRotation = Rotation.valueOf(nbt.getString("LastRotation"));
		} else {
			this.lastRotation = Rotation.FORWARD;
		}
		this.isDHD = nbt.getBoolean("IsDHD");
		this.pointOfOrigin = nbt.getBoolean("PointOfOrigin");
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("Target", this.target);
		nbt.putString("Selected", String.valueOf(this.selected));
		nbt.putBoolean("AutoDialing", this.isAutoDialing);
		nbt.putInt("RotationTicks", this.getRotationTicks());
		nbt.putInt("MaxRotationTicks", this.getMaxRotationTicks());
		if (this.lastRotation == null) this.lastRotation = Rotation.FORWARD;
		nbt.putString("LastRotation", this.lastRotation.name());
		nbt.putBoolean("IsDHD", this.isDHD);
		nbt.putBoolean("PointOfOrigin", this.pointOfOrigin);
		return nbt;
	}

	public void setRotationTicks(int rotationTicks) {
		this.rotationTicks = rotationTicks;
		this.parent.sync();
	}

	public void setMaxRotationTicks(int maxRotationTicks) {
		this.maxRotationTicks = maxRotationTicks;
		this.parent.sync();
	}

	private int getRotationTicks() {
		return rotationTicks;
	}

	private int getMaxRotationTicks() {
		return maxRotationTicks;
	}
	public float getRotationProgress() {
		return MathHelper.clamp((float) this.getRotationTicks() / ((float) this.getMaxRotationTicks()), 0, 1);
	}

	public String getCurrentDialSequence() {
		return this.target;
	}

	public boolean dial(String address, TimeUnit unit, long delay) {
		if (this.isAutoDialing) return false;
		delay = delay / 2;

		if (address.length() > 1){
			this.clear();
		}

		this.setMaxRotationTicks((int) TimeUnit.TICKS.from(unit, delay));
		this.internalDial(address, 0);
		this.isAutoDialing = true;
		return true;
	}

	public boolean dial(Address address, TimeUnit unit, long delay) {
		return this.dial(address.text(), unit, delay);
	}

	public boolean dial(char target, TimeUnit unit, long delay) {
		return this.dial(String.valueOf(target), unit, delay);
	}

	private void internalDial(String address, int i) {
		if (i == address.length()) {
			this.isAutoDialing = false;
			return;
		}

		if (this.selected == address.charAt(i)) {
			this.rotationTicks = this.getMaxRotationTicks();
		}

		if (i == 0 && this.rotateTowards(address.charAt(i), true)) {
			this.lastRotation = this.lastRotation == Rotation.FORWARD ? Rotation.BACKWARD : Rotation.FORWARD;
		}
		if (this.getRotationTicks() < this.getMaxRotationTicks()) {
			this.setRotationTicks(this.getRotationTicks() + 1);
		} else {
			this.setRotationTicks(0);
			if (this.selected != address.charAt(i)) {
				this.rotateTowards(address.charAt(i), false);
			} else {
				this.lock();
				i++;
				this.setRotationTicks(-this.getMaxRotationTicks());

				if (i != address.length() && this.rotateTowards(address.charAt(i), true)) {
					this.lastRotation = this.lastRotation == Rotation.FORWARD ? Rotation.BACKWARD : Rotation.FORWARD;
				}
			}
		}

		int finalI = i;
		Scheduler.get().runTaskLater(() -> this.internalDial(address, finalI), TimeUnit.TICKS, 1);
	}

	/**
	 * @return whether the dialer is filled
	 */
	public boolean isComplete() {
		return this.getIsDHD() ? this.pointOfOrigin && this.target.length() == 7: this.target.length() == 7;
	}

	public void setPointOfOrigin(boolean pointOfOrigin) {
		this.pointOfOrigin = pointOfOrigin;
		if (this.pointOfOrigin) this.parent.handleDialerComplete(this);
		this.parent.sync();
	}

	public void setIsDHD(boolean isDHD) {
		if (this.isDHD) return;
		this.isDHD = isDHD;
		this.parent.sync();
	}

	public boolean getIsDHD() {
		return this.isDHD;
	}

	public Optional<Stargate> toStargate(World world) {
		if (!this.isComplete()) return Optional.empty();

		Address found = StargateNetwork.getInstance(world).getAddress(this.target).orElse(null);
		if (found == null) return Optional.empty();

		return StargateNetwork.getInstance(world).getOptional(found);
	}
	public Optional<StargateCall> complete(Stargate source, World world) {
		Stargate target = this.toStargate(world).orElse(null);
		System.out.println(target);

		this.clear();

		if (target == null) {
			this.setIsDHD(false);
			this.setPointOfOrigin(false);
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
		this.isAutoDialing = false;
		this.setIsDHD(false);
		this.setPointOfOrigin(false);
		this.parent.sync();
		return old;
	}

	/**
	 * Locks a chevron in place
	 * Adds it to the target address
	 */
	public void lock() {
		this.append(this.selected);
		this.parent.sync();

		this.parent.playSound(StargateSounds.CHEVRON_LOCK, 0.25f, StargateMod.RANDOM.nextFloat(0.9f, 1.1f));
	}
	public char getSelected() {
		return this.selected;
	}

	public void setSelected(char glyph) {
		this.selected = glyph;
		this.parent.sync();
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

	private boolean simulateRotateTowards(char target) {
		if (this.selected == target) return false;

		Rotation before = this.lastRotation;
		Rotation after = before;

		if (this.isNextFaster(this.selected, target)) {
			after = Rotation.FORWARD;
		} else {
			after = Rotation.BACKWARD;
		}

		return before != after;
	}
	private boolean rotateTowards(char target, boolean simulate) {
		if (this.selected == target) return false;

		if (simulate) {
			return this.simulateRotateTowards(target);
		}

		Rotation before = this.lastRotation;

		if (this.isNextFaster(this.selected, target)) {
			this.next();
			this.lastRotation = Rotation.FORWARD;
		} else {
			this.previous();
			this.lastRotation = Rotation.BACKWARD;
		}

		return before != this.lastRotation;
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

	/**
	 * @return whether its currently moving to a glyph
	 */
	public boolean isDialing() {
		return this.isAutoDialing;
	}

	/**
	 * @return the last direction the ring was rotating in
	 */
	public Rotation getRotation() {
		return this.lastRotation;
	}

	public enum Rotation {
		FORWARD, BACKWARD
	}
}
