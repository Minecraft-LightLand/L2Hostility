package dev.xkmc.l2hostility.compat.data;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;

public class MowzieData {

	public static void genConfig(ConfigDataProvider.Collector collector) {
		addEntity(collector, 100, 30, EntityHandler.FROSTMAW);
		addEntity(collector, 100, 30, EntityHandler.UMVUTHI);
		addEntity(collector, 100, 30, EntityHandler.WROUGHTNAUT);
	}

}
