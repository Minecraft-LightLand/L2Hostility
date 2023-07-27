package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficultyCap;
import dev.xkmc.l2hostility.content.capability.mob.MobModifierCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
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
			if (MobModifierCap.HOLDER.isProper(entity)) {
				MobModifierCap.HOLDER.get(entity).syncToPlayer(entity, player);
			}
		}
	}

	private static void initMob(LivingEntity mob) {
		if (MobModifierCap.HOLDER.isProper(mob)) {
			MobModifierCap cap = MobModifierCap.HOLDER.get(mob);
			if (!mob.level().isClientSide() && !cap.isInitialized()) {
				BlockPos pos = mob.blockPosition();
				ChunkAccess chunk = mob.level().getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.CARVERS);
				if (chunk instanceof ImposterProtoChunk im) {
					chunk = im.getWrapped();
				}
				if (chunk instanceof LevelChunk c) {
					var opt = c.getCapability(ChunkDifficulty.CAPABILITY);
					if (opt.resolve().isPresent()) {
						cap.init(mob.level(), mob, opt.resolve().get());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
		LivingEntity mob = event.getEntity();
		initMob(mob);
	}


	@SubscribeEvent
	public static void livingTickEvent(LivingEvent.LivingTickEvent event) {
		LivingEntity mob = event.getEntity();
		initMob(mob);
		if (MobModifierCap.HOLDER.isProper(mob)) {
			MobModifierCap cap = MobModifierCap.HOLDER.get(mob);
			cap.tick(mob);
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
		if (player != null && MobModifierCap.HOLDER.isProper(mob)) {
			MobModifierCap cap = MobModifierCap.HOLDER.get(mob);
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
