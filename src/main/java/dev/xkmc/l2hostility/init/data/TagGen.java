package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class TagGen {

	public static final TagKey<EntityType<?>> BLACKLIST = create("blacklist");
	public static final TagKey<EntityType<?>> NO_SCALING = create("no_scaling");
	public static final TagKey<EntityType<?>> NO_MODIFIER = create("no_modifier");

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(BLACKLIST).add(EntityType.ENDERMITE);
		pvd.addTag(NO_SCALING).addTag(BLACKLIST);
		pvd.addTag(NO_MODIFIER).addTag(BLACKLIST);
	}

	public static TagKey<EntityType<?>> create(String id) {
		return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), new ResourceLocation(L2Hostility.MODID, id));
	}

}
