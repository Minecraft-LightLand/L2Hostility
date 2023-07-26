package dev.xkmc.l2complements.init.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public abstract class Biomes {
	public static final ResourceKey<Biome> THE_VOID = register("the_void");

	public static final ResourceKey<Biome> PLAINS = register("plains");
	public static final ResourceKey<Biome> SUNFLOWER_PLAINS = register("sunflower_plains");
	public static final ResourceKey<Biome> SNOWY_PLAINS = register("snowy_plains");

	public static final ResourceKey<Biome> OLD_GROWTH_BIRCH_FOREST = register("old_growth_birch_forest");
	public static final ResourceKey<Biome> OLD_GROWTH_PINE_TAIGA = register("old_growth_pine_taiga");
	public static final ResourceKey<Biome> OLD_GROWTH_SPRUCE_TAIGA = register("old_growth_spruce_taiga");

	public static final ResourceKey<Biome> TAIGA = register("taiga");
	public static final ResourceKey<Biome> SNOWY_TAIGA = register("snowy_taiga");

	public static final ResourceKey<Biome> MEADOW = register("meadow");

	public static final ResourceKey<Biome> CHERRY_GROVE = register("cherry_grove");
	public static final ResourceKey<Biome> GROVE = register("grove");

	public static final ResourceKey<Biome> RIVER = register("river");
	public static final ResourceKey<Biome> FROZEN_RIVER = register("frozen_river");

	public static final ResourceKey<Biome> BEACH = register("beach");
	public static final ResourceKey<Biome> SNOWY_BEACH = register("snowy_beach");

	public static final ResourceKey<Biome> WARM_OCEAN = register("warm_ocean");
	public static final ResourceKey<Biome> LUKEWARM_OCEAN = register("lukewarm_ocean");
	public static final ResourceKey<Biome> OCEAN = register("ocean");
	public static final ResourceKey<Biome> COLD_OCEAN = register("cold_ocean");
	public static final ResourceKey<Biome> FROZEN_OCEAN = register("frozen_ocean");

	public static final ResourceKey<Biome> NETHER_WASTES = register("nether_wastes");
	public static final ResourceKey<Biome> WARPED_FOREST = register("warped_forest");
	public static final ResourceKey<Biome> CRIMSON_FOREST = register("crimson_forest");
	public static final ResourceKey<Biome> SOUL_SAND_VALLEY = register("soul_sand_valley");
	public static final ResourceKey<Biome> BASALT_DELTAS = register("basalt_deltas");

	public static final ResourceKey<Biome> THE_END = register("the_end");
	public static final ResourceKey<Biome> END_HIGHLANDS = register("end_highlands");
	public static final ResourceKey<Biome> END_MIDLANDS = register("end_midlands");
	public static final ResourceKey<Biome> SMALL_END_ISLANDS = register("small_end_islands");
	public static final ResourceKey<Biome> END_BARRENS = register("end_barrens");

	private static ResourceKey<Biome> register(String p_48229_) {
		return ResourceKey.create(Registries.BIOME, new ResourceLocation(p_48229_));
	}
}