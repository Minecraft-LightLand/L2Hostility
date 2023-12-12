package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
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

	public CapSyncPacket(LivingEntity entity, MobTraitCap cap) {
		id = entity.getId();
		tag = TagCodec.toTag(new CompoundTag(), MobTraitCap.class, cap, SerialClass.SerialField::toClient);
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientCapHandler.handle(this);
	}

}
