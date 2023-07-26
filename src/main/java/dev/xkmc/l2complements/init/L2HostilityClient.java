package dev.xkmc.l2complements.init;

import dev.xkmc.l2complements.init.registrate.LHParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2HostilityClient {

	@SubscribeEvent
	public static void onParticleRegistryEvent(RegisterParticleProvidersEvent event) {
		LHParticle.registerClient();
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
	}


	@SubscribeEvent
	public static void onResourceReload(RegisterClientReloadListenersEvent event) {
	}

}
