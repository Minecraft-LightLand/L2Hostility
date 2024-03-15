package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.init.network.ClientSyncHandler;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ChunkCapSyncToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public CompoundTag tag;

	@SerialClass.SerialField
	public ResourceLocation level;

	@SerialClass.SerialField
	public int x, z;

	@Deprecated
	public ChunkCapSyncToClient() {
	}

	public ChunkCapSyncToClient(ChunkDifficulty chunk) {
		this.level = chunk.chunk.getLevel().dimension().location();
		this.x = chunk.chunk.getPos().x;
		this.z = chunk.chunk.getPos().z;
		this.tag = TagCodec.toTag(new CompoundTag(), chunk);
	}


	@Override
	public void handle(NetworkEvent.Context context) {
		ClientSyncHandler.handleChunkUpdate(level, x, z, tag);
	}

}
