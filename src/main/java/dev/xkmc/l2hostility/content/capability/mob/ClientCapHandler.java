package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ClientCapHandler {

	public static void handle(MobCapSyncToClient packet) {
		Level level = Minecraft.getInstance().level;
		if (level == null) return;
		Entity entity = level.getEntity(packet.id());
		if (!(entity instanceof LivingEntity le)) return;
		if (!LHMiscs.MOB.type().isProper(le)) return;
		var opt = LHMiscs.MOB.type().getOrCreate(le);
		new TagCodec(level.registryAccess())
				.pred(SerialField::toClient)
				.fromTag(packet.tag(), MobTraitCap.class, opt);
	}

}
