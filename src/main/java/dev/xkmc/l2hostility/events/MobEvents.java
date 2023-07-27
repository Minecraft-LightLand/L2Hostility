package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.mob.MobModifierCap;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobEvents {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onMobHurt(LivingHurtEvent event) {
		if (MobModifierCap.HOLDER.isProper(event.getEntity())) {
			for (var e : MobModifierCap.HOLDER.get(event.getEntity()).modifiers) {
				e.modifier().onHurtByOthers(e.level(), event.getEntity(), event);
			}
		}
	}

}
