package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
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

	@Override
	public void addDetail(List<Component> list) {
		list.add(LangData.TOOLTIP_TARGET_EFFECT.get());
		list.add(mapLevel(e -> {
			MobEffectInstance ins = func.apply(e);
			MutableComponent ans = Component.translatable(ins.getDescriptionId());
			MobEffect mobeffect = ins.getEffect();
			if (ins.getAmplifier() > 0) {
				ans = Component.translatable("potion.withAmplifier", ans,
						Component.translatable("potion.potency." + ins.getAmplifier()));
			}
			if (!ins.endsWithin(20)) {
				ans = Component.translatable("potion.withDuration", ans,
						MobEffectUtil.formatDuration(ins, 1));
			}
			return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
		}));
	}

}
