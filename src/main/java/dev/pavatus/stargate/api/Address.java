package dev.pavatus.stargate.api;

import dev.pavatus.lib.data.DirectedGlobalPos;
import dev.pavatus.stargate.StargateMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * Represents a Stargate address.
 * 6 Characters Long
 * @param text address in string form
 * @param pos the position of the Stargate
 */
public record Address(String text, DirectedGlobalPos pos) {
	private static final Identifier FONT_ID = new Identifier("minecraft", "alt");
	private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);

	/**
	 * Creates a new address with a random string of characters.
	 * @param pos the position of the Stargate
	 */
	public Address(DirectedGlobalPos pos) {
		this(randomAddress(pos.getDimension().getValue()), pos);
	}

	/**
	 * @return the address in enchanting glyphs
	 */
	public Text toGlyphs() {
		return toGlyphs(text);
	}

	public static Text toGlyphs(String text) {
		return Text.literal(text).fillStyle(STYLE);
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.putString("Text", text);
		nbt.put("Position", pos.toNbt());

		return nbt;
	}

	public static Address fromNbt(NbtCompound nbt) {
		String text = nbt.getString("Text");

		DirectedGlobalPos pos = DirectedGlobalPos.fromNbt(nbt.getCompound("Position"));

		return new Address(text, pos);
	}

	private static String randomAddress() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			builder.append((char) ('A' + StargateMod.RANDOM.nextInt(Dialer.GLYPHS.length)));
		}
		return builder.toString();
	}
	private static String randomAddress(Identifier world) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			builder.append((char) ('A' + StargateMod.RANDOM.nextInt(Dialer.GLYPHS.length)));
		}

		builder.append(PointOfOriginRegistry.getInstance().get(world).glyph());
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;
		return text.equals(address.text);
	}

	@Override
	public int hashCode() {
		int result = text.hashCode();
		result = 31 * result + Objects.hashCode(pos);
		return result;
	}
}
