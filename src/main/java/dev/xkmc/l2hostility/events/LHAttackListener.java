package dev.xkmc.l2hostility.events;

import dev.xkmc.l2core.init.reg.ench.LegacyEnchantment;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.enchantments.HitTargetEnchantment;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.common.Tags;

public class LHAttackListener implements AttackListener {

	private static final ResourceLocation SCALING = L2Hostility.loc("scaling");
	private static final ResourceLocation MASTER_IMMUNE = L2Hostility.loc("master_immune");

	private static boolean masterImmunity(DamageData event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return false;
		MobTraitCap attacker = null, target = null;
		if (event.getAttacker() instanceof Mob mob)
			attacker = LHMiscs.MOB.type().getExisting(mob).orElse(null);
		if (event.getTarget() instanceof Mob mob)
			target = LHMiscs.MOB.type().getExisting(mob).orElse(null);
		LivingEntity attackerMaster = null, targetMaster = null;
		if (attacker != null && attacker.asMinion != null) {
			attackerMaster = attacker.asMinion.master;
			if (event.getTarget() == attackerMaster) {
				return true;
			}
		}
		if (target != null && target.asMinion != null) {
			targetMaster = target.asMinion.master;
			if (event.getAttacker() == targetMaster) {
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
	public boolean onAttack(DamageData.Attack event) {
		if (masterImmunity(event)) return true;
		var source = event.getSource();
		var target = event.getTarget();
		boolean bypassInvul = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
		boolean bypassMagic = source.is(DamageTypeTags.BYPASSES_EFFECTS);
		boolean magic = source.is(Tags.DamageTypes.IS_MAGIC);
		if (magic && !bypassInvul && !bypassMagic) {
			if (CurioCompat.hasItemInCurio(target, LHItems.RING_DIVINITY.get())) {
				return true;
			}
		}
		var opt = LHMiscs.MOB.type().getExisting(target);
		if (opt.isPresent()) {
			for (var e : opt.get().traits.entrySet()) {
				if (e.getKey().onAttackedByOthers(e.getValue(), target, event)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onHurt(DamageData.Offence data) {
		var source = data.getSource();
		if (source.is(L2DamageTypes.NO_SCALE))
			return;
		LivingEntity attacker = data.getAttacker();
		var target = data.getTarget();
		if (attacker == target)
			return;
		var targetOpt = LHMiscs.MOB.type().getExisting(target);
		if (targetOpt.isPresent()) {
			var cap = targetOpt.get();
			for (var e : LegacyEnchantment.findAll(data.getWeapon(), HitTargetEnchantment.class, true)) {
				e.val().hitMob(target, cap, e.lv(), data);
			}
		}

		if (attacker != null) {
			var attOpt = LHMiscs.MOB.type().getExisting(attacker);
			if (attOpt.isPresent()) {
				MobTraitCap cap = attOpt.get();
				if (!attacker.getType().is(LHTagGen.NO_SCALING)) {
					int lv = cap.getLevel();
					double factor;
					if (LHConfig.SERVER.exponentialDamage.get()) {
						factor = Math.pow(1 + LHConfig.SERVER.damageFactor.get(), lv) - 1;
					} else {
						factor = lv * LHConfig.SERVER.damageFactor.get();
					}
					var config = cap.getConfigCache(attacker);
					if (config != null)
						factor *= config.attackScale;
					double old = factor;
					for (var ent : cap.traits.entrySet()) {
						factor *= ent.getKey().modifyBonusDamage(source, old, ent.getValue());
					}
					data.addHurtModifier(DamageModifier.multTotal(1 + (float) factor, SCALING));
				}
				TraitEffectCache traitCache = new TraitEffectCache(target);
				cap.traitEvent((k, v) -> k.onHurtTarget(v, attacker, data, traitCache));
			}
		}
		if (attacker != null) {
			for (var e : CurseCurioItem.getFromPlayer(attacker)) {
				e.item().onHurtTarget(e.stack(), attacker, data);
			}
		}
	}

	@Override
	public void onHurtMaximized(DamageData.OffenceMax data) {
		var target = data.getTarget();
		LHMiscs.MOB.type().getExisting(target)
				.ifPresent(cap -> cap.traitEvent((k, v) -> k.onHurtByMax(v, target, data)));

	}

	@Override
	public void onDamage(DamageData.Defence data) {
		var mob = data.getTarget();
		var opt = LHMiscs.MOB.type().getExisting(mob);
		if (opt.isPresent()) {
			MobTraitCap cap = opt.get();
			cap.traitEvent((k, v) -> k.onDamaged(v, mob, data));
		}

		var attacker = data.getAttacker();
		if (attacker == null) return;
		TraitEffectCache traitCache = new TraitEffectCache(mob);
		LHMiscs.MOB.type().getExisting(attacker)
				.ifPresent(cap -> cap.traitEvent((k, v) -> k.onHurtTargetMax(v, attacker, data, traitCache)));

		for (var e : CurseCurioItem.getFromPlayer(mob)) {
			e.item().onDamage(e.stack(), mob, data);
		}
		if (masterImmunity(data)) {
			data.addDealtModifier(DamageModifier.nonlinearFinal(10432, e -> 0, MASTER_IMMUNE));
		}
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		LivingEntity mob = event.getAttacker();
		var opt = LHMiscs.MOB.type().getExisting(mob);
		opt.ifPresent(cap -> cap.traitEvent((k, v) -> k.onCreateSource(v, event.getAttacker(), event)));
		var type = event.getResult();
		if (type == null) return;
		var root = type.toRoot();
		if (root == L2DamageTypes.MOB_ATTACK || root == L2DamageTypes.PLAYER_ATTACK) {
			if (CurioCompat.hasItemInCurioOrSlot(mob, LHItems.IMAGINE_BREAKER.get())) {
				event.enable(DefaultDamageState.BYPASS_MAGIC);
			}
			if (CurioCompat.hasItemInCurio(mob, LHItems.PLATINUM_STAR.get())) {
				event.enable(DefaultDamageState.BYPASS_COOLDOWN);
			}
		}

	}

}
