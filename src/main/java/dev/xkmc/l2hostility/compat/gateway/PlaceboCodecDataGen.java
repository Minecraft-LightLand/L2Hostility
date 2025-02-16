package dev.xkmc.l2hostility.compat.gateway;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.shadowsoffire.placebo.codec.CodecProvider;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class PlaceboCodecDataGen<T extends CodecProvider<T>> implements DataProvider {
	private final DataGenerator generator;
	private final String name;
	private final Map<String, T> map = new HashMap<>();

	public PlaceboCodecDataGen(DataGenerator generator, String name) {
		this.generator = generator;
		this.name = name;
	}

	public abstract void add(BiConsumer<String, T> map);

	public CompletableFuture<?> run(CachedOutput cache) {
		Path folder = this.generator.getPackOutput().getOutputFolder();
		this.add(map::put);
		List<CompletableFuture<?>> list = new ArrayList<>();
		this.map.forEach((k, v) -> {
			JsonElement elem = v.getCodec().encodeStart(JsonOps.INSTANCE, Wrappers.cast(v))
					.getOrThrow();
			if (elem != null) {
				Path path = folder.resolve("data/" + k + ".json");
				list.add(DataProvider.saveStable(cache, elem, path));
			}
		});
		return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
	}

	public String getName() {
		return this.name;
	}

}
