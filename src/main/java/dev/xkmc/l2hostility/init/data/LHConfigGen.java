package dev.xkmc.l2hostility.init.data;

import com.github.L_Ender.cataclysm.cataclysm;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.network.ArmorEffectConfig;
import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2hostility.compat.data.TFData;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.serial.network.ConfigDataProvider;
import dev.xkmc.l2weaponry.init.L2Weaponry;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LHConfigGen extends ConfigDataProvider {

	public LHConfigGen(DataGenerator generator) {
		super(generator, "data/", "L2Hostility Config");
	}

	@Override
	public void add(Map<String, BaseConfig> collector) {
		L2Hostility.REGISTRATE.CONFIGS.forEach(e -> e.accept(collector));
		var config = new ArmorEffectConfig();
		config.immune.put(LHItems.CURSE_WRATH.getId().toString(), new LinkedHashSet<>(Set.of(
				MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.CONFUSION,
				MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS)));
		L2Hostility.ARMOR.add(collector, new ResourceLocation(L2Hostility.MODID, "equipments"), config);

		L2Hostility.DIFFICULTY.add(collector, new ResourceLocation(L2Hostility.MODID, "overworld"), new WorldDifficultyConfig()
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

		L2Hostility.DIFFICULTY.add(collector, new ResourceLocation(L2Hostility.MODID, "nether"), new WorldDifficultyConfig()
				.putDim(BuiltinDimensionTypes.NETHER, 0, 20, 9, 1.2)
		);

		L2Hostility.DIFFICULTY.add(collector, new ResourceLocation(L2Hostility.MODID, "end"), new WorldDifficultyConfig()
				.putDim(BuiltinDimensionTypes.END, 0, 40, 16, 1.5)
		);

		L2Hostility.ENTITY.add(collector, new ResourceLocation(L2Hostility.MODID, "bosses"), new EntityConfig()
				.putEntity(0, 20, 1, 0, List.of(EntityType.ELDER_GUARDIAN, EntityType.PIGLIN_BRUTE), List.of())
				.putEntity(0, 50, 1, 0, List.of(EntityType.WITHER),
						List.of(new EntityConfig.TraitBase(LHTraits.CURSED.get(), 0, 1)))
				.putEntity(100, 50, 1, 0, List.of(EntityType.ENDER_DRAGON), List.of())
		);

		L2Hostility.WEAPON.add(collector, new ResourceLocation(L2Hostility.MODID, "vanilla"), new WeaponConfig()
				.putMeleeWeapon(0, 200, Items.AIR)
				.putMeleeWeapon(30, 100, Items.IRON_AXE, Items.IRON_SWORD)
				.putMeleeWeapon(50, 100, Items.DIAMOND_AXE, Items.DIAMOND_SWORD)
				.putMeleeWeapon(70, 100, Items.NETHERITE_AXE, Items.NETHERITE_SWORD)
				.putRangedWeapon(0, 100,
						Items.AIR
				)
				.putWeaponEnch(30, 0.5f,
						Enchantments.SHARPNESS,
						Enchantments.POWER_ARROWS
				)
				.putWeaponEnch(40, 0.2f,
						Enchantments.KNOCKBACK,
						Enchantments.PUNCH_ARROWS
				)
				.putWeaponEnch(50, 0.1f,
						Enchantments.FIRE_ASPECT,
						Enchantments.FLAMING_ARROWS
				)
				.putArmorEnch(30, 0.5f, Enchantments.ALL_DAMAGE_PROTECTION)
				.putArmorEnch(30, 0.2f,
						Enchantments.PROJECTILE_PROTECTION,
						Enchantments.BLAST_PROTECTION,
						Enchantments.FIRE_PROTECTION,
						Enchantments.FALL_PROTECTION
				)
				.putArmorEnch(70, 0.3f, Enchantments.BINDING_CURSE)
		);

		L2Hostility.WEAPON.add(collector, new ResourceLocation(L2Complements.MODID, "l2complements"), new WeaponConfig()
				.putWeaponEnch(100, 0.02f,
						LCEnchantments.CURSE_BLADE.get(),
						LCEnchantments.SHARP_BLADE.get(),
						LCEnchantments.FLAME_BLADE.get(),
						LCEnchantments.ICE_BLADE.get()
				)
				.putWeaponEnch(200, 0.01f,
						LCEnchantments.VOID_TOUCH.get()
				)
				.putArmorEnch(70, 0.2f,
						LCEnchantments.STABLE_BODY.get(),
						LCEnchantments.SNOW_WALKER.get()
				)
				.putArmorEnch(100, 0.02f,
						LCEnchantments.ICE_THORN.get(),
						LCEnchantments.FLAME_THORN.get()
						//TODO 	LCEnchantments.SAFEGUARD.get()
				)
		);


		L2Hostility.WEAPON.add(collector, new ResourceLocation(L2Weaponry.MODID, "weapons"), new WeaponConfig()
				.putMeleeWeapon(200, 10,
						LWItems.STORM_JAVELIN.get(),
						LWItems.FLAME_AXE.get(),
						LWItems.FROZEN_SPEAR.get()
				)
				.putMeleeWeapon(300, 5,
						LWItems.ABYSS_MACHETE.get(),
						LWItems.HOLY_AXE.get(),
						LWItems.BLACK_AXE.get()
				)
				.putMeleeWeapon(400, 2,
						LWItems.CHEATER_CLAW.get(),
						LWItems.CHEATER_MACHETE.get()
				)
		);

		L2Hostility.WEAPON.add(collector, new ResourceLocation(L2Archery.MODID, "bows"), new WeaponConfig()
				.putRangedWeapon(50, 10,
						ArcheryItems.STARTER_BOW.get()
				)
				.putRangedWeapon(70, 5,
						ArcheryItems.IRON_BOW.get(),
						ArcheryItems.MASTER_BOW.get()
				)
				.putRangedWeapon(100, 2,
						ArcheryItems.FLAME_BOW.get(),
						ArcheryItems.BLACKSTONE_BOW.get(),
						ArcheryItems.TURTLE_BOW.get(),
						ArcheryItems.EAGLE_BOW.get(),
						ArcheryItems.EXPLOSION_BOW.get(),
						ArcheryItems.FROZE_BOW.get()
				)
		);

		if (ModList.get().isLoaded(TwilightForestMod.ID)) {
			TFData.genConfig(collector);
		}
		if (ModList.get().isLoaded(cataclysm.MODID)) {
			CataclysmData.genConfig(collector);
		}
	}

	public static <T extends LivingEntity> void addEntity(Map<String, BaseConfig> collector, int min, int base, RegistryObject<EntityType<T>> obj, EntityConfig.TraitBase... traits) {
		L2Hostility.ENTITY.add(collector, obj.getId(), new EntityConfig().putEntity(min, base, 0, 0, List.of(obj.get()), List.of(traits)));
	}

	public static <T extends LivingEntity> void addEntity(Map<String, BaseConfig> collector, int min, int base, RegistryObject<EntityType<T>> obj, List<EntityConfig.TraitBase> traits, List<EntityConfig.ItemPool> items) {
		L2Hostility.ENTITY.add(collector, obj.getId(), new EntityConfig().putEntityAndItem(min, base, 0, 0, List.of(obj.get()), traits, items));
	}

}