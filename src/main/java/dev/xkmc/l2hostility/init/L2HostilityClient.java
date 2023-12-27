package dev.xkmc.l2hostility.init;

import dev.xkmc.l2hostility.content.item.curio.misc.PocketOfRestoration;
import dev.xkmc.l2hostility.content.menu.tab.DifficultyOverlay;
import dev.xkmc.l2hostility.content.menu.tab.DifficultyTab;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.tabs.core.TabRegistry;
import dev.xkmc.l2library.base.tabs.core.TabToken;
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


	public static TabToken<DifficultyTab> TAB_DIFFICULTY;

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_DIFFICULTY = TabRegistry.registerTab(DifficultyTab::new,
					() -> Items.ZOMBIE_HEAD, LangData.INFO_TAB_TITLE.get());

			ItemProperties.register(LHItems.RESTORATION.get(), new ResourceLocation(L2Hostility.MODID, "filled"),
					(stack, level, entity, i) -> stack.getTagElement(PocketOfRestoration.ROOT) == null ? 0 : 1);
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
