package dev.xkmc.l2hostility.content.modifiers.common;

import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.entity.LivingEntity;

public class RegenModifier extends MobModifier {

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.tickCount % 20 == 0) {
			mob.heal((float) (LHConfig.COMMON.regen.get() * level));
		}
	}

}
