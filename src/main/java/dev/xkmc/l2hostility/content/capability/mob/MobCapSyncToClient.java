package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record MobCapSyncToClient(int id, CompoundTag tag) implements SerialPacketBase<MobCapSyncToClient> {

	public static MobCapSyncToClient of(LivingEntity entity, MobTraitCap cap) {
		var tag = new TagCodec(entity.level().registryAccess())
				.pred(SerialField::toClient)
				.toTag(new CompoundTag(), MobTraitCap.class, cap);
		return new MobCapSyncToClient(entity.getId(), tag);
	}

	@Override
	public void handle(Player player) {
		ClientCapHandler.handle(this);
	}

}
