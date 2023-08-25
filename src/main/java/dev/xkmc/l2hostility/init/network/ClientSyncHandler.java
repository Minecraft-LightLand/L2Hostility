package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientSyncHandler {

	public static void handleChunkUpdate(ResourceLocation id, int x, int z, CompoundTag tag) {
		Player player = Proxy.getClientPlayer();
		if (player == null) return;
		PlayerDifficulty.HOLDER.get(player).pendingFlag = false;
		Level level = Minecraft.getInstance().level;
		if (level == null || !level.dimension().location().equals(id)) return;
		var cap = ChunkDifficulty.at(level, x, z);
		if (cap.isEmpty()) return;
		ChunkDifficulty diff = cap.get();
		TagCodec.fromTag(tag, ChunkDifficulty.class, diff, e -> true);
	}

	public static void handleEffect(TraitEffectToClient packet) {
		packet.effect.func.get().accept(packet);
	}

	public static void triggerUndying(TraitEffectToClient packet) {
		if (Proxy.getClientWorld() != null && packet.id >= 0 && Proxy.getClientWorld().getEntity(packet.id) instanceof LivingEntity entity) {
			Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
			entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
		}
	}

	public static void triggerClear(TraitEffectToClient packet) {
		ClientLevel lv = Proxy.getClientWorld();
		if (lv != null && lv.isLoaded(packet.pos)) {
			int x = packet.pos.getX() & -15;
			int y = packet.pos.getY() & -15;
			int z = packet.pos.getZ() & -15;
			for (int i = 0; i < 128; i++) {
				double dx = x + lv.getRandom().nextDouble() * 16;
				double dy = y + lv.getRandom().nextDouble() * 16;
				double dz = z + lv.getRandom().nextDouble() * 16;
				lv.addParticle(ParticleTypes.HAPPY_VILLAGER, dx, dy, dz, 0, 0, 0);
			}
			lv.playLocalSound(x + 8, y + 8, z + 8, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F, false);
		}
	}

}
