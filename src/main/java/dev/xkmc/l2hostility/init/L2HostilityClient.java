package dev.xkmc.l2hostility.init;

import dev.xkmc.l2hostility.content.item.curio.misc.PocketOfRestoration;
import dev.xkmc.l2hostility.content.menu.tab.DifficultyOverlay;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Hostility.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2HostilityClient {

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(LHItems.RESTORATION.get(), L2Hostility.loc("filled"),
					(stack, level, entity, i) -> stack.getTagElement(PocketOfRestoration.ROOT) == null ? 0 : 1);
		});
	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, L2Hostility.loc("info"), new DifficultyOverlay());
	}

}
