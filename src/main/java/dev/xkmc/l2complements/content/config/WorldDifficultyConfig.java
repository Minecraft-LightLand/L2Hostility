package dev.xkmc.l2complements.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.HashMap;

@SerialClass
public class WorldDifficultyConfig extends BaseConfig {

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public final HashMap<ResourceLocation, DifficultyConfig> levelMap = new HashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public final HashMap<ResourceLocation, DifficultyConfig> biomeMap = new HashMap<>();

	public record DifficultyConfig(int base, double variation, double scale) {

	}

	public WorldDifficultyConfig putDim(ResourceKey<DimensionType> key, int base, double var, double scale) {
		levelMap.put(key.location(), new DifficultyConfig(base, var, scale));
		return this;
	}

	@SafeVarargs
	public final WorldDifficultyConfig putBiome(int base, double var, double scale, ResourceKey<Biome>... keys) {
		for (var key : keys) {
			biomeMap.put(key.location(), new DifficultyConfig(base, var, scale));
		}
		return this;
	}

}
