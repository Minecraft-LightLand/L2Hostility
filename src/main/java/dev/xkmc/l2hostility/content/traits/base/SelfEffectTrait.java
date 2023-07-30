package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class SelfEffectTrait extends MobTrait {

	public final Supplier<MobEffect> effect;

	public SelfEffectTrait(Supplier<MobEffect> effect) {
		super(() -> effect.get().getColor());
		this.effect = effect;
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		EffectUtil.refreshEffect(mob, new MobEffectInstance(effect.get(), 40, level - 1), EffectUtil.AddReason.FORCE, null);
	}

}
