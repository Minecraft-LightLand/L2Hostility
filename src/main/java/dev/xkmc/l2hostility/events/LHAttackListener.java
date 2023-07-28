package dev.xkmc.l2hostility.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobModifierCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LHAttackListener implements AttackListener {

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		LivingEntity mob = cache.getAttacker();
		if (mob != null && MobModifierCap.HOLDER.isProper(mob)) {
			MobModifierCap cap = MobModifierCap.HOLDER.get(mob);
			if (!mob.getType().is(TagGen.NO_SCALING)) {
				cache.addHurtModifier(DamageModifier.multTotal(1 + (float) (cap.getLevel() * LHConfig.COMMON.damageFactor.get())));
			}
			cap.modifiers.forEach((k, v) -> k.onHurtTarget(v, cache.getAttacker(), cache));
		}
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		if (MobModifierCap.HOLDER.isProper(event.getAttacker())) {
			MobModifierCap.HOLDER.get(event.getAttacker()).modifiers
					.forEach((k, v) -> k.onCreateSource(v, event.getAttacker(), event));
		}
	}

}
