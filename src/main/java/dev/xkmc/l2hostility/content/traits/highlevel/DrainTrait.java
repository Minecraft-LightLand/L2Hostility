package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.traits.EffectBooster;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
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
		var cap = LHMiscs.MOB.type().getOrCreate(mob);
		var potions = LHTraits.TRAITS.reg().getTag(LHTraits.POTION);
		if (potions.isEmpty() || potions.get().size() == 0) return;
		for (int i = 0; i < 4; i++) {
			var opt = potions.get().getRandomElement(mob.getRandom());
			if (opt.isEmpty()) continue;
			var trait = opt.get().value();
			if (trait.allow(mob) && !cap.hasTrait(trait)) {
				cap.setTrait(trait, lv);
				return;
			}
		}
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, DamageData.Offence cache, TraitEffectCache traitCache) {
		LivingEntity target = cache.getTarget();
		var neg = target.getActiveEffects().stream()
				.filter(e -> e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
				.count();
		cache.addHurtModifier(DamageModifier
				.multTotal((float) (1 + LHConfig.SERVER.drainDamage.get() * level * neg), getRegistryName()));
	}

	@Override
	public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
		var pos = new ArrayList<>(target.getActiveEffects().stream()
				.filter(e -> e.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL)
				.toList());
		for (int i = 0; i < level; i++) {
			if (pos.isEmpty()) continue;
			var ins = pos.remove(attacker.getRandom().nextInt(pos.size()));
			target.removeEffect(ins.getEffect());
		}
		double factor = 1 + LHConfig.SERVER.drainDuration.get() * level;
		int maxTime = level * LHConfig.SERVER.drainDurationMax.get();
		EffectBooster.boostTrait(target, factor, maxTime);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
				mapLevel(i -> Component.literal(i + "")
						.withStyle(ChatFormatting.AQUA)),
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.SERVER.drainDamage.get() * 100) + "%")
						.withStyle(ChatFormatting.AQUA)),
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.SERVER.drainDuration.get() * 100) + "%")
						.withStyle(ChatFormatting.AQUA)),
				mapLevel(i -> Component.literal(Math.round(i * LHConfig.SERVER.drainDurationMax.get() / 20f) + "")
						.withStyle(ChatFormatting.AQUA))
		).withStyle(ChatFormatting.GRAY));
	}

}
