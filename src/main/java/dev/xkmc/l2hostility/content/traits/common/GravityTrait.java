package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public class GravityTrait extends AuraEffectTrait {

	public GravityTrait(Holder<MobEffect> eff) {
		super(eff);
	}

	@Override
	public void onHurtByMax(int level, LivingEntity mob, DamageData.OffenceMax cache) {
		var e = cache.getAttacker();
		if (e != null && !e.onGround()) {
			if (LHItems.ABRAHADABRA.get().isOn(e)) return;
			e.push(0, -level, 0);
			if (e instanceof ServerPlayer) {
				e.hurtMarked = true;
			}
		}
	}

}
