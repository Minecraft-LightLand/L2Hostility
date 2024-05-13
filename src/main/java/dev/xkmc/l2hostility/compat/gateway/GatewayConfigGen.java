package dev.xkmc.l2hostility.compat.gateway;

import dev.shadowsoffire.gateways.Gateways;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class GatewayConfigGen {

	public static void genConfig(ConfigDataProvider.Collector collector) {
		/*
		collector.add(L2Hostility.ENTITY, new ResourceLocation(Gateways.MODID, "slime_gate_minions"), new EntityConfig()
				.put(EntityConfig.entity(30, 10, 1, 0, List.of(EntityType.SLIME)).trait(List.of(
						EntityConfig.trait(LHTraits.GROWTH.get(), 1, 1, 100, 0.5f),
						EntityConfig.trait(LHTraits.TANK.get(), 2, 2, 100, 0.5f),
						EntityConfig.trait(LHTraits.ADAPTIVE.get(), 1, 1, 200, 0.3f)
				)).conditions(GatewayCondition.of(new ResourceLocation(Gateways.MODID, "basic/slime"),
						0, 2, 100, 0.5))));

		collector.add(L2Hostility.ENTITY, new ResourceLocation(Gateways.MODID, "slime_gate_boss"), new EntityConfig()
				.put(EntityConfig.entity(50, 20, 1, 0, List.of(EntityType.SLIME)).trait(List.of(
						EntityConfig.trait(LHTraits.GROWTH.get(), 2, 2, 200, 1),
						EntityConfig.trait(LHTraits.DISPELL.get(), 1, 1, 300, 0.5f),
						EntityConfig.trait(LHTraits.DEMENTOR.get(), 1, 1, 300, 0.3f)
				)).conditions(GatewayCondition.of(new ResourceLocation(Gateways.MODID, "basic/slime"),
						3, 1, 1))));
		 */
	}

}
