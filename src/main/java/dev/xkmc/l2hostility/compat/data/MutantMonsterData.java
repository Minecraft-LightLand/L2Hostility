package dev.xkmc.l2hostility.compat.data;

import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import fuzs.mutantmonsters.MutantMonsters;
import fuzs.mutantmonsters.init.ModEntityTypes;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MutantMonsterData {

	public static void genConfig(ConfigDataProvider.Collector collector) {
		collector.add(L2Hostility.ENTITY, ResourceLocation.fromNamespaceAndPath(MutantMonsters.MOD_ID, "bosses"), new EntityConfig()
				.put(EntityConfig.entity(0, 0, 0, 0, List.of(
						ModEntityTypes.MUTANT_ZOMBIE_ENTITY_TYPE.value(),
						ModEntityTypes.MUTANT_SKELETON_ENTITY_TYPE.value(),
						ModEntityTypes.MUTANT_CREEPER_ENTITY_TYPE.value(),
						ModEntityTypes.MUTANT_ENDERMAN_ENTITY_TYPE.value()
				)).minLevel(100).trait(List.of(
						EntityConfig.trait(LHTraits.REPRINT.get(), 1, 1, 150, 0.5f),
						EntityConfig.trait(LHTraits.ADAPTIVE.get(), 1, 2, 200, 0.5f)
				))));
	}

}
