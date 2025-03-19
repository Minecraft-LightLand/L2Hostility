package dev.xkmc.l2hostility.init.data;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.alexthe666.iceandfire.IceAndFire;
import dev.shadowsoffire.gateways.Gateways;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2hostility.compat.data.*;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2weaponry.init.L2Weaponry;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import fuzs.mutantmonsters.MutantMonsters;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class LHConfigGen extends ConfigDataProvider {

	public LHConfigGen(DataGenerator generator) {
		super(generator, "L2Hostility Config");
	}

	@Override
	public void add(Collector collector) {
		L2Hostility.REGISTRATE.CONFIGS.forEach(e -> e.accept(collector));

		collector.add(L2DamageTracker.ARMOR, new ResourceLocation(L2Hostility.MODID, "equipments"), new ArmorEffectConfig()
				.add(LHItems.CURSE_WRATH.getId().toString(),
						MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.CONFUSION,
						MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS));

		collector.add(L2Hostility.DIFFICULTY, new ResourceLocation(L2Hostility.MODID, "overworld"), new WorldDifficultyConfig()
				.putDim(Level.OVERWORLD, 0, 0, 4, 1)
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
				.putDim(Level.NETHER, 0, 20, 9, 1.2)
				.putLevelDef(Level.NETHER, EntityConfig.entity(0, 10, 0, 0, List.of(EntityType.ZOMBIE))
						.trait(List.of(EntityConfig.trait(LHTraits.TANK.get(), 1, 1))))
				.putLevelDef(Level.NETHER, EntityConfig.entity(0, 10, 0, 0, List.of(EntityType.SKELETON))
						.trait(List.of(EntityConfig.trait(LHTraits.SPEEDY.get(), 1, 1))))
				.putStructureDef(BuiltinStructures.BASTION_REMNANT, EntityConfig.entity(0, 20, 0, 0, List.of(EntityType.PIGLIN))
						.trait(List.of(EntityConfig.trait(LHTraits.PROTECTION.get(), 1, 1))))
		);

		collector.add(L2Hostility.DIFFICULTY, new ResourceLocation(L2Hostility.MODID, "end"), new WorldDifficultyConfig()
				.putDim(Level.END, 0, 40, 16, 1.5)
		);

		collector.add(L2Hostility.ENTITY, new ResourceLocation(L2Hostility.MODID, "bosses"), new EntityConfig()
				.putEntity(0, 20, 1, 0, List.of(EntityType.ELDER_GUARDIAN), List.of(
						EntityConfig.trait(LHTraits.REPELLING.get(), 1, 1, 300, 0.5f)
				))
				.putEntity(0, 20, 1, 0, List.of(EntityType.PIGLIN_BRUTE), List.of(
						EntityConfig.trait(LHTraits.PULLING.get(), 1, 1, 300, 0.5f)
				))
				.putEntity(0, 20, 1, 0, List.of(EntityType.WARDEN), List.of(
						EntityConfig.trait(LHTraits.DISPELL.get(), 1, 1, 200, 1),
						EntityConfig.trait(LHTraits.REPRINT.get(), 1, 1, 300, 1)
				))
				.putEntity(0, 50, 1, 0, List.of(EntityType.WITHER), List.of(
						EntityConfig.trait(LHTraits.CURSED.get(), 0, 1)
				))
				.putEntity(100, 50, 1, 0, List.of(EntityType.ENDER_DRAGON), List.of())
		);


		collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Hostility.MODID, "armors"), new WeaponConfig()
				.putArmor(0, 200, Items.AIR)
				.putArmor(20, 100, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS)
				.putArmor(35, 100, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS)
				.putArmor(45, 100, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)
				.putArmor(60, 200, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS)
				.putArmor(80, 300, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS)
				.putArmor(100, 100, Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS)
		);

		collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Hostility.MODID, "vanilla"), new WeaponConfig()
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

		collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Complements.MODID, "l2complements"), new WeaponConfig()
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
						LCEnchantments.FLAME_THORN.get(),
						LCEnchantments.SAFEGUARD.get()
				)
		);
		{
			var config = new WeaponConfig();
			config.special_weapons.put(new LinkedHashSet<>(List.of(
					EntityType.ZOMBIE, EntityType.HUSK, EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON, EntityType.ZOMBIFIED_PIGLIN
			)), new ArrayList<>(List.of(
					new WeaponConfig.ItemConfig(new ArrayList<>(List.of(LCItems.SONIC_SHOOTER.asStack())), 150, 100),
					new WeaponConfig.ItemConfig(new ArrayList<>(List.of(LCItems.WINTERSTORM_WAND.asStack())), 180, 80),
					new WeaponConfig.ItemConfig(new ArrayList<>(List.of(LCItems.HELLFIRE_WAND.asStack())), 200, 50)
			)));
			collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Complements.MODID, "special"), config);
		}

		collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Weaponry.MODID, "weapons"), new WeaponConfig()
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

		collector.add(L2Hostility.WEAPON, new ResourceLocation(L2Archery.MODID, "bows"), new WeaponConfig()
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
		if (ModList.get().isLoaded(Cataclysm.MODID)) {
			CataclysmData.genConfig(collector);
		}
		if (ModList.get().isLoaded("bosses_of_mass_destruction")) {
			BoMDData.genConfig(collector);
		}
		if (ModList.get().isLoaded(IceAndFire.MODID)) {
			IaFData.genConfig(collector);
		}
		if (ModList.get().isLoaded(Gateways.MODID)) {
			GatewayConfigGen.genConfig(collector);
		}
		if (ModList.get().isLoaded(MutantMonsters.MOD_ID)) {
			MutantMonsterData.genConfig(collector);
		}
		if (ModList.get().isLoaded(MowziesMobs.MODID)) {
			MowzieData.genConfig(collector);
		}
	}

	public static <T extends LivingEntity> void addEntity(Collector collector, int min, int base, RegistryObject<EntityType<T>> obj, EntityConfig.TraitBase... traits) {
		collector.add(L2Hostility.ENTITY, obj.getId(), new EntityConfig().putEntity(min, base, 0, 0, List.of(obj.get()), List.of(traits)));
	}

	public static <T extends LivingEntity> void addEntity(Collector collector, int min, int base, RegistryObject<EntityType<T>> obj, List<EntityConfig.TraitBase> traits, List<EntityConfig.ItemPool> items) {
		collector.add(L2Hostility.ENTITY, obj.getId(), new EntityConfig().putEntityAndItem(min, base, 0, 0, List.of(obj.get()), traits, items));
	}

}