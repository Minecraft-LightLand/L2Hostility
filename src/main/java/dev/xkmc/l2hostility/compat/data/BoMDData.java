package dev.xkmc.l2hostility.compat.data;

import com.cerbon.bosses_of_mass_destruction.entity.BMDEntities;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;

import java.util.List;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;

public class BoMDData {

	public static void genConfig(ConfigDataProvider.Collector collector) {
		addEntity(collector, 200, 50, BMDEntities.LICH, List.of(
				EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
				EntityConfig.trait(LHTraits.REPRINT.get(), 1, 1),
				EntityConfig.trait(LHTraits.FREEZING.get(), 2, 3)
		), List.of());
		addEntity(collector, 200, 50, BMDEntities.OBSIDILITH, List.of(
				EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
				EntityConfig.trait(LHTraits.REFLECT.get(), 2, 3),
				EntityConfig.trait(LHTraits.WEAKNESS.get(), 5, 5)
		), List.of());
		addEntity(collector, 200, 50, BMDEntities.GAUNTLET, List.of(
				EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
				EntityConfig.trait(LHTraits.REFLECT.get(), 2, 3),
				EntityConfig.trait(LHTraits.SOUL_BURNER.get(), 2, 3)
		), List.of());
		addEntity(collector, 200, 50, BMDEntities.VOID_BLOSSOM, List.of(
				EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
				EntityConfig.trait(LHTraits.REGEN.get(), 5, 5),
				EntityConfig.trait(LHTraits.ADAPTIVE.get(), 2, 3)
		), List.of());
	}
}
