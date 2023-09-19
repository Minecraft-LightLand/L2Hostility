package dev.xkmc.l2hostility.events;

import dev.xkmc.l2complements.init.data.DamageTypeGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
		if (mob instanceof Player player) {
			int level = PlayerDifficulty.HOLDER.get(player).getLevel().getLevel();
			double rate = LHConfig.COMMON.prideDamageBonus.get();
			cache.addHurtModifier(DamageModifier.multTotal((float) (1 + level * rate)));
		}
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getAttacker())) {
			MobTraitCap.HOLDER.get(event.getAttacker()).traits
					.forEach((k, v) -> k.onCreateSource(v, event.getAttacker(), event));
		}
	}

}
