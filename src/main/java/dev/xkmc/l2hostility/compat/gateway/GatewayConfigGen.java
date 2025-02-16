package dev.xkmc.l2hostility.compat.gateway;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.shadowsoffire.gateways.GatewayObjects;
import dev.shadowsoffire.gateways.Gateways;
import dev.shadowsoffire.gateways.gate.*;
import dev.shadowsoffire.gateways.gate.endless.ApplicationMode;
import dev.shadowsoffire.gateways.gate.normal.NormalGateway;
import dev.shadowsoffire.gateways.item.GatePearlItem;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.l2core.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.RecipeGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class GatewayConfigGen extends PlaceboCodecDataGen<Gateway> {

	private static HostilityGateways slimeGate() {
		var g2 = EntityConfig.trait(LHTraits.GROWTH.get(), 2, 2);
		var g3 = EntityConfig.trait(LHTraits.GROWTH.get(), 2, 3);
		var t2 = EntityConfig.trait(LHTraits.TANK.get(), 2, 2);
		var t3 = EntityConfig.trait(LHTraits.TANK.get(), 2, 3);
		var t4 = EntityConfig.trait(LHTraits.TANK.get(), 3, 4);
		var adaptive = EntityConfig.trait(LHTraits.ADAPTIVE.get(), 2, 2);
		var strike = EntityConfig.trait(LHTraits.STRIKE.get(), 1, 1);
		var reflect = EntityConfig.trait(LHTraits.REFLECT.get(), 2, 2);
		var dem = EntityConfig.trait(LHTraits.DEMENTOR.get(), 1, 1);
		var dis = EntityConfig.trait(LHTraits.DISPELL.get(), 1, 1);
		return new HostilityGateways(LHTraits.GROWTH.get(), 2, 2000, Items.SLIME_BALL, List.of(
				wave(100, 1200, slime(EntityType.SLIME, 2, 4)),
				wave(100, 1800,
						slime(EntityType.SLIME, 2, 3),
						slime(EntityType.MAGMA_CUBE, 2, 1)),
				wave(120, 2400,
						slime(EntityType.SLIME, 2, 2),
						slime(EntityType.MAGMA_CUBE, 2, 2)),
				wave(120, 3000,
						slime(EntityType.SLIME, 3, 1),
						slime(EntityType.MAGMA_CUBE, 3, 1),
						slime(EntityType.SLIME, 2, 2),
						slime(EntityType.MAGMA_CUBE, 2, 2)),
				wave(140, 3600,
						slime(EntityType.SLIME, 3, 3),
						slime(EntityType.MAGMA_CUBE, 3, 3))
		), List.of(
				e -> config(e, 3, -1, 1, 150, 80, EntityType.SLIME).trait(List.of(g3, t4, strike, dem)),
				e -> config(e, 3, -1, 1, 150, 80, EntityType.MAGMA_CUBE).trait(List.of(g3, adaptive, reflect, dis)),
				e -> config(e, 2, -1, 1, 130, 70, EntityType.SLIME).trait(List.of(g3, t3, strike)),
				e -> config(e, 2, -1, 1, 130, 70, EntityType.MAGMA_CUBE).trait(List.of(g3, adaptive, reflect)),
				e -> config(e, 1, -1, 1, 120, 60, EntityType.SLIME).trait(List.of(g2, t2)),
				e -> config(e, 1, -1, 1, 120, 60, EntityType.MAGMA_CUBE).trait(List.of(g2, adaptive)),
				e -> config(e, 0, -1, 100, 100, 50, EntityType.SLIME, EntityType.MAGMA_CUBE).trait(List.of(g2))
		));
	}

	private static HostilityGateways splitGate() {
		var types = new EntityType[]{EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK,
				EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.STRAY};
		var g1 = EntityConfig.trait(LHTraits.SPLIT.get(), 1, 1);
		var g2 = EntityConfig.trait(LHTraits.SPLIT.get(), 1, 2);
		var g3 = EntityConfig.trait(LHTraits.SPLIT.get(), 2, 3);
		return new HostilityGateways(LHTraits.SPLIT.get(), 2, 2000, Items.ROTTEN_FLESH, List.of(
				wave(100, 1200, entity(EntityType.ZOMBIE, 4)),
				wave(100, 1800,
						entity(EntityType.SKELETON, 2),
						entity(EntityType.ZOMBIE, 2)),
				wave(120, 2400,
						entity(EntityType.DROWNED, 2),
						entity(EntityType.SKELETON, 2),
						entity(EntityType.HUSK, 2)),
				wave(120, 3000,
						entity(EntityType.DROWNED, 2),
						entity(EntityType.WITHER_SKELETON, 2),
						entity(EntityType.STRAY, 1),
						entity(EntityType.ZOMBIE, 1),
						entity(EntityType.HUSK, 1),
						entity(EntityType.SKELETON, 1)),
				wave(140, 3600,
						entity(EntityType.VEX, 2),
						entity(EntityType.CAVE_SPIDER, 2),
						entity(EntityType.ZOMBIE, 1),
						entity(EntityType.HUSK, 1),
						entity(EntityType.SKELETON, 1),
						entity(EntityType.DROWNED, 1),
						entity(EntityType.WITHER_SKELETON, 1),
						entity(EntityType.STRAY, 1))
		), List.of(
				e -> config(e, 0, 0, 10, 100, 50, types).trait(List.of(g1)),
				e -> config(e, 1, 2, 10, 120, 70, types).trait(List.of(g2)),
				e -> config(e, 3, 4, 10, 140, 90, types).trait(List.of(g3))
		));
	}

	public static final List<HostilityGateways> GATEWAYS = List.of(
			slimeGate(), splitGate()
	);

	public GatewayConfigGen(DataGenerator generator) {
		super(generator, "L2Hostility x Gateways Compat");
		WaveModifier.initSerializers();
		Reward.initSerializers();
		WaveEntity.initSerializers();
		Failure.initSerializers();
		ApplicationMode.initSerializers();
	}

	public static void genConfig(ConfigDataProvider.Collector collector) {
		for (var e : GATEWAYS) {
			var config = new EntityConfig();
			e.configs().forEach(x -> config.put(x.apply(e.path())));
			collector.add(L2Hostility.ENTITY, ResourceLocation.fromNamespaceAndPath(Gateways.MODID,
					e.trait().getRegistryName().getPath()), config);
		}
	}

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		for (var e : GATEWAYS) {
			ItemStack result = GatewayObjects.GATE_PEARL.value().getDefaultInstance();
			GatePearlItem.setGate(result, GatewayRegistry.INSTANCE.holder(e.path()));
			RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)::unlockedBy, LCItems.VOID_EYE.get())
					.pattern("1A1").pattern("ABA").pattern("1A1")
					.define('B', Items.ENDER_PEARL)
					.define('A', LHItems.HOSTILITY_ESSENCE)
					.define('1', e.mat())
					.save(ConditionalRecipeWrapper.mod(pvd, Gateways.MODID), e.path());
		}
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (var e : GATEWAYS) {
			pvd.add(e.path().toString().replace(':', '.'),
					RegistrateLangProvider.toEnglishName(e.trait().getRegistryName().getPath()) + " Gateway");
		}
	}

	@Override
	public void add(BiConsumer<String, Gateway> map) {
		for (var e : GATEWAYS) add(map, e);
	}

	private void add(BiConsumer<String, Gateway> map, HostilityGateways gate) {
		map.accept(Gateways.MODID + "/gateways/hostility/" + gate.trait().getRegistryName().getPath(), new NormalGateway(
				Gateway.Size.MEDIUM, TextColor.fromRgb(gate.trait().getColor()), gate.waves(), List.of(
				new Reward.StackReward(new ItemStack(gate.trait().asItem(), gate.count())),
				new Reward.XpReward(gate.xp(), gate.xp() / 10)
		), List.of(), SpawnAlgorithms.OPEN_FIELD, GateRules.DEFAULT, BossEventSettings.DEFAULT));
	}

	private static Wave wave(int setup, int max, WaveEntity... entities) {
		return new Wave(List.of(entities), List.of(), List.of(), max, setup);
	}

	private static WaveEntity entity(EntityType<?> type, int count) {
		return new WaveEntity.StandardWaveEntity(type, Optional.empty(), Optional.empty(), List.of(), false, count);
	}

	private static WaveEntity slime(EntityType<?> type, int size, int count) {
		return new WaveEntity.StandardWaveEntity(type, Optional.empty(), Optional.of(
				Util.make(new CompoundTag(), e -> e.putInt("Size", (1 << size) - 1))
		), List.of(), false, count);
	}

	private static EntityConfig.Config config(ResourceLocation id, int start, int end, int count, int min, int base, EntityType<?>... types) {
		return EntityConfig.entity(min, base, 0, 0, List.of(types))
				.conditions(GatewayCondition.of(id, start, end, count, 1))
				.blacklist(LHTraits.MOONWALK.get(), LHTraits.GRAVITY.get(), LHTraits.INVISIBLE.get());
	}

}
