package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ReflectTrait extends MobTrait {

	@Override
	public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
		if (event.getSource().getDirectEntity() instanceof LivingEntity le && !event.getSource().is(L2DamageTypes.MAGIC)) {
			// TODO unstable
			float factor = (float) (1 + level * LHConfig.COMMON.reflect.get());
			le.hurt(entity.level().damageSources().indirectMagic(entity, null), event.getAmount() * factor);
		}
	}

}
