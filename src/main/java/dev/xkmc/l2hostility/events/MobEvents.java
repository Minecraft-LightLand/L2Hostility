package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobEvents {

	@SubscribeEvent
	public static void onMobAttack(LivingAttackEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traits
					.forEach((k, v) -> k.onAttackedByOthers(v, event.getEntity(), event));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onMobHurt(LivingHurtEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traits
					.forEach((k, v) -> k.onHurtByOthers(v, event.getEntity(), event));
		}
	}

	@SubscribeEvent
	public static void onMobDeath(LivingDeathEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traits
					.forEach((k, v) -> k.onDeath(v, event.getEntity(), event));
		}
	}

	private static final List<Runnable> TASKS = new ArrayList<>();

	public static void schedule(Runnable runnable) {
		TASKS.add(runnable);
	}

	@SubscribeEvent
	public static void onTick(TickEvent.ServerTickEvent event) {
		for (var e : TASKS) {
			e.run();
		}
		TASKS.clear();
	}


}
