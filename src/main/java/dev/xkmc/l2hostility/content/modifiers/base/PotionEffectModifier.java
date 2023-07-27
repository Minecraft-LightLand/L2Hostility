package dev.xkmc.l2hostility.content.modifiers.base;

import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class PotionEffectModifier extends MobModifier {

	public final Supplier<MobEffect> effect;

	public PotionEffectModifier(Supplier<MobEffect> effect) {
		this.effect = effect;
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		EffectUtil.refreshEffect(mob, new MobEffectInstance(effect.get(), 40, level - 1), EffectUtil.AddReason.SELF, mob);
	}

}
