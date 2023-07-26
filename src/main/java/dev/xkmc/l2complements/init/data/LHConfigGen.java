package dev.xkmc.l2complements.init.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;

public class LHConfigGen extends ConfigDataProvider {

	public LHConfigGen(DataGenerator generator) {
		super(generator, "L2Hostility Config");
	}

	@Override
	public void add(Collector collector) {
	}

}