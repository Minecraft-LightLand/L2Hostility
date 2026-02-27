package dev.xkmc.l2hostility.init.data;

import com.cerbon.bosses_of_mass_destruction.entity.BMDEntities;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2complements.init.data.LCTagGen;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import fuzs.mutantmonsters.MutantMonsters;
import fuzs.mutantmonsters.init.ModEntityTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class LHTagGen {

	public static final TagKey<Item> CHAOS_CURIO = ItemTags.create(L2Hostility.loc("chaos_equipment"));
	public static final TagKey<Item> CURSE_SLOT = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "hostility_curse"));
	public static final TagKey<Item> TRAIT_ITEM = ItemTags.create(L2Hostility.loc("trait_item"));
	public static final TagKey<Item> NO_SEAL = ItemTags.create(L2Hostility.loc("no_seal"));
	public static final TagKey<Item> ANTIBUILD_BAN = ItemTags.create(L2Hostility.loc("antibuild_ban"));

	public static final TagKey<Block> BEACON_BLOCK = BlockTags.create(L2Hostility.loc("beacon"));
	public static final TagKey<Item> BEACON_PAYMENT = ItemTags.create(L2Hostility.loc("beacon_payment"));

	public static final TagKey<Enchantment> NO_DISPELL = TagKey.create(Registries.ENCHANTMENT,
			L2Hostility.loc("no_dispell"));
	public static final TagKey<Enchantment> NO_REPRINT = TagKey.create(Registries.ENCHANTMENT,
			L2Hostility.loc("no_reprint"));

	public static final TagKey<EntityType<?>> BLACKLIST = createEntityTag("blacklist");
	public static final TagKey<EntityType<?>> WHITELIST = createEntityTag("whitelist");
	public static final TagKey<EntityType<?>> NO_SCALING = createEntityTag("no_scaling");
	public static final TagKey<EntityType<?>> NO_TRAIT = createEntityTag("no_trait");
	public static final TagKey<EntityType<?>> HOSTILITY_SPAWNER_BLACKLIST = createEntityTag("hostility_spawner_blacklist");

	public static final TagKey<EntityType<?>> SEMIBOSS = createEntityTag("semiboss");
	public static final TagKey<EntityType<?>> NO_DROP = createEntityTag("no_drop");
	public static final TagKey<EntityType<?>> HIDE_TRAITS = createEntityTag("hide_traits");
	public static final TagKey<EntityType<?>> HIDE_LEVEL = createEntityTag("hide_level");
	public static final TagKey<EntityType<?>> HIDE_TITLE = createEntityTag("hide_title");

	public static final TagKey<EntityType<?>> ARMOR_TARGET = createEntityTag("armor_target");
	public static final TagKey<EntityType<?>> MELEE_WEAPON_TARGET = createEntityTag("melee_weapon_target");
	public static final TagKey<EntityType<?>> RANGED_WEAPON_TARGET = createEntityTag("ranged_weapon_target");

	public static final Map<ResourceLocation, Consumer<RegistrateTagsProvider.IntrinsicImpl<EntityType<?>>>> ENTITY_TAG_BUILDER = new TreeMap<>();

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(BlockTags.BEACON_BASE_BLOCKS).addTag(BEACON_BLOCK);
	}

	public static void onEnchTagGen(RegistrateTagsProvider<Enchantment> pvd) {
		pvd.addTag(NO_DISPELL).add(Enchantments.UNBREAKING,
						LHEnchantments.SPLIT_SUPPRESS.id(),
						LHEnchantments.INSULATOR.id(),
						LHEnchantments.VANISH.id())
				.addOptional(LCEnchantments.LIFE_SYNC.id().location())
				.addOptional(LCEnchantments.HARDENED.id().location())
				.addOptional(LCEnchantments.SAFEGUARD.id().location())
				.addOptional(LCEnchantments.ETERNAL.id().location())
				.addOptional(LCEnchantments.DURABLE_ARMOR.id().location())
				.addOptional(LCEnchantments.SOUL_BOUND.id().location());
		pvd.addTag(NO_REPRINT)
				.addOptional(LCEnchantments.ETERNAL.id().location())
				.addOptional(LCEnchantments.INVINCIBLE.id().location());
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(NO_SEAL)
				.addOptional(ResourceLocation.fromNamespaceAndPath("enigmaticlegacy", "cursed_ring"));
		pvd.addTag(ANTIBUILD_BAN)
				.addOptional(ResourceLocation.fromNamespaceAndPath("enigmaticlegacy", "extradimensional_eye"));
	}

	public static void onTraitTagGen(RegistrateTagsProvider.IntrinsicImpl<MobTrait> pvd) {
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(BLACKLIST);
		pvd.addTag(WHITELIST);
		pvd.addTag(HIDE_TITLE);
		pvd.addTag(HIDE_TRAITS).addTag(HIDE_TITLE);
		pvd.addTag(HIDE_LEVEL).addTag(HIDE_TITLE);
		pvd.addTag(NO_SCALING).addTag(BLACKLIST);
		pvd.addTag(NO_TRAIT).addTag(BLACKLIST).add(EntityType.ENDERMITE);
		pvd.addTag(HOSTILITY_SPAWNER_BLACKLIST).addTags(NO_SCALING, NO_TRAIT);
		pvd.addTag(ARMOR_TARGET).add(
				EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER,
				EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON,
				EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN_BRUTE
		);

		pvd.addTag(MELEE_WEAPON_TARGET).add(
				EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER,
				EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN_BRUTE,
				EntityType.WITHER_SKELETON, EntityType.VINDICATOR
		);

		pvd.addTag(RANGED_WEAPON_TARGET).add(
				EntityType.SKELETON, EntityType.BOGGED, EntityType.STRAY
		);

		pvd.addTag(SEMIBOSS).addTag(Tags.EntityTypes.BOSSES)
				.add(EntityType.WARDEN, EntityType.ELDER_GUARDIAN, EntityType.RAVAGER);


		if (ModList.get().isLoaded(Cataclysm.MODID)) {
			pvd.addTag(SEMIBOSS)
					.addOptional(ModEntities.ENDER_GOLEM.getId())
					.addOptional(ModEntities.ENDER_GUARDIAN.getId())
					.addOptional(ModEntities.NETHERITE_MONSTROSITY.getId())
					.addOptional(ModEntities.IGNIS.getId())
					.addOptional(ModEntities.THE_HARBINGER.getId())
					.addOptional(ModEntities.THE_LEVIATHAN.getId())
					.addOptional(ModEntities.AMETHYST_CRAB.getId())
					.addOptional(ModEntities.ANCIENT_REMNANT.getId())
					.addOptional(ModEntities.IGNITED_BERSERKER.getId())
					.addOptional(ModEntities.KOBOLEDIATOR.getId())
					.addOptional(ModEntities.WADJET.getId())
					.addOptional(ModEntities.MALEDICTUS.getId())
					.addOptional(ModEntities.APTRGANGR.getId());
		}

		if (ModList.get().isLoaded(TwilightForestMod.ID)) {
			pvd.addTag(NO_DROP).addOptional(TFEntities.DEATH_TOME.getId());
		}

		if (ModList.get().isLoaded("bosses_of_mass_destruction")) {
			pvd.addTag(SEMIBOSS)
					.addOptional(BMDEntities.LICH.getId())
					.addOptional(BMDEntities.GAUNTLET.getId())
					.addOptional(BMDEntities.OBSIDILITH.getId())
					.addOptional(BMDEntities.VOID_BLOSSOM.getId());

			pvd.addTag(WHITELIST)
					.addOptional(BMDEntities.LICH.getId())
					.addOptional(BMDEntities.GAUNTLET.getId())
					.addOptional(BMDEntities.OBSIDILITH.getId())
					.addOptional(BMDEntities.VOID_BLOSSOM.getId());

		}


		if (ModList.get().isLoaded(MutantMonsters.MOD_ID)) {
			pvd.addTag(SEMIBOSS)
					.addOptional(ModEntityTypes.MUTANT_ENDERMAN_ENTITY_TYPE.key().location())
					.addOptional(ModEntityTypes.MUTANT_CREEPER_ENTITY_TYPE.key().location())
					.addOptional(ModEntityTypes.MUTANT_ZOMBIE_ENTITY_TYPE.key().location())
					.addOptional(ModEntityTypes.MUTANT_SKELETON_ENTITY_TYPE.key().location())
					.addOptional(ModEntityTypes.SPIDER_PIG_ENTITY_TYPE.key().location());

			pvd.addTag(WHITELIST)
					.addOptional(ModEntityTypes.SPIDER_PIG_ENTITY_TYPE.key().location());

		}

		/* TODO

		if (ModList.get().isLoaded(IceAndFire.MODID)) {
			pvd.addTag(SEMIBOSS)
					.addOptional(IafEntityRegistry.ICE_DRAGON.getId())
					.addOptional(IafEntityRegistry.FIRE_DRAGON.getId())
					.addOptional(IafEntityRegistry.LIGHTNING_DRAGON.getId())
					.addOptional(IafEntityRegistry.DEATH_WORM.getId())
					.addOptional(IafEntityRegistry.SEA_SERPENT.getId());

			pvd.addTag(WHITELIST)
					.addOptional(IafEntityRegistry.ICE_DRAGON.getId())
					.addOptional(IafEntityRegistry.FIRE_DRAGON.getId())
					.addOptional(IafEntityRegistry.LIGHTNING_DRAGON.getId())
					.addOptional(IafEntityRegistry.DEATH_WORM.getId())
					.addOptional(IafEntityRegistry.SEA_SERPENT.getId());
		}


		if (ModList.get().isLoaded(AlexsCaves.MODID)) {
			pvd.addTag(SEMIBOSS)
					.addOptional(ACEntityRegistry.HULLBREAKER.getId());

			pvd.addTag(WHITELIST)
					.addOptional(ACEntityRegistry.HULLBREAKER.getId())
					.addOptional(ACEntityRegistry.VALLUMRAPTOR.getId())
					.addOptional(ACEntityRegistry.GROTTOCERATOPS.getId())
					.addOptional(ACEntityRegistry.RELICHEIRUS.getId())
					.addOptional(ACEntityRegistry.SUBTERRANODON.getId())
					.addOptional(ACEntityRegistry.TREMORSAURUS.getId());
		}

		if (ModList.get().isLoaded(MowziesMobs.MODID)) {
			pvd.addTag(SEMIBOSS)
					.addOptional(EntityHandler.FROSTMAW.getId())
					.addOptional(EntityHandler.UMVUTHI.getId())
					.addOptional(EntityHandler.WROUGHTNAUT.getId());
		}

		 */

		ENTITY_TAG_BUILDER.values().forEach(e -> e.accept(pvd));
	}

	public static TagKey<EntityType<?>> createEntityTag(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, L2Hostility.loc(id));
	}

	public static TagKey<MobTrait> createTraitTag(String id) {
		return TagKey.create(LHTraits.TRAITS.key(), L2Hostility.loc(id));
	}

	public static void onEffTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		pvd.addTag(LCTagGen.SKILL_EFFECT).add(LHEffects.ANTIBUILD.get());
	}

}
