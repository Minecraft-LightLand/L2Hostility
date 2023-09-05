package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

import java.util.List;

public class RegenTrait extends MobTrait {

	public RegenTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.level().isClientSide()) return;
		if (!validTarget(mob)) return;

		if (mob.tickCount % 20 == 0) {
			mob.heal((float) (mob.getMaxHealth() * LHConfig.COMMON.regen.get() * level));
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal((int) Math.round(LHConfig.COMMON.regen.get() * 100 * i) + "")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		return validTarget(le) && super.allow(le, difficulty, maxModLv);
	}

	public boolean validTarget(LivingEntity le) {
		if (le instanceof EnderDragon) {
			return false;
		}
		return le.canBeAffected(new MobEffectInstance(LCEffects.CURSE.get(), 100));
	}

}
