package dev.xkmc.l2hostility.init.data;

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
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

	public static final TagKey<Enchantment> NO_DISPELL = TagKey.create(Registries.ENCHANTMENT,
			new ResourceLocation(L2Hostility.MODID, "no_dispell"));

	public static final TagKey<EntityType<?>> BLACKLIST = createEntityTag("blacklist");
	public static final TagKey<EntityType<?>> WHITELIST = createEntityTag("whitelist");
	public static final TagKey<EntityType<?>> NO_SCALING = createEntityTag("no_scaling");
	public static final TagKey<EntityType<?>> NO_TRAIT = createEntityTag("no_trait");

	public static final TagKey<EntityType<?>> SEMIBOSS = createEntityTag("semiboss");

	public static final TagKey<EntityType<?>> ARMOR_TARGET = createEntityTag("armor_target");
	public static final TagKey<EntityType<?>> MELEE_WEAPON_TARGET = createEntityTag("melee_weapon_target");
	public static final TagKey<EntityType<?>> RANGED_WEAPON_TARGET = createEntityTag("ranged_weapon_target");

	public static final Map<ResourceLocation, Consumer<RegistrateTagsProvider.IntrinsicImpl<EntityType<?>>>> ENTITY_TAG_BUILDER = new TreeMap<>();

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
	}

	public static void onEnchTagGen(RegistrateTagsProvider.IntrinsicImpl<Enchantment> pvd) {
		pvd.addTag(NO_DISPELL).add(Enchantments.UNBREAKING,
				LCEnchantments.LIFE_SYNC.get(),
				LCEnchantments.HARDENED.get(),
				LCEnchantments.SAFEGUARD.get(),
				LCEnchantments.ETERNAL.get(),
				LCEnchantments.DURABLE_ARMOR.get(),
				LCEnchantments.SOUL_BOUND.get()
		);
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(NO_SEAL)
				.addOptional(new ResourceLocation("enigmaticlegacy:curse_ring"))
				.addOptional(new ResourceLocation("moonstone:sevencurse"));
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

		ENTITY_TAG_BUILDER.values().forEach(e -> e.accept(pvd));

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
					.addOptional(ModEntities.AMETHYST_CRAB.getId());
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
