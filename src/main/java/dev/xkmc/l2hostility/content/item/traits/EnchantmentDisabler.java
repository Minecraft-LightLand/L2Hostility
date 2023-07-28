package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentDisabler {

	private static final String ENCH = "Enchantments", ROOT = "l2hostility_enchantment", OLD = "originalEnchantments", TIME = "startTime";

	public static void disableEnchantment(Level level, ItemStack stack, int duration) {
		CompoundTag root = stack.getOrCreateTag();
		if (!root.contains(ENCH, Tag.TAG_LIST)) return;
		CompoundTag tag = stack.getOrCreateTagElement(ROOT);
		tag.put(OLD, root.getList(ENCH, Tag.TAG_COMPOUND));
		tag.putLong(TIME, level.getGameTime() + duration);
	}

	public static void tickStack(Level level, ItemStack stack) {
		if (level.isClientSide()) return;
		if (stack.getTag() == null || !stack.getTag().contains(ROOT, Tag.TAG_COMPOUND)) return;
		CompoundTag root = stack.getOrCreateTag();
		CompoundTag tag = stack.getOrCreateTagElement(ROOT);
		long time = tag.getLong(TIME);
		if (level.getGameTime() >= time) {
			stack.getTag().remove(ROOT);
			stack.getTag().put(ENCH, tag.getList(OLD, Tag.TAG_COMPOUND));
		} else if (root.contains(ENCH, Tag.TAG_LIST)) {
			root.remove(ENCH);
		}
	}

	public static void modifyTooltip(ItemStack stack, List<Component> tooltip, Level level) {
		if (stack.getTag() == null || !stack.getTag().contains(ROOT, Tag.TAG_COMPOUND)) return;
		CompoundTag tag = stack.getOrCreateTagElement(ROOT);
		long time = tag.getLong(TIME) - level.getGameTime();
		ListTag list = tag.getList(OLD, Tag.TAG_COMPOUND);
		tooltip.add(LangData.TOOLTIP_DISABLE.get(
						Component.literal(list.size() + "")
								.withStyle(ChatFormatting.LIGHT_PURPLE),
						Component.literal(time / 20 + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.RED));
		List<Component> disabled = new ArrayList<>();
		ItemStack.appendEnchantmentNames(disabled, list);
		for (var e : disabled) {
			tooltip.add(e.copy().withStyle(ChatFormatting.DARK_GRAY));
		}
	}

}
