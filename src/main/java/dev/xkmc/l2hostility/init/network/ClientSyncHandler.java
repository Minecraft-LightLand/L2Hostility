package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ClientSyncHandler {

	public static void handleChunkUpdate(ResourceLocation id, int x, int z, CompoundTag tag) {
		Player player = Proxy.getClientPlayer();
		if (player == null) return;
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
		if (!LHConfig.CLIENT.showUndyingParticles.get()) return;
		if (Proxy.getClientWorld() != null && packet.id >= 0 && Proxy.getClientWorld().getEntity(packet.id) instanceof LivingEntity entity) {
			Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
			entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
		}
	}

	public static void triggerAura(TraitEffectToClient packet) {
		if (Proxy.getClientWorld() != null && packet.id >= 0 && Proxy.getClientWorld().getEntity(packet.id) instanceof LivingEntity entity) {
			Level level = Proxy.getClientWorld();
			double radius = LHConfig.COMMON.killerAuraRange.get();
			Vec3 center = entity.position();
			for (int i = 0; i < 100; i++) {
				float tpi = (float) (Math.PI * 2);
				Vec3 v0 = new Vec3(0, radius, 0);
				v0 = v0.xRot(tpi / 4).yRot(level.getRandom().nextFloat() * tpi);
				level.addAlwaysVisibleParticle(ParticleTypes.FLAME,
						center.x + v0.x,
						center.y + v0.y + 0.5f,
						center.z + v0.z, 0, 0, 0);
			}
			if (LHConfig.CLIENT.killerAuraSoundEffect.get())
				entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRECHARGE_USE,
						entity.getSoundSource(), 3, 1.0F, false);
		}
	}

}
