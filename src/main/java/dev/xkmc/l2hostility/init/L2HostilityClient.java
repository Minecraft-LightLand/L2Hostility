package dev.xkmc.l2hostility.init;

import dev.xkmc.l2hostility.content.menu.tab.DifficultyOverlay;
import dev.xkmc.l2hostility.content.menu.tab.DifficultyTab;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import dev.xkmc.l2tabs.tabs.inventory.TabRegistry;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2HostilityClient {

	public static TabToken<InvTabData, DifficultyTab> TAB_DIFFICULTY;

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_DIFFICULTY = TabRegistry.GROUP.registerTab(5000, DifficultyTab::new,
					() -> Items.ZOMBIE_HEAD, LangData.INFO_TAB_TITLE.get());
		});
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void onResourceReload(RegisterClientReloadListenersEvent event) {
	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "l2hostility", new DifficultyOverlay());
	}

}
