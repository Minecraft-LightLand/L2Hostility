package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.HashMap;

@SerialClass
public class WorldDifficultyConfig extends BaseConfig {

	public static DifficultyConfig defaultLevel() {
		int base = LHConfig.COMMON.defaultLevelBase.get();
		double var = LHConfig.COMMON.defaultLevelVar.get();
		double scale = LHConfig.COMMON.defaultLevelScale.get();
		return new DifficultyConfig(0, base, var, scale, 1, 1);
	}

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public final HashMap<ResourceLocation, DifficultyConfig> levelMap = new HashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public final HashMap<ResourceLocation, DifficultyConfig> biomeMap = new HashMap<>();

	public record DifficultyConfig(int min, int base, double variation, double scale, double apply_chance,
								   double trait_chance) {

	}

	public WorldDifficultyConfig putDim(ResourceKey<DimensionType> key, int min, int base, double var, double scale) {
		levelMap.put(key.location(), new DifficultyConfig(min, base, var, scale, 1, 1));
		return this;
	}

	@SafeVarargs
	public final WorldDifficultyConfig putBiome(int min, int base, double var, double scale, ResourceKey<Biome>... keys) {
		for (var key : keys) {
			biomeMap.put(key.location(), new DifficultyConfig(min, base, var, scale, 1, 1));
		}
		return this;
	}

}
