package dev.xkmc.l2hostility.compat.data;

import com.github.L_Ender.cataclysm.init.ModEntities;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;

public class CataclysmData {

	public static void genRecipe(RegistrateRecipeProvider pvd) {

	}

	public static void genConfig(ConfigDataProvider.Collector collector) {
		addEntity(collector, 200, 50, ModEntities.ENDER_GUARDIAN,
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 2, 2),
				new EntityConfig.TraitBase(LHTraits.WEAKNESS.get(), 3, 5)
		);
		addEntity(collector, 200, 50, ModEntities.NETHERITE_MONSTROSITY,
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 2, 2),
				new EntityConfig.TraitBase(LHTraits.SLOWNESS.get(), 3, 5)
		);
		addEntity(collector, 200, 50, ModEntities.IGNIS,
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1),
				new EntityConfig.TraitBase(LHTraits.SOUL_BURNER.get(), 2, 3)
		);
		addEntity(collector, 200, 50, ModEntities.THE_HARBINGER,
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1),
				new EntityConfig.TraitBase(LHTraits.WITHER.get(), 2, 3)
		);
		addEntity(collector, 200, 50, ModEntities.THE_LEVIATHAN,
				new EntityConfig.TraitBase(LHTraits.REFLECT.get(), 2, 2),
				new EntityConfig.TraitBase(LHTraits.FREEZING.get(), 2, 3)
		);
		addEntity(collector, 100, 30, ModEntities.ENDER_GOLEM,
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 0, 1),
				new EntityConfig.TraitBase(LHTraits.WEAKNESS.get(), 0, 1)
		);
		addEntity(collector, 100, 30, ModEntities.AMETHYST_CRAB,
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 0, 1),
				new EntityConfig.TraitBase(LHTraits.POISON.get(), 0, 1)
		);
	}

}
