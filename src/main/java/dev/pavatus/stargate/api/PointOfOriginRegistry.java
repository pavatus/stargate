package dev.pavatus.stargate.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.pavatus.lib.api.Identifiable;
import dev.pavatus.lib.register.datapack.SimpleDatapackRegistry;
import dev.pavatus.stargate.StargateMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public class PointOfOriginRegistry extends SimpleDatapackRegistry<PointOfOriginRegistry.Symbol> {
	private PointOfOriginRegistry() {
		super(Symbol::fromInputStream, Symbol.CODEC, "point_of_origin", true, StargateMod.MOD_ID);
	}

	@Override
	protected void defaults() {
		register(new Symbol(World.OVERWORLD, 'Q'));
	}

	@Override
	public Symbol fallback() {
		return get(World.OVERWORLD.getValue());
	}

	@Override
	public Symbol get(Identifier id) {
		return this.REGISTRY.computeIfAbsent(id, key -> new Symbol(key, (char) ('A' + StargateMod.RANDOM.nextInt(Dialer.GLYPHS.length))));
	}

	public record Symbol(Identifier dimension, char glyph) implements Identifiable {
		public static final Codec<Symbol> CODEC = Codecs.exceptionCatching(RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("dimension").forGetter(Symbol::dimension),
				Codec.STRING.fieldOf("glyph").forGetter(symbol -> String.valueOf(symbol.glyph()))
		).apply(instance, Symbol::new)));

		private Symbol(Identifier dimension, String glyph) {
			this(dimension, glyph.charAt(0));
		}
		public Symbol(RegistryKey<World> dimension, char glyph) {
			this(dimension.getValue(), glyph);
		}
		private static char cleanseChar(char input) {
			// ensure its an upper case A-Z
			return (char) ('A' + (input - 'A') % 26);
		}

		public static Symbol fromInputStream(InputStream stream) {
			return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
		}

		public static Symbol fromJson(JsonObject json) {
			AtomicReference<Symbol> created = new AtomicReference<>();

			CODEC.decode(JsonOps.INSTANCE, json).get().ifLeft(planet -> created.set(planet.getFirst())).ifRight(err -> {
				created.set(null);
				StargateMod.LOGGER.error("Error decoding datapack symbol: {}", err);
			});

			return created.get();
		}

		@Override
		public Identifier id() {
			return dimension;
		}
	}

	private static PointOfOriginRegistry instance;
	public static PointOfOriginRegistry getInstance() {
		if (instance == null) {
			instance = new PointOfOriginRegistry();
		}

		return instance;
	}
}
