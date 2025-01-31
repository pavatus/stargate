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

public class PointOfOriginRegistry extends SimpleDatapackRegistry<PointOfOriginRegistry.PointOfOrigin> {
	private PointOfOriginRegistry() {
		super(PointOfOrigin::fromInputStream, PointOfOrigin.CODEC, "point_of_origin", true);
	}

	@Override
	protected void defaults() {
		register(new PointOfOrigin(World.OVERWORLD, 'Q'));
	}

	@Override
	public PointOfOrigin fallback() {
		return get(World.OVERWORLD.getValue());
	}

	@Override
	public PointOfOrigin get(Identifier id) {
		return this.REGISTRY.computeIfAbsent(id, key -> new PointOfOrigin(key, (char) ('A' + StargateMod.RANDOM.nextInt(Dialer.GLYPHS.length))));
	}

	public record PointOfOrigin(Identifier dimension, char glyph) implements Identifiable {
		public static final Codec<PointOfOrigin> CODEC = Codecs.exceptionCatching(RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("dimension").forGetter(PointOfOrigin::dimension),
				Codec.STRING.fieldOf("glyph").forGetter(pointOfOrigin -> String.valueOf(pointOfOrigin.glyph()))
		).apply(instance, PointOfOrigin::new)));

		private PointOfOrigin(Identifier dimension, String glyph) {
			this(dimension, glyph.charAt(0));
		}
		public PointOfOrigin(RegistryKey<World> dimension, char glyph) {
			this(dimension.getValue(), glyph);
		}
		private static char cleanseChar(char input) {
			// ensure its an upper case A-Z
			return (char) ('A' + (input - 'A') % 26);
		}

		public static PointOfOrigin fromInputStream(InputStream stream) {
			return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
		}

		public static PointOfOrigin fromJson(JsonObject json) {
			AtomicReference<PointOfOrigin> created = new AtomicReference<>();

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
