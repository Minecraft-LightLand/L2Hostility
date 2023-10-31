package dev.xkmc.l2hostility.events;

import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.mixin.ForgeInternalHandlerAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobEvents {

	@SubscribeEvent
	public static void onMobAttack(LivingAttackEvent event) {
		boolean bypassInvul = event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY);
		boolean bypassMagic = event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS);
		boolean magic = event.getSource().is(L2DamageTypes.MAGIC);
		if (magic && !bypassInvul && !bypassMagic) {
			if (CurioCompat.hasItem(event.getEntity(), LHItems.RING_DIVINITY.get())) {
				event.setCanceled(true);
				return;
			}
		}
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traitEvent((k, v) -> k.onAttackedByOthers(v, event.getEntity(), event));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onMobHurt(LivingHurtEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traitEvent((k, v) -> k.onHurtByOthers(v, event.getEntity(), event));
		} else if (event.getEntity() instanceof Player player &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				CurioCompat.hasItem(player, LHItems.CURSE_PRIDE.get())) {
			int level = PlayerDifficulty.HOLDER.get(player).getLevel().getLevel();
			double rate = LHConfig.COMMON.prideHealthBonus.get();
			double factor = 1 + rate * level;
			event.setAmount((float) (event.getAmount() / factor));
		}
	}

	@SubscribeEvent
	public static void onDamage(LivingDamageEvent event) {
		for (var e : CurioCompat.getItems(event.getEntity(), e -> e.getItem() instanceof CurseCurioItem)) {
			if (e.getItem() instanceof CurseCurioItem curse) {
				curse.onDamage(e, event.getEntity(), event);
			}
		}
	}

	@SubscribeEvent
	public static void onMobDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof Mob mob) {
			var credit = mob.getKillCredit();
			if (credit != null && CurioCompat.hasItem(credit, LHItems.CURSE_LUST.get())) {
				for (var e : EquipmentSlot.values())
					mob.setDropChance(e, 1);
			}
		}
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traitEvent((k, v) -> k.onDeath(v, event.getEntity(), event));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMobDrop(LivingDropsEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			var cap = MobTraitCap.HOLDER.get(event.getEntity());
			if (cap.noDrop) {
				event.setCanceled(true);
				return;
			}
			// TODO drop multiply
		}
	}

	@SubscribeEvent()
	public static void onExpDrop(LivingExperienceDropEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			var cap = MobTraitCap.HOLDER.get(event.getEntity());
			if (cap.noDrop) {
				event.setCanceled(true);
				return;
			}
			int exp = event.getDroppedExperience();
			int level = cap.getLevel();
			exp *= 1 + level * LHConfig.COMMON.expDropFactor.get() * level;
			event.setDroppedExperience(exp);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPotionTest(MobEffectEvent.Applicable event) {
		LivingEntity entity = event.getEntity();
		if (CurioCompat.hasItem(entity, LHItems.CURSE_WRATH.get())) {
			var config = ArmorEffectConfig.get().getImmunity(LHItems.CURSE_WRATH.getId().toString());
			if (config.contains(event.getEffectInstance().getEffect())) {
				event.setResult(Event.Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		List<TraitLootModifier> list = new ArrayList<>();
		for (var e : ForgeInternalHandlerAccessor.callGetLootModifierManager().getAllLootMods()) {
			if (e instanceof TraitLootModifier loot) {
				list.add(loot);
			}
		}
		LootDataToClient packet = new LootDataToClient(list);
		if (event.getPlayer() == null) {
			L2Hostility.HANDLER.toAllClient(packet);
		} else {
			L2Hostility.HANDLER.toClientPlayer(packet, event.getPlayer());
		}
	}

	private static final List<Runnable> TASKS = new ArrayList<>();

	public static synchronized void schedule(Runnable runnable) {
		TASKS.add(runnable);
	}

	@SubscribeEvent
	public static void onTick(TickEvent.ServerTickEvent event) {
		if (TASKS.isEmpty()) return;
		var temp = new ArrayList<>(TASKS);
		TASKS.clear();
		for (var e : temp) {
			e.run();
		}
	}

}
