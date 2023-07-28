package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;

public class RegenTrait extends MobTrait {

	public RegenTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.tickCount % 20 == 0) {
			mob.heal((float) (mob.getMaxHealth() * LHConfig.COMMON.regen.get() * level));
		}
	}

}
