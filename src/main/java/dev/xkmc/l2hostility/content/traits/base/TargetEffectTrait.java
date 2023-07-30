package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class TargetEffectTrait extends MobTrait {

	private final Function<Integer, MobEffectInstance> func;

	public TargetEffectTrait(Function<Integer, MobEffectInstance> func) {
		super(() -> func.apply(1).getEffect().getColor());
		this.func = func;
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache) {
		EffectUtil.addEffect(cache.getAttackTarget(), func.apply(level), EffectUtil.AddReason.NONE, attacker);
	}

}
