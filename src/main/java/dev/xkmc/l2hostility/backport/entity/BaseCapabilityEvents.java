package dev.xkmc.l2hostility.backport.entity;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BaseCapabilityEvents {

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		for (GeneralCapabilityHolder<?, ?> holder : GeneralCapabilityHolder.INTERNAL_MAP.values()) {
			Entity e = event.getObject();
			if (holder.entity_class.isInstance(e)) {
				if (holder.shouldHaveCap(Wrappers.cast(e))) {
					event.addCapability(holder.id, holder.generateSerializer(Wrappers.cast(e)));
				}
			}
		}
	}

}
