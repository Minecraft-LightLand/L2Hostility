package dev.xkmc.l2hostility.compat.data;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import fuzs.mutantmonsters.MutantMonsters;
import fuzs.mutantmonsters.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MutantMonsterData {

	public static void genConfig(ConfigDataProvider.Collector collector) {
		collector.add(L2Hostility.ENTITY, new ResourceLocation(MutantMonsters.MOD_ID, "bosses"), new EntityConfig()
				.put(EntityConfig.entity(0, 0, 0, 0, List.of(
						ModRegistry.MUTANT_ZOMBIE_ENTITY_TYPE.get(),
						ModRegistry.MUTANT_SKELETON_ENTITY_TYPE.get(),
						ModRegistry.MUTANT_CREEPER_ENTITY_TYPE.get(),
						ModRegistry.MUTANT_ENDERMAN_ENTITY_TYPE.get()
				)).minLevel(100).trait(List.of(
						EntityConfig.trait(LHTraits.REPRINT.get(), 1, 1, 150, 0.5f),
						EntityConfig.trait(LHTraits.ADAPTIVE.get(), 1, 2, 200, 0.5f)
				))));
	}

}
