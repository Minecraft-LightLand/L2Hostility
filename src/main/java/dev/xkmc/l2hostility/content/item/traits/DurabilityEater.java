package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DurabilityEater {

	public static void corrosion(LivingEntity user, EquipmentSlot slot) {
		ItemStack stack = user.getItemBySlot(slot);
		if (!stack.isDamageableItem()) return;
		double factor = LHConfig.COMMON.corrosionFactor.get();
		int add = (int) (stack.getDamageValue() * factor);
		if (add <= 0) return;
		stack.hurtAndBreak(add, user, e -> e.broadcastBreakEvent(slot));
	}

	public static void erosion(LivingEntity user, EquipmentSlot slot) {
		ItemStack stack = user.getItemBySlot(slot);
		if (!stack.isDamageableItem()) return;
		double factor = LHConfig.COMMON.erosionFactor.get();
		int add = (int) ((stack.getMaxDamage() - stack.getDamageValue()) * factor);
		if (add <= 0) return;
		stack.hurtAndBreak(add, user, e -> e.broadcastBreakEvent(slot));
	}

}
