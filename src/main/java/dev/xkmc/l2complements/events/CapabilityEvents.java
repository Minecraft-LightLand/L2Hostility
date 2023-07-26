package dev.xkmc.l2complements.events;

import dev.xkmc.l2complements.content.capability.chunk.ChunkDifficultyCap;
import dev.xkmc.l2complements.content.capability.mob.MobModifierCap;
import dev.xkmc.l2complements.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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

	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof LivingEntity entity) {
			if (MobModifierCap.HOLDER.isProper(entity)) {

			}
		}
	}

}
