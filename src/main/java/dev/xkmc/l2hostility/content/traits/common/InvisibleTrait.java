package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InvisibleTrait extends SelfEffectTrait {

	public InvisibleTrait() {
		super(() -> MobEffects.INVISIBILITY);
	}

	@Override
	public void postInit(LivingEntity mob, int lv) {
		for (var e : EquipmentSlot.values()) {
			ItemStack stack = mob.getItemBySlot(e);
			if (!stack.isEmpty()) {
				stack.enchant(LCEnchantments.SHULKER_ARMOR.get(), 1);
			}
		}
	}

}
