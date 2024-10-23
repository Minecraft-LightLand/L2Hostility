package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnchantmentDisabler {

	private static final String ROOT = "l2hostility_enchantment", OLD = "originalEnchantments", TIME = "startTime";

	public static void disableEnchantment(Level level, ItemStack stack, int duration) {
		var enchs = stack.get(DataComponents.ENCHANTMENTS);
		if (enchs == null) return;
		double durability = stack.getMaxDamage() == 0 ? 0 : 1d * stack.getDamageValue() / stack.getMaxDamage();
		ItemEnchantments.Mutable retain = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		ItemEnchantments.Mutable disabled = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (var e : enchs.entrySet()) {
			if (e.getKey().is(LHTagGen.NO_DISPELL)) {
				retain.set(e.getKey(), e.getIntValue());
			} else {
				disabled.set(e.getKey(), e.getIntValue());
			}
		}
		if (disabled.keySet().isEmpty()) return;
		stack.set(DataComponents.ENCHANTMENTS, retain.toImmutable());
		LHItems.DC_DISPELL_START.set(stack, level.getGameTime() + duration);
		LHItems.DC_DISPELL_ENCH.set(stack, disabled.toImmutable());
		if (stack.isDamageableItem()) {
			stack.setDamageValue(Mth.clamp((int) Math.floor(durability * stack.getMaxDamage()), 0, stack.getMaxDamage() - 1));
		}
	}

	public static void tickStack(Level level, Entity user, ItemStack stack) {
		if (level.isClientSide()) return;
		if (user instanceof Player player && !player.getAbilities().instabuild && stack.isEnchanted() &&
				stack.getEnchantmentLevel(LHEnchantments.VANISH.holder()) > 0) {
			stack.setCount(0);
			return;
		}
		var disabled = LHItems.DC_DISPELL_ENCH.get(stack);
		if (disabled == null) return;
		long time = LHItems.DC_DISPELL_START.getOrDefault(stack, 0L);
		if (level.getGameTime() < time) return;
		stack.remove(LHItems.DC_DISPELL_ENCH);
		stack.remove(LHItems.DC_DISPELL_START);
		var builder = new ItemEnchantments.Mutable(disabled);
		var enchs = stack.get(DataComponents.ENCHANTMENTS);
		if (enchs != null) {
			for (var e : enchs.entrySet()) {
				builder.set(e.getKey(), e.getIntValue());
			}
		}
		EnchantmentHelper.setEnchantments(stack, builder.toImmutable());
	}

	public static void modifyTooltip(ItemStack stack, List<Component> tooltip, Level level, Item.TooltipContext context, TooltipFlag flags) {
		var disabled = LHItems.DC_DISPELL_ENCH.get(stack);
		if (disabled == null) return;
		long time = Math.max(0, LHItems.DC_DISPELL_START.getOrDefault(stack, 0L) - level.getGameTime());
		tooltip.add(LangData.TOOLTIP_DISABLE.get(
						Component.literal(disabled.size() + "")
								.withStyle(ChatFormatting.LIGHT_PURPLE),
						Component.literal(time / 20 + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.RED));
		disabled.addToTooltip(context, e -> tooltip.add(e.copy().withStyle(ChatFormatting.DARK_GRAY)), flags);
	}

}
