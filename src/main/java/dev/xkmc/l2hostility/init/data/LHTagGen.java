package dev.xkmc.l2hostility.init.data;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.cerbon.bosses_of_mass_destruction.entity.BMDEntities;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import fuzs.mutantmonsters.MutantMonsters;
import fuzs.mutantmonsters.init.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class LHTagGen {

	public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<Enchantment>> ENCH_TAGS =
			ProviderType.register("tags/enchantment",
					type -> (p, e) -> new RegistrateTagsProvider.IntrinsicImpl<>(p, type, "enchantments",
							e.getGenerator().getPackOutput(), Registries.ENCHANTMENT, e.getLookupProvider(),
							ench -> ResourceKey.create(ForgeRegistries.ENCHANTMENTS.getRegistryKey(),
									ForgeRegistries.ENCHANTMENTS.getKey(ench)),
							e.getExistingFileHelper()));

	public static final TagKey<Item> CHAOS_CURIO = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "chaos_equipment"));
	public static final TagKey<Item> CURSE_SLOT = ItemTags.create(new ResourceLocation("curios", "hostility_curse"));
	public static final TagKey<Item> TRAIT_ITEM = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "trait_item"));
	public static final TagKey<Item> NO_SEAL = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "no_seal"));
	public static final TagKey<Item> ANTIBUILD_BAN = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "antibuild_ban"));

	public static final TagKey<Block> BEACON_BLOCK = BlockTags.create(new ResourceLocation(L2Hostility.MODID, "beacon"));
	public static final TagKey<Item> BEACON_PAYMENT = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "beacon_payment"));

	public static final TagKey<Enchantment> NO_DISPELL = TagKey.create(Registries.ENCHANTMENT,
			new ResourceLocation(L2Hostility.MODID, "no_dispell"));

	public static final TagKey<EntityType<?>> BLACKLIST = createEntityTag("blacklist");
	public static final TagKey<EntityType<?>> WHITELIST = createEntityTag("whitelist");
	public static final TagKey<EntityType<?>> NO_SCALING = createEntityTag("no_scaling");
	public static final TagKey<EntityType<?>> NO_TRAIT = createEntityTag("no_trait");

	public static final TagKey<EntityType<?>> SEMIBOSS = createEntityTag("semiboss");
	public static final TagKey<EntityType<?>> NO_DROP = createEntityTag("no_drop");

	public static final TagKey<EntityType<?>> ARMOR_TARGET = createEntityTag("armor_target");
	public static final TagKey<EntityType<?>> MELEE_WEAPON_TARGET = createEntityTag("melee_weapon_target");
	public static final TagKey<EntityType<?>> RANGED_WEAPON_TARGET = createEntityTag("ranged_weapon_target");
	public static final TagKey<EntityType<?>> HOSTILITY_SPAWNER_BLACKLIST = createEntityTag("hostility_spawner_blacklist");

	public static final Map<ResourceLocation, Consumer<RegistrateTagsProvider.IntrinsicImpl<EntityType<?>>>> ENTITY_TAG_BUILDER = new TreeMap<>();

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(BlockTags.BEACON_BASE_BLOCKS).addTag(BEACON_BLOCK);
	}

	public static void onEnchTagGen(RegistrateTagsProvider.IntrinsicImpl<Enchantment> pvd) {
		pvd.addTag(NO_DISPELL).add(Enchantments.UNBREAKING,
				LCEnchantments.LIFE_SYNC.get(),
				LCEnchantments.HARDENED.get(),
				LCEnchantments.SAFEGUARD.get(),
				LCEnchantments.ETERNAL.get(),
				LCEnchantments.DURABLE_ARMOR.get(),
				LCEnchantments.SOUL_BOUND.get(),
				LHEnchantments.SPLIT_SUPPRESS.get(),
				LHEnchantments.INSULATOR.get(),
				LHEnchantments.VANISH.get()
		);
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(NO_SEAL)
				.addOptional(new ResourceLocation("enigmaticlegacy:cursed_ring"));
		pvd.addTag(ANTIBUILD_BAN)
				.addOptional(new ResourceLocation("enigmaticlegacy:extradimensional_eye"));
	}

	public static void onTraitTagGen(RegistrateTagsProvider.IntrinsicImpl<MobTrait> pvd) {
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(BLACKLIST);
		pvd.addTag(WHITELIST);
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
				EntityType.SKELETON, EntityType.STRAY
		);

		pvd.addTag(SEMIBOSS).addTag(Tags.EntityTypes.BOSSES)
				.add(EntityType.WARDEN, EntityType.ELDER_GUARDIAN, EntityType.RAVAGER);

		if (ModList.get().isLoaded(TwilightForestMod.ID)) {
			pvd.addTag(NO_DROP).addOptional(TFEntities.DEATH_TOME.getId());
		}

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
					.addOptional(ModRegistry.MUTANT_ENDERMAN_ENTITY_TYPE.getResourceLocation())
					.addOptional(ModRegistry.MUTANT_CREEPER_ENTITY_TYPE.getResourceLocation())
					.addOptional(ModRegistry.MUTANT_ZOMBIE_ENTITY_TYPE.getResourceLocation())
					.addOptional(ModRegistry.MUTANT_SKELETON_ENTITY_TYPE.getResourceLocation())
					.addOptional(ModRegistry.SPIDER_PIG_ENTITY_TYPE.getResourceLocation());

			pvd.addTag(WHITELIST)
					.addOptional(ModRegistry.SPIDER_PIG_ENTITY_TYPE.getResourceLocation());

		}
		if (ModList.get().isLoaded(MowziesMobs.MODID)) {
			pvd.addTag(SEMIBOSS)
					.addOptional(EntityHandler.FROSTMAW.getId())
					.addOptional(EntityHandler.UMVUTHI.getId())
					.addOptional(EntityHandler.WROUGHTNAUT.getId());
		}

		ENTITY_TAG_BUILDER.values().forEach(e -> e.accept(pvd));
	}

	public static TagKey<EntityType<?>> createEntityTag(String id) {
		return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), new ResourceLocation(L2Hostility.MODID, id));
	}

	public static TagKey<MobTrait> createTraitTag(String id) {
		return TagKey.create(LHTraits.TRAITS.key(), new ResourceLocation(L2Hostility.MODID, id));
	}

	public static void onEffTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		pvd.addTag(TagGen.SKILL_EFFECT).add(LHEffects.ANTIBUILD.get());
	}

}
