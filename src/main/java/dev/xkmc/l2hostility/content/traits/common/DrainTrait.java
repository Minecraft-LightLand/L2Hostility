package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.base.effects.EffectBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class DrainTrait extends MobTrait {

	public DrainTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache) {
		super.onHurtTarget(level, attacker, cache);
		LivingEntity target = cache.getAttackTarget();
		var neg = target.getActiveEffects().stream()
				.filter(e -> e.getEffect().getCategory() == MobEffectCategory.HARMFUL)
				.count();
		cache.addHurtModifier(DamageModifier
				.multTotal((float) (1 + LHConfig.COMMON.drainDamage.get() * level * neg)));
	}

	@Override
	public void postHurt(int level, LivingEntity attacker, LivingEntity target) {
		var pos = target.getActiveEffects().stream()
				.filter(e -> e.getEffect().getCategory() == MobEffectCategory.BENEFICIAL)
				.toList();
		for (int i = 0; i < level; i++) {
			if (pos.isEmpty()) continue;
			var ins = pos.remove(attacker.getRandom().nextInt(pos.size()));
			target.removeEffect(ins.getEffect());
		}
		for (var e : target.getActiveEffects()) {
			if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
				int bonus = (int) (e.getDuration() * LHConfig.COMMON.drainDuration.get());
				bonus = Math.min(LHConfig.COMMON.drainDurationMax.get(), bonus);
				new EffectBuilder(e).setDuration(e.getDuration() + bonus);
			}
		}
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
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.COMMON.drainDurationMax.get() / 20f) + "%")
						.withStyle(ChatFormatting.AQUA))
		).withStyle(ChatFormatting.GRAY));
	}

}
