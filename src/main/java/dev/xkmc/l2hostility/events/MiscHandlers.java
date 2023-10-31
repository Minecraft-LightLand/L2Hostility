package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;

public class MiscHandlers {

	public static void copyCap(LivingEntity self, LivingEntity sub) {
		if (MobTraitCap.HOLDER.isProper(self) && MobTraitCap.HOLDER.isProper(sub)) {
			var selfCap = MobTraitCap.HOLDER.get(self);
			var subCap = MobTraitCap.HOLDER.get(sub);
			subCap.copyFrom(self, sub, selfCap);
		}
	}

}
