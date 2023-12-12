package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ClientCapHandler {

	public static void handle(CapSyncPacket packet) {
		Level level = Minecraft.getInstance().level;
		if (level == null) return;
		Entity entity = level.getEntity(packet.id);
		if (!(entity instanceof LivingEntity le)) return;
		if (!MobTraitCap.HOLDER.isProper(le)) return;
		TagCodec.fromTag(packet.tag, MobTraitCap.class, MobTraitCap.HOLDER.get(le), SerialClass.SerialField::toClient);
	}

}
