package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class InfoRequestToServer extends SerialPacketBase {

	@SerialClass.SerialField
	public ResourceLocation level;

	@SerialClass.SerialField
	public int x, z;

	@Deprecated
	public InfoRequestToServer() {

	}

	public InfoRequestToServer(LevelChunk chunk) {
		level = chunk.getLevel().dimension().location();
		x = chunk.getPos().x;
		z = chunk.getPos().z;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ServerPlayer player = context.getSender();
		if (player == null) return;
		MinecraftServer server = player.level().getServer();
		if (server == null) return;
		ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, this.level));
		if (level == null) return;
		var cap = ChunkDifficulty.at(level, x, z);
		if (cap.isEmpty()) return;
		ChunkDifficulty diff = cap.get();
		CompoundTag tag = TagCodec.toTag(new CompoundTag(), diff);
		if (tag == null) return;
		L2Hostility.HANDLER.toClientPlayer(new CapSyncToClient(this.level, x, z, tag), player);
	}

}
