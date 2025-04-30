package dev.xkmc.l2hostility.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.enchantments.HitTargetEnchantment;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.data.HostilityDamageState;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class LHAttackListener implements AttackListener {

	private static boolean masterImmunity(AttackCache cache) {
		var event = cache.getLivingAttackEvent();
		if (event != null && event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return false;
		MobTraitCap attacker = null, target = null;
		if (cache.getAttacker() instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob))
			attacker = MobTraitCap.HOLDER.get(mob);
		if (cache.getAttackTarget() instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob))
			target = MobTraitCap.HOLDER.get(mob);
		LivingEntity attackerMaster = null, targetMaster = null;
		if (attacker != null && attacker.asMinion != null) {
			attackerMaster = attacker.asMinion.master;
			if (cache.getAttackTarget() == attackerMaster) {
				return true;
			}
		}
		if (target != null && target.asMinion != null) {
			targetMaster = target.asMinion.master;
			if (cache.getAttacker() == targetMaster) {
				return true;
			}
		}
		if (attackerMaster != null && attackerMaster == targetMaster)
			return true;
		if (target != null && target.isMasterProtected())
			return true;
		return false;
	}

	@Override
	public void onAttack(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingAttackEvent();
		assert event != null;
		if (masterImmunity(cache)) {
			event.setCanceled(true);
		}
	}

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingHurtEvent();
		assert event != null;
		var source = event.getSource();
		if (source.is(L2DamageTypes.NO_SCALE))
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
					factor = Math.pow(1 + LHConfig.COMMON.damageFactor.get(), lv) - 1;
				} else {
					factor = lv * LHConfig.COMMON.damageFactor.get();
				}
				var config = cap.getConfigCache(mob);
				if (config != null)
					factor *= config.attackScale;
				double old = factor;
				for (var ent : cap.traits.entrySet()) {
					factor *= ent.getKey().modifyBonusDamage(source, old, ent.getValue());
				}
				cache.addHurtModifier(DamageModifier.multTotal(1 + (float) factor));
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

	@Override
	public void onDamage(AttackCache cache, ItemStack weapon) {
		var mob = cache.getAttackTarget();
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			cap.traitEvent((k, v) -> k.onDamaged(v, mob, cache));
		}
		if (masterImmunity(cache)) {
			cache.addDealtModifier(DamageModifier.nonlinearFinal(10432, e -> 0));
		}
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		LivingEntity mob = event.getAttacker();
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap.HOLDER.get(mob).traitEvent((k, v) -> k.onCreateSource(v, event.getAttacker(), event));
		}
		var type = event.getResult();
		if (type == null) return;
		var root = type.toRoot();
		if (root == L2DamageTypes.MOB_ATTACK || root == L2DamageTypes.PLAYER_ATTACK) {
			if (CurioCompat.hasItemInCurioOrSlot(mob, LHItems.IMAGINE_BREAKER.get())) {
				event.enable(DefaultDamageState.BYPASS_MAGIC);
			}
			if (CurioCompat.hasItemInCurio(mob, LHItems.PLATINUM_STAR.get())) {
				event.enable(HostilityDamageState.BYPASS_COOLDOWN);
			}
		}

	}

}
