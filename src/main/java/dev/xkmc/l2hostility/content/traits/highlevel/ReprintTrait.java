package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.traits.ReprintHandler;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class ReprintTrait extends MobTrait {

	private static float limit(float v, int exp) {
		if (Float.isNaN(v)) v = 1;
		double ans = v * Math.pow(2d, Math.min(127, exp));
		return (float) Math.min(Float.MAX_VALUE / 2f, ans);
	}

	public ReprintTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
		long total = 0;
		int maxLv = 0;
		var event = cache.getLivingHurtEvent();
		for (var slot : EquipmentSlot.values()) {
			ItemStack dst = attacker.getItemBySlot(slot);
			ItemStack src = cache.getAttackTarget().getItemBySlot(slot);
			var targetEnch = src.getAllEnchantments();
			for (var e : targetEnch.entrySet()) {
				int lv = e.getValue();
				maxLv = Math.max(maxLv, lv);
				if (lv > 30) {
					total = -1;
				} else if (total >= 0) {
					total += 1L << lv;
				}
			}

			if (event != null && event.getSource().getDirectEntity() == attacker)
				ReprintHandler.reprint(dst, src);
		}
		int bypass = LHConfig.COMMON.reprintBypass.get();
		if (maxLv >= bypass) {
			ItemStack weapon = attacker.getItemBySlot(EquipmentSlot.MAINHAND);
			if (!weapon.isEmpty() && (weapon.isEnchanted() || weapon.isEnchantable())) {
				if (weapon.canApplyAtEnchantingTable(LCEnchantments.VOID_TOUCH.get())) {
					var map = weapon.getAllEnchantments();
					map.compute(LCEnchantments.VOID_TOUCH.get(), (k, v) -> v == null ? 20 : Math.max(v, 20));
					map.compute(Enchantments.VANISHING_CURSE, (k, v) -> v == null ? 1 : Math.max(v, 1));
					EnchantmentHelper.setEnchantments(map, weapon);
				}
			}
		}
		if (total >= 0)
			cache.addHurtModifier(DamageModifier.multTotal(1 + (float) (LHConfig.COMMON.reprintDamage.get() * total)));
		else {
			int finalMaxLv = maxLv;
			cache.addHurtModifier(DamageModifier.nonlinearFinal(0, v -> limit(v, finalMaxLv)));
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.reprintDamage.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
