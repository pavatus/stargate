package dev.pavatus.stargate.api;

import dev.pavatus.stargate.StargateMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

/**
 * Represents a Stargate address.
 * @param text address in string form
 * @param pos the position of the Stargate
 */
public record Address(String text, GlobalPos pos) {
	private static final Identifier FONT_ID = new Identifier("minecraft", "alt");
	private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);

	/**
	 * Creates a new address with a random string of characters.
	 * @param pos the position of the Stargate
	 */
	public Address(GlobalPos pos) {
		this(randomAddress(), pos);
	}

	/**
	 * @return the address in enchanting glyphs
	 */
	public Text toGlyphs() {
		return Text.literal(text).fillStyle(STYLE);
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.putString("Text", text);

		nbt.put("Position", NbtHelper.fromBlockPos(pos.getPos()));
		nbt.putString("Dimension", pos.getDimension().getValue().toString());

		return nbt;
	}

	public static Address fromNbt(NbtCompound nbt) {
		String text = nbt.getString("Text");

		RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD,
				new Identifier(nbt.getString("Dimension")));
		GlobalPos pos = GlobalPos.create(dimension, NbtHelper.toBlockPos(nbt.getCompound("Position")));

		return new Address(text, pos);
	}

	private static String randomAddress() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			builder.append((char) ('A' + StargateMod.RANDOM.nextInt(26)));
		}
		return builder.toString();
	}
}
