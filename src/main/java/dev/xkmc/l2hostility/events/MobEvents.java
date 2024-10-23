package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfWrath;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2hostility.mixin.NeoForgeEventHandlerAccessor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = L2Hostility.MODID, bus = EventBusSubscriber.Bus.GAME)
public class MobEvents {

	@SubscribeEvent
	public static void onMobDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof Mob mob) {
			var credit = mob.getKillCredit();
			if (credit != null && CurioCompat.hasItemInCurio(credit, LHItems.CURSE_LUST.get())) {
				for (var e : EquipmentSlot.values())
					mob.setDropChance(e, 1);
			}
		}
		var opt = LHMiscs.MOB.type().getExisting(event.getEntity());
		opt.ifPresent(cap -> cap.traitEvent((k, v) -> k.onDeath(v, event.getEntity(), event)));
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onMobDrop(LivingDropsEvent event) {
		var mob = event.getEntity();
		var opt = LHMiscs.MOB.type().getExisting(mob);
		if (opt.isEmpty()) return;
		var cap = opt.get();
		if (cap.noDrop) {
			event.setCanceled(true);
			return;
		}
		LivingEntity killer = event.getEntity().getKillCredit();
		if (killer != null && CurioCompat.hasItemInCurio(killer, LHItems.NIDHOGGUR.get())) {
			double val = LHConfig.SERVER.nidhoggurDropFactor.get() * cap.getLevel();
			int count = (int) val;
			if (event.getEntity().getRandom().nextDouble() < val - count) count++;
			count++;
			for (var stack : event.getDrops()) {
				int ans = stack.getItem().getCount() * count;
				if (LHConfig.SERVER.nidhoggurCapAtItemMaxStack.get()) {
					ans = Math.min(stack.getItem().getMaxStackSize(), ans);
				}
				stack.getItem().setCount(ans);
			}

		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onExpDrop(LivingExperienceDropEvent event) {
		var mob = event.getEntity();
		var opt = LHMiscs.MOB.type().getExisting(mob);
		if (opt.isEmpty()) return;
		var cap = opt.get();
		if (cap.noDrop) {
			event.setCanceled(true);
			return;
		}
		int exp = event.getDroppedExperience();
		int level = cap.getLevel();
		exp = (int) (exp * (1 + LHConfig.SERVER.expDropFactor.get() * level));
		event.setDroppedExperience(exp);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPotionTest(MobEffectEvent.Applicable event) {
		var ins = event.getEffectInstance();
		if (ins == null) return;
		LivingEntity entity = event.getEntity();
		if (CurioCompat.hasItemInCurio(entity, LHItems.CURSE_WRATH.get())) {
			if (CurseOfWrath.SET.contains(ins.getEffect())) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		List<TraitLootModifier> list = new ArrayList<>();
		for (var e : NeoForgeEventHandlerAccessor.callGetLootModifierManager().getAllLootMods()) {
			if (e instanceof TraitLootModifier loot) {
				list.add(loot);
			}
		}
		LootDataToClient packet = LootDataToClient.of(list);
		if (event.getPlayer() == null) {
			L2Hostility.HANDLER.toAllClient(packet);
		} else {
			L2Hostility.HANDLER.toClientPlayer(packet, event.getPlayer());
		}
	}

}
