package dev.xkmc.l2hostility.init.data;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class TagGen {

	public static final TagKey<Item> CHAOS_CURIO = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "chaos_equipment"));
	public static final TagKey<Item> CURSE_SLOT = ItemTags.create(new ResourceLocation("curios", "hostility_curse"));
	public static final TagKey<Item> TRAIT_ITEM = ItemTags.create(new ResourceLocation(L2Hostility.MODID, "trait_item"));

	public static final TagKey<EntityType<?>> BLACKLIST = createEntityTag("blacklist");
	public static final TagKey<EntityType<?>> NO_SCALING = createEntityTag("no_scaling");
	public static final TagKey<EntityType<?>> NO_TRAIT = createEntityTag("no_trait");

	public static final TagKey<EntityType<?>> ARMOR_TARGET = createEntityTag("armor_target");
	public static final TagKey<EntityType<?>> MELEE_WEAPON_TARGET = createEntityTag("melee_weapon_target");

	public static final Map<ResourceLocation, Consumer<RegistrateTagsProvider.IntrinsicImpl<EntityType<?>>>> ENTITY_TAG_BUILDER = new TreeMap<>();

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
	}

	public static void onTraitTagGen(RegistrateTagsProvider.IntrinsicImpl<MobTrait> pvd) {
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(BLACKLIST).add(EntityType.ENDERMITE);
		pvd.addTag(NO_SCALING).addTag(BLACKLIST);
		pvd.addTag(NO_TRAIT).addTag(BLACKLIST);

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

		ENTITY_TAG_BUILDER.values().forEach(e -> e.accept(pvd));

		if (ModList.get().isLoaded(Cataclysm.MODID)) {
			pvd.addTag(Tags.EntityTypes.BOSSES)
					.addOptional(ModEntities.ENDER_GOLEM.getId())
					.addOptional(ModEntities.ENDER_GUARDIAN.getId())
					.addOptional(ModEntities.NETHERITE_MONSTROSITY.getId())
					.addOptional(ModEntities.IGNIS.getId())
					.addOptional(ModEntities.THE_HARBINGER.getId())
					.addOptional(ModEntities.THE_LEVIATHAN.getId())
					.addOptional(ModEntities.AMETHYST_CRAB.getId());
		}
	}

	public static TagKey<EntityType<?>> createEntityTag(String id) {
		return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), new ResourceLocation(L2Hostility.MODID, id));
	}

	public static TagKey<MobTrait> createTraitTag(String id) {
		return TagKey.create(LHTraits.TRAITS.key(), new ResourceLocation(L2Hostility.MODID, id));
	}

}
