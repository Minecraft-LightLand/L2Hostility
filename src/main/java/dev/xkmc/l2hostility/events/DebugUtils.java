package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.Level;

public class DebugUtils {

	public static void checkThread() {
		if (!isGoodThread()) {
			var err = new IllegalStateException("Modify Attributes on wrong threads");
			for (var e : err.getStackTrace()) {
				if (e.getClassName().contains("net.minecraft.world.level.ChunkMap"))
					return;
			}
			L2Hostility.LOGGER.error("Wrong thread!!!");
			L2Hostility.LOGGER.throwing(Level.ERROR, err);
		}

	}

	private static boolean isGoodThread() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			if (Minecraft.getInstance().level == null) return true;
			if (Minecraft.getInstance().player == null) return true;
			if (Minecraft.getInstance().isSameThread()) return true;
		}
		var server = Proxy.getServer();
		if (server.isEmpty()) return true;
		if (server.get().isSameThread()) return true;
		return false;
	}

}