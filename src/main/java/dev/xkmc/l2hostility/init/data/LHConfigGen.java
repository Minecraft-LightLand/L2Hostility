package dev.xkmc.l2hostility.init.data;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.init.ModEntities;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2hostility.compat.data.TFData;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LHConfigGen extends ConfigDataProvider {

	public static final List<Consumer<Collector>> LIST = new ArrayList<>();

	public LHConfigGen(DataGenerator generator) {
		super(generator, "L2Hostility Config");
	}

	@Override
	public void add(Collector collector) {
		LIST.forEach(e -> e.accept(collector));

		collector.add(L2DamageTracker.ARMOR, new ResourceLocation(L2Hostility.MODID, "equipments"), new ArmorEffectConfig()
				.add(LHItems.CURSE_WRATH.getId().toString(),
						MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.CONFUSION,
						MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS));

		collector.add(L2Hostility.DIFFICULTY, new ResourceLocation(L2Hostility.MODID, "overworld"), new WorldDifficultyConfig()
				.putDim(BuiltinDimensionTypes.OVERWORLD, 0, 0, 4, 1)
				.putBiome(0, 5, 1, 0,
						Biomes.LUSH_CAVES,
						Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST,
						Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE,
						Biomes.DESERT,
						Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU,
						Biomes.TAIGA, Biomes.SNOWY_TAIGA
				)
				.putBiome(0, 5, 1, 0.2,
						Biomes.DRIPSTONE_CAVES,
						Biomes.DARK_FOREST,
						Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_SAVANNA
				)
				.putBiome(0, 10, 1, 0.2,
						Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS,
						Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN,
						Biomes.MUSHROOM_FIELDS,
						Biomes.STONY_SHORE,
						Biomes.SWAMP, Biomes.MANGROVE_SWAMP
				)
				.putBiome(0, 20, 1, 0.2,
						Biomes.SNOWY_SLOPES,
						Biomes.ICE_SPIKES,
						Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS
				)
				.putBiome(0, 50, 4, 0.5, Biomes.DEEP_DARK)
		);

		collector.add(L2Hostility.DIFFICULTY, new ResourceLocation(L2Hostility.MODID, "nether"), new WorldDifficultyConfig()
				.putDim(BuiltinDimensionTypes.NETHER, 0, 20, 9, 1.2)
		);

		collector.add(L2Hostility.DIFFICULTY, new ResourceLocation(L2Hostility.MODID, "end"), new WorldDifficultyConfig()
				.putDim(BuiltinDimensionTypes.END, 0, 40, 16, 1.5)
		);

		collector.add(L2Hostility.ENTITY, new ResourceLocation(L2Hostility.MODID, "bosses"), new EntityConfig()
				.putEntity(0, 20, 1, 0, List.of(EntityType.ELDER_GUARDIAN, EntityType.PIGLIN_BRUTE), List.of())
				.putEntity(0, 50, 1, 0, List.of(EntityType.WITHER),
						List.of(new EntityConfig.TraitBase(LHTraits.CURSED.get(), 0, 1)))
				.putEntity(100, 50, 1, 0, List.of(EntityType.ENDER_DRAGON), List.of())
		);

		collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Hostility.MODID, "melee"), new WeaponConfig()
				.putMeleeWeapon(0, 200, Items.AIR)
				.putMeleeWeapon(30, 100, Items.IRON_AXE, Items.IRON_SWORD)
				.putMeleeWeapon(50, 100, Items.DIAMOND_AXE, Items.DIAMOND_SWORD)
				.putMeleeWeapon(70, 100, Items.NETHERITE_AXE, Items.NETHERITE_SWORD)
		);


		if (ModList.get().isLoaded(TwilightForestMod.ID)) {
			TFData.genConfig(collector);
		}
		if (ModList.get().isLoaded(Cataclysm.MODID)) {
			CataclysmData.genConfig(collector);
		}
	}

	public static <T extends LivingEntity> void addEntity(Collector collector, int min, int base, RegistryObject<EntityType<T>> obj, EntityConfig.TraitBase... traits) {
		collector.add(L2Hostility.ENTITY, obj.getId(), new EntityConfig().putEntity(min, base, 0, 0, List.of(obj.get()), List.of(traits)));
	}

}