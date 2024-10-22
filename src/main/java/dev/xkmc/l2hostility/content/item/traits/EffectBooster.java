package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Predicate;

public class EffectBooster {

	public static void boostCharge(LivingEntity target) {
		double factor = 1 + LHConfig.SERVER.drainDuration.get();
		int maxTime = LHConfig.SERVER.drainDurationMax.get();
		int min = LHConfig.SERVER.witchChargeMinDuration.get();
		boost(target, e -> e.getCategory() == MobEffectCategory.HARMFUL, min, factor, maxTime);
	}

	public static void boostBottle(LivingEntity target) {
		double factor = 1 + LHConfig.SERVER.drainDuration.get();
		int maxTime = LHConfig.SERVER.drainDurationMax.get();
		int min = LHConfig.SERVER.witchChargeMinDuration.get();
		boost(target, e -> true, min, factor, maxTime);
	}

	public static void boostTrait(LivingEntity target, double factor, int maxTime) {
		boost(target, e -> e.getCategory() == MobEffectCategory.HARMFUL, 0, factor, maxTime);
	}

	private static void boost(LivingEntity target, Predicate<MobEffect> pred, int min, double factor, int maxTime) {
		var list = new ArrayList<>(target.getActiveEffects());
		for (var e : list) {
			if (pred.test(e.getEffect().value())) {
				int current = e.getDuration();
				if (current < min) continue;
				int max = Math.min(maxTime, (int) (current * factor));
				if (max > current) {
					new EffectBuilder(e).setDuration(max);
				}
				target.forceAddEffect(e, null);
			}
		}
	}

	public static void boostInfinite(LivingEntity target) {
		int min = LHConfig.SERVER.witchChargeMinDuration.get();
		var list = new ArrayList<>(target.getActiveEffects());
		for (var e : list) {
			if (e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
				int current = e.getDuration();
				if (current < min) continue;
				new EffectBuilder(e).setDuration(MobEffectInstance.INFINITE_DURATION);
				target.forceAddEffect(e, null);
			}
		}
	}

}
