package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.traits.EffectBooster;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class DrainTrait extends MobTrait {

	public DrainTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void postInit(LivingEntity mob, int lv) {
		var cap = MobTraitCap.HOLDER.get(mob);
		var manager = LHTraits.TRAITS.get().tags();
		if (manager == null) return;
		for (int i = 0; i < 4; i++) {
			var opt = manager.getTag(LHTraits.POTION).getRandomElement(mob.getRandom());
			if (opt.isEmpty()) continue;
			var trait = opt.get();
			if (trait.allow(mob) && !cap.hasTrait(trait)) {
				cap.setTrait(trait, lv);
				return;
			}
		}
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
		super.onHurtTarget(level, attacker, cache, traitCache);
		LivingEntity target = cache.getAttackTarget();
		var neg = target.getActiveEffects().stream()
				.filter(e -> e.getEffect().getCategory() == MobEffectCategory.HARMFUL)
				.count();
		cache.addHurtModifier(DamageModifier
				.multTotal((float) (1 + LHConfig.COMMON.drainDamage.get() * level * neg)));
	}

	@Override
	public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
		var pos = new ArrayList<>(target.getActiveEffects().stream()
				.filter(e -> e.getEffect().getCategory() == MobEffectCategory.BENEFICIAL)
				.toList());
		for (int i = 0; i < level; i++) {
			if (pos.isEmpty()) continue;
			var ins = pos.remove(attacker.getRandom().nextInt(pos.size()));
			target.removeEffect(ins.getEffect());
		}
		double factor = 1 + LHConfig.COMMON.drainDuration.get() * level;
		int maxTime = level * LHConfig.COMMON.drainDurationMax.get();
		EffectBooster.boostTrait(target, factor, maxTime);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
				mapLevel(i -> Component.literal(i + "")
						.withStyle(ChatFormatting.AQUA)),
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.COMMON.drainDamage.get() * 100) + "%")
						.withStyle(ChatFormatting.AQUA)),
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.COMMON.drainDuration.get() * 100) + "%")
						.withStyle(ChatFormatting.AQUA)),
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.COMMON.drainDurationMax.get() / 20f) + "")
						.withStyle(ChatFormatting.AQUA))
		).withStyle(ChatFormatting.GRAY));
	}

}
