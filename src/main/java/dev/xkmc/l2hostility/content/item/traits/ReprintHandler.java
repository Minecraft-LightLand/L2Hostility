package dev.xkmc.l2hostility.content.item.traits;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReprintHandler {

	public static void reprint(ItemStack dst, ItemStack src) {
		if (!dst.isEnchanted() && !dst.isEnchantable() || !src.isEnchanted()) return;
		var selfEnch = dst.getAllEnchantments();
		var targetEnch = src.getAllEnchantments();
		Map<Enchantment, Integer> newEnch = new LinkedHashMap<>();
		for (var pair : targetEnch.entrySet()) {
			Enchantment e = pair.getKey();
			if (!dst.canApplyAtEnchantingTable(e)) continue;
			if (!allow(newEnch, e)) continue;
			int lv = pair.getValue();
			newEnch.compute(e, (k, v) -> v == null ? lv : Math.max(v, lv));
		}
		for (var pair : selfEnch.entrySet()) {
			Enchantment e = pair.getKey();
			if (!dst.canApplyAtEnchantingTable(e)) continue;
			if (!allow(newEnch, e)) continue;
			int lv = pair.getValue();
			newEnch.compute(e, (k, v) -> v == null ? lv : Math.max(v, lv));
		}
		EnchantmentHelper.setEnchantments(newEnch, dst);
	}

	private static boolean allow(Map<Enchantment, Integer> map, Enchantment ench) {
		if (map.containsKey(ench)) return true;
		for (var e : map.keySet()) {
			if (!e.isCompatibleWith(ench)) {
				return false;
			}
		}
		return true;
	}

}
