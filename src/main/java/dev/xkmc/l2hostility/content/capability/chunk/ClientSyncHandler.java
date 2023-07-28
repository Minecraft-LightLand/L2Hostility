package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

}
