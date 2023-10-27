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
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
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
				int lv = cap.getLevel();
				double factor;
				if (LHConfig.COMMON.exponentialDamage.get()) {
					factor = Math.pow(1 + LHConfig.COMMON.damageFactor.get(), lv);
				} else {
					factor = 1 + lv * LHConfig.COMMON.damageFactor.get();
				}
				cache.addHurtModifier(DamageModifier.multTotal((float) factor));
			}
			cap.traitEvent((k, v) -> k.onHurtTarget(v, cache.getAttacker(), cache));
		}
		if (mob != null) {
			for (var e : CurseCurioItem.getFromPlayer(mob)) {
				e.item().onHurtTarget(e.stack(), mob, cache);
			}
		}
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		LivingEntity mob = event.getAttacker();
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap.HOLDER.get(mob).traitEvent((k, v) -> k.onCreateSource(v, event.getAttacker(), event));
		}
		if (CurioCompat.hasItem(mob, LHItems.IMAGINE_BREAKER.get())) {
			if (event.getResult() == L2DamageTypes.MOB_ATTACK || event.getResult() == L2DamageTypes.PLAYER_ATTACK) {
				event.enable(DefaultDamageState.BYPASS_MAGIC);
			}
		}
	}

}
