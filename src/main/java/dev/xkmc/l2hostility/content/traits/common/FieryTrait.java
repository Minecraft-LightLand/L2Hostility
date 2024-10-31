package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FieryTrait extends SelfEffectTrait {

	public FieryTrait() {
		super(MobEffects.FIRE_RESISTANCE);
	}

	@Override
	public void onHurtTargetMax(int level, LivingEntity attacker, DamageData.OffenceMax cache, TraitEffectCache traitCache) {
		if (cache.getDamageOriginal() > 0) {
			if (cache.getSource().getDirectEntity() instanceof LivingEntity le) {
				le.setRemainingFireTicks(LHConfig.SERVER.fieryTime.get() * 20);
			}
		}
	}

	@Override
	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		if (event.getSource().getDirectEntity() instanceof LivingEntity le)
			le.setRemainingFireTicks(LHConfig.SERVER.fieryTime.get() * 20);
		return event.getSource().is(DamageTypeTags.IS_FIRE);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.SERVER.fieryTime.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
		super.addDetail(list);
	}

}
