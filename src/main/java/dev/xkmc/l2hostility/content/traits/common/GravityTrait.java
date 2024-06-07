package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class GravityTrait extends AuraEffectTrait {

	public GravityTrait(Supplier<MobEffect> eff) {
		super(eff);
	}

	@Override
	public void onDamaged(int level, LivingEntity mob, AttackCache cache) {
		var e = cache.getAttacker();
		if (e != null && !e.onGround()) {
			if (CurioCompat.hasItemInCurio(e, LHItems.ABRAHADABRA.get())) return;
			e.push(0, -level, 0);
			if (e instanceof ServerPlayer) {
				e.hurtMarked = true;
			}
		}
	}

}
