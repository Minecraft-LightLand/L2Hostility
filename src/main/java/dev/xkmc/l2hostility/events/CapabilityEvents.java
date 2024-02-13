package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficultyCap;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {

	@SubscribeEvent
	public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
		event.addCapability(new ResourceLocation(L2Hostility.MODID, "difficulty"),
				new ChunkDifficultyCap(event.getObject()));
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof LivingEntity entity && event.getEntity() instanceof ServerPlayer player) {
			if (MobTraitCap.HOLDER.isProper(entity)) {
				MobTraitCap.HOLDER.get(entity).syncToPlayer(entity, player);
			}
		}
	}

	@SubscribeEvent
	public static void livingTickEvent(LivingEvent.LivingTickEvent event) {
		if (Float.isNaN(event.getEntity().getHealth())) {
			event.getEntity().setHealth(0);
		}
		if (Float.isNaN(event.getEntity().getAbsorptionAmount())) {
			event.getEntity().setAbsorptionAmount(0);
		}
		LivingEntity mob = event.getEntity();
		if (MobTraitCap.HOLDER.isProper(mob) && mob.isAlive()) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			cap.tick(mob);
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		LivingEntity mob = event.getEntity();
		if (mob.level.isClientSide()) return;
		LivingEntity killer = event.getEntity().getKillCredit();
		Player player = null;
		if (killer instanceof Player pl) {
			player = pl;
		} else if (killer instanceof OwnableEntity own && own.getOwner() instanceof Player pl) {
			player = pl;
		}
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (killer != null) {
				cap.onKilled(mob, player);
			}
			if (player != null) {
				PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
				playerDiff.addKillCredit(cap);
				LevelChunk chunk = mob.level.getChunkAt(mob.blockPosition());
				var opt = chunk.getCapability(ChunkDifficulty.CAPABILITY);
				if (opt.resolve().isPresent()) {
					opt.resolve().get().addKillHistory(player, mob, cap);
				}
			}
		}
	}

}
