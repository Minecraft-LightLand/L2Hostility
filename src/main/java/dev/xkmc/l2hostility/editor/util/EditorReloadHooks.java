package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.editor.base.EditorSaveState;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, value = Dist.CLIENT)
public class EditorReloadHooks {

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			EditorSaveState.savedFlag = false;
		}
	}

}
