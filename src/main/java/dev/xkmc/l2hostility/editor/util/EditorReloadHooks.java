package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.editor.base.EditorSaveState;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = L2Hostility.MODID, value = Dist.CLIENT)
public class EditorReloadHooks {

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			EditorSaveState.savedFlag = false;
		}
	}

}
