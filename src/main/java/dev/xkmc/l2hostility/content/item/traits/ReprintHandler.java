package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReprintHandler {

	public static void reprint(RegistryAccess access, ItemStack dst, ItemStack src) {
		if (!dst.isEnchanted() && !dst.isEnchantable() || !src.isEnchanted()) return;
		var reg = access.lookupOrThrow(Registries.ENCHANTMENT);
		var selfEnch = dst.getAllEnchantments(reg);
		var targetEnch = src.getAllEnchantments(reg);
		Map<Holder<Enchantment>, Integer> newEnch = new LinkedHashMap<>();
		for (var pair : targetEnch.entrySet()) {
			var e = pair.getKey();
			if (e.is(LHTagGen.NO_REPRINT)) continue;
			if (!dst.isPrimaryItemFor(e)) continue;
			if (!allow(newEnch, e)) continue;
			int lv = pair.getIntValue();
			newEnch.compute(e, (k, v) -> v == null ? lv : Math.max(v, lv));
		}
		for (var pair : selfEnch.entrySet()) {
			var e = pair.getKey();
			if (!dst.isPrimaryItemFor(e)) continue;
			if (!allow(newEnch, e)) continue;
			int lv = pair.getIntValue();
			newEnch.compute(e, (k, v) -> v == null ? lv : Math.max(v, lv));
		}
		var builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (var e : newEnch.entrySet()) {
			builder.set(e.getKey(), e.getValue());
		}
		EnchantmentHelper.setEnchantments(dst, builder.toImmutable());
	}

	private static boolean allow(Map<Holder<Enchantment>, Integer> map, Holder<Enchantment> ench) {
		if (map.containsKey(ench)) return true;
		for (var e : map.keySet()) {
			if (!Enchantment.areCompatible(e, ench)) {
				return false;
			}
		}
		return true;
	}

}
