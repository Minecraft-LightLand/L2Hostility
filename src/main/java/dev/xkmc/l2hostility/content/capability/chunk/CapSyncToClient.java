package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.init.network.ClientSyncHandler;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class CapSyncToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public CompoundTag tag;

	@SerialClass.SerialField
	public ResourceLocation level;

	@SerialClass.SerialField
	public int x, z;

	public CapSyncToClient(ResourceLocation level, int x, int z, CompoundTag tag) {
		this.level = level;
		this.x = x;
		this.z = z;
		this.tag = tag;
	}


	@Override
	public void handle(NetworkEvent.Context context) {
		ClientSyncHandler.handle(level, x, z, tag);
	}

}
