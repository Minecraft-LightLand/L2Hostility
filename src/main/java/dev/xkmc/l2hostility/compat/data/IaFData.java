package dev.xkmc.l2hostility.compat.data;

import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.network.BaseConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;

public class IaFData {

	public static void genConfig(Map<String, BaseConfig> collector) {
		addEntity(collector, 100, 50, IafEntityRegistry.FIRE_DRAGON, List.of(
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 1, 2),
				new EntityConfig.TraitBase(LHTraits.REGEN.get(), 2, 3),
				new EntityConfig.TraitBase(LHTraits.SOUL_BURNER.get(), 2, 3)
		), List.of(LHTraits.TANK.get()));
		addEntity(collector, 100, 50, IafEntityRegistry.ICE_DRAGON, List.of(
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 1, 2),
				new EntityConfig.TraitBase(LHTraits.REGEN.get(), 2, 3),
				new EntityConfig.TraitBase(LHTraits.FREEZING.get(), 2, 3)
		), List.of(LHTraits.TANK.get()));
		addEntity(collector, 100, 50, IafEntityRegistry.LIGHTNING_DRAGON, List.of(
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 1, 2),
				new EntityConfig.TraitBase(LHTraits.REGEN.get(), 2, 3),
				new EntityConfig.TraitBase(LHTraits.REFLECT.get(), 2, 3)
		), List.of(LHTraits.TANK.get()));
	}

	public static <T extends LivingEntity> void addEntity(Map<String, BaseConfig> collector, int min, int base, RegistryObject<EntityType<T>> obj, List<EntityConfig.TraitBase> traits, List<MobTrait> ban) {
		var config = new EntityConfig();
		config.putEntityAndItem(min, base, 0, 0, List.of(obj.get()), traits, List.of());
		config.list.get(0).blacklist().addAll(ban);
		L2Hostility.ENTITY.add(collector, obj.getId(), config);
	}

}
