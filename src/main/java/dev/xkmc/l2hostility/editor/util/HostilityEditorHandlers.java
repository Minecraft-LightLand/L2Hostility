package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public final class HostilityEditorHandlers {

	private static String key(@Nullable ResourceLocation rl) {
		return rl == null ? "" : rl.toString();
	}

	public static final EditorHandler<EntityType<?>> ENTITY_TYPE = EditorHandler.of(
			HostilityEditorUtil::entityName, HostilityEditorUtil::entityIcon,
			t -> key(ForgeRegistries.ENTITY_TYPES.getKey(t)));

	public static final EditorHandler<MobTrait> TRAIT = EditorHandler.of(
			HostilityEditorUtil::traitName, HostilityEditorUtil::traitIcon,
			t -> key(t.getRegistryName()));

	public static final EditorHandler<Enchantment> ENCHANTMENT = EditorHandler.of(
			e -> HostilityEditorUtil.enchantName(e), e -> new ItemStack(Items.ENCHANTED_BOOK),
			e -> key(ForgeRegistries.ENCHANTMENTS.getKey(e)));

	public static final EditorHandler<ResourceKey<Biome>> BIOME = EditorHandler.of(
			HostilityEditorUtil::biomeName, null,
			k -> k.location().toString());

	public static final EditorHandler<ResourceKey<Structure>> STRUCTURE = EditorHandler.of(
			HostilityEditorUtil::structureName, null,
			k -> k.location().toString());

	public static final EditorHandler<Item> ITEM = EditorHandler.of(
			e -> e.getName(ItemStack.EMPTY), Item::getDefaultInstance,
			e -> key(ForgeRegistries.ITEMS.getKey(e)));

}
