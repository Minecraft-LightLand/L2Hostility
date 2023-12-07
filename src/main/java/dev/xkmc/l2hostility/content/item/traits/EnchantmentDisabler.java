package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentDisabler {

	private static final String ENCH = "Enchantments", ROOT = "l2hostility_enchantment", OLD = "originalEnchantments", TIME = "startTime";

	public static void disableEnchantment(Level level, ItemStack stack, int duration) {
		CompoundTag root = stack.getOrCreateTag();
		if (!root.contains(ENCH, Tag.TAG_LIST)) return;
		double durability = stack.getMaxDamage() == 0 ? 0 : 1d * stack.getDamageValue() / stack.getMaxDamage();
		CompoundTag tag = stack.getOrCreateTagElement(ROOT);
		var list = root.getList(ENCH, Tag.TAG_COMPOUND);
		var cache = new ListTag();
		list.removeIf(e -> {
			if (noDispell(e)) return false;
			cache.add(e);
			return true;
		});
		tag.put(OLD, cache);
		tag.putLong(TIME, level.getGameTime() + duration);
		if (stack.isDamageableItem()) {
			stack.setDamageValue(Mth.clamp((int) Math.floor(durability * stack.getMaxDamage()), 0, stack.getMaxDamage() - 1));
		}
	}

	private static boolean noDispell(Tag e) {
		if (!(e instanceof CompoundTag c)) return false;
		var id = new ResourceLocation(c.getString("id"));
		return ForgeRegistries.ENCHANTMENTS.tags()
				.getTag(LHTagGen.NO_DISPELL)
				.contains(ForgeRegistries.ENCHANTMENTS.getValue(id));
	}

	public static void tickStack(Level level, ItemStack stack) {
		if (level.isClientSide()) return;
		if (stack.getTag() == null || !stack.getTag().contains(ROOT, Tag.TAG_COMPOUND)) return;
		CompoundTag root = stack.getOrCreateTag();
		CompoundTag tag = stack.getOrCreateTagElement(ROOT);
		long time = tag.getLong(TIME);
		if (level.getGameTime() >= time) {
			stack.getTag().remove(ROOT);
			var list = root.getList(ENCH, Tag.TAG_COMPOUND);
			var cache = tag.getList(OLD, Tag.TAG_COMPOUND);
			cache.addAll(list);
			stack.getTag().put(ENCH, cache);
		}
	}

	public static void modifyTooltip(ItemStack stack, List<Component> tooltip, Level level) {
		if (stack.getTag() == null || !stack.getTag().contains(ROOT, Tag.TAG_COMPOUND)) return;
		CompoundTag tag = stack.getOrCreateTagElement(ROOT);
		long time = Math.max(0, tag.getLong(TIME) - level.getGameTime());
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
