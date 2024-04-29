package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkCapSyncToClient;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficultyCap;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.mob.PerformanceConstants;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedHashSet;
import java.util.Set;

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

	private static void initMob(LivingEntity mob, MobSpawnType type) {
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (!mob.level().isClientSide() && !cap.isInitialized()) {
				var opt = ChunkDifficulty.at(mob.level(), mob.blockPosition());
				if (opt.isPresent()) {
					cap.init(mob.level(), mob, opt.get());
					if (type == MobSpawnType.SPAWNER) {
						cap.dropRate = LHConfig.COMMON.dropRateFromSpawner.get();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
		LivingEntity mob = event.getEntity();
		initMob(mob, event.getSpawnType());
	}


	@SubscribeEvent
	public static void livingTickEvent(LivingEvent.LivingTickEvent event) {
		LivingEntity mob = event.getEntity();
		if (mob.tickCount % PerformanceConstants.NAN_FIX == 0) {
			if (Float.isNaN(mob.getHealth())) {
				mob.setHealth(0);
			}
			if (Float.isNaN(mob.getAbsorptionAmount())) {
				mob.setAbsorptionAmount(0);
			}
		}
		if (mob.isAlive()) {
			mob.getCapability(MobTraitCap.CAPABILITY).ifPresent(e -> e.tick(mob));
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		LivingEntity mob = event.getEntity();
		if (mob.level().isClientSide()) return;
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
				LevelChunk chunk = mob.level().getChunkAt(mob.blockPosition());
				var opt = chunk.getCapability(ChunkDifficulty.CAPABILITY);
				if (opt.resolve().isPresent()) {
					opt.resolve().get().addKillHistory(player, mob, cap);
				}
			}
		}
	}

	private static final Set<ChunkDifficulty> PENDING = new LinkedHashSet<>();

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		for (var e : PENDING) {
			L2Hostility.toTrackingChunk(e.chunk, new ChunkCapSyncToClient(e));
		}
		PENDING.clear();
	}

	@SubscribeEvent
	public static void onStartTrackingChunk(ChunkWatchEvent.Watch event) {
		var opt = event.getChunk().getCapability(ChunkDifficulty.CAPABILITY).resolve();
		if (opt.isEmpty()) return;
		L2Hostility.HANDLER.toClientPlayer(new ChunkCapSyncToClient(opt.get()), event.getPlayer());
	}

	public static void markDirty(ChunkDifficulty chunk) {
		PENDING.add(chunk);
	}

}
