package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Supplier;

public class SelfEffectTrait extends MobTrait {

	public final Supplier<MobEffect> effect;

	public SelfEffectTrait(Supplier<MobEffect> effect) {
		super(() -> effect.get().getColor());
		this.effect = effect;
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.level().isClientSide()) return;
		EffectUtil.refreshEffect(mob, new MobEffectInstance(effect.get(), 40, level - 1), EffectUtil.AddReason.FORCE, mob);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(LangData.TOOLTIP_SELF_EFFECT.get());
		ChatFormatting color = effect.get().getCategory().getTooltipFormatting();
		if (getMaxLevel() == 1) {
			list.add(effect.get().getDisplayName().copy().withStyle(color));
		} else list.add(mapLevel(e ->
				Component.translatable("potion.withAmplifier", effect.get().getDisplayName(),
								Component.translatable("potion.potency." + (e - 1)))
						.withStyle(color))
		);
	}

}
