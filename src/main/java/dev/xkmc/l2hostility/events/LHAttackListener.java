package dev.xkmc.l2hostility.events;

import dev.xkmc.l2complements.init.data.DamageTypeGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.FlamingThorn;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LHAttackListener implements AttackListener {

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingHurtEvent();
		assert event != null;
		if (event.getSource().is(DamageTypeGen.SOUL_FLAME))
			return;
		LivingEntity mob = cache.getAttacker();
		if (mob != null && MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (!mob.getType().is(TagGen.NO_SCALING)) {
				cache.addHurtModifier(DamageModifier.multTotal(1 + (float) (cap.getLevel() * LHConfig.COMMON.damageFactor.get())));
			}
			cap.traits.forEach((k, v) -> k.onHurtTarget(v, cache.getAttacker(), cache));
		}
		if (mob != null && CurioCompat.hasItem(mob, LHItems.CURSE_PRIDE.get())) {
			int level = DifficultyLevel.ofAny(mob);
			double rate = LHConfig.COMMON.prideDamageBonus.get();
			cache.addHurtModifier(DamageModifier.multTotal((float) (1 + level * rate)));
		}
		if (mob != null && CurioCompat.hasItem(mob, LHItems.CURSE_WRATH.get())) {
			int level = DifficultyLevel.ofAny(cache.getAttackTarget()) - DifficultyLevel.ofAny(mob);
			if (level > 0) {
				double rate = LHConfig.COMMON.wrathDamageBonus.get();
				cache.addHurtModifier(DamageModifier.multTotal((float) (1 + level * rate)));
			}
		}
		if (mob != null && CurioCompat.hasItem(mob, LHItems.FLAMING_THORN.get())) {
			FlamingThorn.process(mob, cache.getAttackTarget());
		}
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		LivingEntity mob = event.getAttacker();
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap.HOLDER.get(mob).traits
					.forEach((k, v) -> k.onCreateSource(v, event.getAttacker(), event));
		}
		if (CurioCompat.hasItem(mob, LHItems.IMAGINE_BREAKER.get())) {
			if (event.getResult() == L2DamageTypes.MOB_ATTACK || event.getResult() == L2DamageTypes.PLAYER_ATTACK) {
				event.enable(DefaultDamageState.BYPASS_MAGIC);
			}
		}
	}

}
