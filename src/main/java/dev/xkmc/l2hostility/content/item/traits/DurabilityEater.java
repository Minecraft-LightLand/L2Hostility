package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DurabilityEater {

	public static void corrosion(LivingEntity target, EquipmentSlot slot) {
		ItemStack stack = target.getItemBySlot(slot);
		if (!stack.isDamageableItem()) return;
		double factor = LHConfig.SERVER.corrosionDurability.get();
		int add = (int) (stack.getDamageValue() * factor);
		if (add <= 0) return;
		stack.hurtAndBreak(add, target, slot);
	}

	public static void erosion(LivingEntity target, EquipmentSlot slot) {
		ItemStack stack = target.getItemBySlot(slot);
		if (!stack.isDamageableItem()) return;
		double factor = LHConfig.SERVER.erosionDurability.get();
		int add = (int) ((stack.getMaxDamage() - stack.getDamageValue()) * factor);
		if (add <= 0) return;
		stack.hurtAndBreak(add, target, slot);
	}

	public static void flat(LivingEntity target, EquipmentSlot slot, double factor) {
		ItemStack stack = target.getItemBySlot(slot);
		if (!stack.isDamageableItem()) return;
		int add = (int) (stack.getMaxDamage() * factor);
		if (add <= 0) return;
		stack.hurtAndBreak(add, target, slot);
	}

}
