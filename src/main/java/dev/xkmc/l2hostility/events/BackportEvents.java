package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.backport.explosion.BaseExplosion;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BackportEvents {

	@SubscribeEvent
	public static void onDetonate(ExplosionEvent.Detonate event) {
		if (event.getExplosion() instanceof BaseExplosion exp) {
			event.getAffectedEntities().removeIf(e -> !exp.hurtEntity(e));
		}
	}

}
