package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class CapSyncPacket extends SerialPacketBase {

	@SerialClass.SerialField
	public CompoundTag tag;
	@SerialClass.SerialField
	public int id;

	@Deprecated
	public CapSyncPacket() {
	}

	public CapSyncPacket(LivingEntity entity, MobModifierCap cap) {
		id = entity.getId();
		tag = TagCodec.toTag(new CompoundTag(), MobModifierCap.class, cap, SerialClass.SerialField::toClient);
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientCapHandler.handle(this);
	}

}
