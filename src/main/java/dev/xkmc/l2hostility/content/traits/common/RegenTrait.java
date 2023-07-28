package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.entity.LivingEntity;

public class RegenTrait extends MobTrait {

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.tickCount % 20 == 0) {
			mob.heal((float) (LHConfig.COMMON.regen.get() * level));
		}
	}

}
