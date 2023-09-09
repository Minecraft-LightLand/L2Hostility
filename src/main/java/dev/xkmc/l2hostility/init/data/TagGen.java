package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class TagGen {

	public static final TagKey<EntityType<?>> BLACKLIST = createEntityTag("blacklist");
	public static final TagKey<EntityType<?>> NO_SCALING = createEntityTag("no_scaling");
	public static final TagKey<EntityType<?>> NO_TRAIT = createEntityTag("no_trait");

	public static final TagKey<EntityType<?>> ARMOR_TARGET = createEntityTag("armor_target");
	public static final TagKey<EntityType<?>> MELEE_WEAPON_TARGET = createEntityTag("melee_weapon_target");

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
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

	}

	public static TagKey<EntityType<?>> createEntityTag(String id) {
		return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), new ResourceLocation(L2Hostility.MODID, id));
	}

}
