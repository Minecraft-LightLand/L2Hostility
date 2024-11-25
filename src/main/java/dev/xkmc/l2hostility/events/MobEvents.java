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
			if (CurioCompat.hasItemInCurio(event.getEntity(), LHItems.RING_DIVINITY.get())) {
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
			if (credit != null && CurioCompat.hasItemInCurio(credit, LHItems.CURSE_LUST.get())) {
				for (var e : EquipmentSlot.values())
					mob.setDropChance(e, 1);
			}
		}
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			MobTraitCap.HOLDER.get(event.getEntity()).traitEvent((k, v) -> k.onDeath(v, event.getEntity(), event));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onMobDrop(LivingDropsEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			var cap = MobTraitCap.HOLDER.get(event.getEntity());
			if (cap.noDrop) {
				event.setCanceled(true);
				return;
			}
			LivingEntity killer = event.getEntity().getKillCredit();
			if (killer != null && CurioCompat.hasItemInCurio(killer, LHItems.NIDHOGGUR.get())) {
				double val = LHConfig.COMMON.nidhoggurDropFactor.get() * cap.getLevel();
				int count = (int) val;
				if (event.getEntity().getRandom().nextDouble() < val - count) count++;
				count++;
				for (var stack : event.getDrops()) {
					int ans = stack.getItem().getCount() * count;
					if (LHConfig.COMMON.nidhoggurCapAtItemMaxStack.get()){
						ans = Math.min(stack.getItem().getMaxStackSize(), ans);
					}
					stack.getItem().setCount(ans);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onExpDrop(LivingExperienceDropEvent event) {
		if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
			var cap = MobTraitCap.HOLDER.get(event.getEntity());
			if (cap.noDrop) {
				event.setCanceled(true);
				return;
			}
			int exp = event.getDroppedExperience();
			int level = cap.getLevel();
			exp = (int) (exp * (1 + LHConfig.COMMON.expDropFactor.get() * level));
			event.setDroppedExperience(exp);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPotionTest(MobEffectEvent.Applicable event) {
		LivingEntity entity = event.getEntity();
		if (CurioCompat.hasItemInCurio(entity, LHItems.CURSE_WRATH.get())) {
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

}
