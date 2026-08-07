package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class HostilityEditorHandlers {

	public static final EditorHandler<EntityType<?>> ENTITY_TYPE = EditorHandler.of(
			HostilityEditorUtil::entityName, HostilityEditorUtil::entityIcon);

	public static final EditorHandler<MobTrait> TRAIT = EditorHandler.of(
			HostilityEditorUtil::traitName, HostilityEditorUtil::traitIcon);

	public static final EditorHandler<Enchantment> ENCHANTMENT = EditorHandler.of(
			e -> HostilityEditorUtil.enchantName(e), e -> new ItemStack(Items.ENCHANTED_BOOK));

	public static final EditorHandler<ResourceKey<Biome>> BIOME = EditorHandler.of(
			HostilityEditorUtil::biomeName, null);

	public static final EditorHandler<ResourceKey<Structure>> STRUCTURE = EditorHandler.of(
			HostilityEditorUtil::structureName, null);

	public static final EditorHandler<Item> ITEM = EditorHandler.of(
			e -> e.getName(ItemStack.EMPTY), Item::getDefaultInstance);

}
