package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
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

public class ClientSyncHandler {

	public static void handle(ResourceLocation id, int x, int z, CompoundTag tag) {
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

	public static void handleEffect(int id, MobTrait trait, TraitEffects effect) {
		if (Proxy.getClientWorld() != null && Proxy.getClientWorld().getEntity(id) instanceof LivingEntity le) {
			effect.func.get().accept(le, trait);
		}
	}

	public static void triggerUndying(LivingEntity entity, MobTrait trait) {
		Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
		entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
	}

}
