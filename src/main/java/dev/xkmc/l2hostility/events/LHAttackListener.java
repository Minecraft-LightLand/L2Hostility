package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.backport.damage.DamageModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.enchantments.HitTargetEnchantment;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import dev.xkmc.l2library.init.events.attack.AttackListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LHAttackListener implements AttackListener {

	@Override
	public void onAttack(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingAttackEvent();
		assert event != null;
		LivingEntity mob = event.getSource().getEntity() instanceof LivingEntity le ? le : null;
		if (mob == null) return;
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap.HOLDER.get(mob).traitEvent((k, v) -> k.onCreateSource(v, mob, event));
		}
		var type = event.getSource().getMsgId();
		if (type.equals("player") || type.equals("mob")) {
			if (CurioCompat.hasItem(mob, LHItems.IMAGINE_BREAKER.get())) {
				event.getSource().bypassMagic();
			}
		}
	}

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingHurtEvent();
		assert event != null;
		if (event.getSource().getMsgId().equals("soul_flame") || event.getSource().getMsgId().equals("thorns"))
			return;
		LivingEntity mob = cache.getAttacker();
		var target = cache.getAttackTarget();
		if (mob == target)
			return;
		if (MobTraitCap.HOLDER.isProper(target)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(target);
			for (var e : weapon.getAllEnchantments().entrySet()) {
				if (e.getKey() instanceof HitTargetEnchantment ench) {
					ench.hitMob(target, cap, e.getValue(), cache);
				}
			}
		}
		if (mob != null && MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (!mob.getType().is(LHTagGen.NO_SCALING)) {
				int lv = cap.getLevel();
				double factor;
				if (LHConfig.COMMON.exponentialDamage.get()) {
					factor = Math.pow(1 + LHConfig.COMMON.damageFactor.get(), lv);
				} else {
					factor = 1 + lv * LHConfig.COMMON.damageFactor.get();
				}
				DamageModifier.hurtMultTotal(cache, (float) factor);
			}
			TraitEffectCache traitCache = new TraitEffectCache(target);
			cap.traitEvent((k, v) -> k.onHurtTarget(v, mob, cache, traitCache));
		}
		if (mob != null) {
			for (var e : CurseCurioItem.getFromPlayer(mob)) {
				e.item().onHurtTarget(e.stack(), mob, cache);
			}
		}
	}

}
