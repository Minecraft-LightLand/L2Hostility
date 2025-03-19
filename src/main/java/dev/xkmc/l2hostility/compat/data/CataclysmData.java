package dev.xkmc.l2hostility.compat.data;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.BiConsumer;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;

public class CataclysmData {

	public static void genRecipe(RegistrateRecipeProvider pvd) {

	}

	public static void genConfig(ConfigDataProvider.Collector collector) {
		// bosses
		{
			int equipLevel = 600;
			addEntity(collector, 200, 50, ModEntities.ENDER_GUARDIAN, List.of(
							EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
							EntityConfig.trait(LHTraits.ADAPTIVE.get(), 2, 2),
							EntityConfig.trait(LHTraits.WEAKNESS.get(), 3, 5)),
					List.of(divinity(equipLevel), breaker(equipLevel)));
			addEntity(collector, 200, 50, ModEntities.NETHERITE_MONSTROSITY, List.of(
							EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
							EntityConfig.trait(LHTraits.ADAPTIVE.get(), 2, 2),
							EntityConfig.trait(LHTraits.SLOWNESS.get(), 3, 5)),
					List.of(divinity(equipLevel), breaker(equipLevel)));
			collector.add(L2Hostility.ENTITY, ModEntities.IGNIS.getId(), new EntityConfig()
					.put(EntityConfig.entity(200, 50, 0, 0, List.of(ModEntities.IGNIS.get()))
							.trait(List.of(
									EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
									EntityConfig.trait(LHTraits.CURSED.get(), 1, 1),
									EntityConfig.trait(LHTraits.SOUL_BURNER.get(), 2, 3),
									EntityConfig.trait(LHTraits.MASTER.get(), 1, 0)
							)).item(List.of(
									EntityConfig.simplePool(equipLevel, "equipment/mainhand", getIgnisWeapon()),
									divinity(equipLevel),
									breaker(equipLevel)
							)).master(5, 80,
									new EntityConfig.Minion(EntityType.BLAZE, 2, 0,
											0.9, 16, 100, true, false,
											32, false, true),
									new EntityConfig.Minion(ModEntities.IGNITED_REVENANT.get(), 2, 300,
											0.67, 16, 400, true, false,
											32, true, true),
									new EntityConfig.Minion(ModEntities.IGNITED_BERSERKER.get(), 1, 400,
											0.33, 16, 400, true, false,
											32, true, true)
							)));
			addEntity(collector, 200, 50, ModEntities.THE_HARBINGER, List.of(
							EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
							EntityConfig.trait(LHTraits.CURSED.get(), 1, 1),
							EntityConfig.trait(LHTraits.WITHER.get(), 2, 3)),
					List.of(divinity(equipLevel)));

			addEntity(collector, 200, 50, ModEntities.THE_LEVIATHAN, List.of(
							EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
							EntityConfig.trait(LHTraits.REFLECT.get(), 2, 2),
							EntityConfig.trait(LHTraits.FREEZING.get(), 2, 3)),
					List.of(divinity(equipLevel)));

			collector.add(L2Hostility.ENTITY, ModEntities.ANCIENT_REMNANT.getId(), new EntityConfig()
					.put(EntityConfig.entity(200, 50, 0, 0, List.of(ModEntities.ANCIENT_REMNANT.get()))
							.trait(List.of(
									EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
									EntityConfig.trait(LHTraits.REPRINT.get(), 1, 1),
									EntityConfig.trait(LHTraits.ADAPTIVE.get(), 2, 3),
									EntityConfig.trait(LHTraits.MASTER.get(), 1, 0)
							)).item(List.of(divinity(equipLevel)))
							.master(4, 80,
									new EntityConfig.Minion(ModEntities.KOBOLETON.get(), 2, 0,
											0.8, 16, 250, true, false,
											32, false, true),
									new EntityConfig.Minion(ModEntities.KOBOLEDIATOR.get(), 1, 300,
											0.6, 16, 400, true, false,
											32, true, true),
									new EntityConfig.Minion(ModEntities.WADJET.get(), 1, 300,
											0.6, 16, 400, true, false,
											32, true, true)
							)));


			collector.add(L2Hostility.ENTITY, ModEntities.MALEDICTUS.getId(), new EntityConfig()
					.put(EntityConfig.entity(200, 50, 0, 0, List.of(ModEntities.MALEDICTUS.get()))
							.trait(List.of(
									EntityConfig.trait(LHTraits.TANK.get(), 2, 3),
									EntityConfig.trait(LHTraits.DRAIN.get(), 1, 1),
									EntityConfig.trait(LHTraits.DISPELL.get(), 1, 1),
									EntityConfig.trait(LHTraits.MASTER.get(), 1, 0)
							)).item(List.of(divinity(equipLevel)))
							.master(8, 80,
									new EntityConfig.Minion(ModEntities.DRAUGR.get(), 3, 0,
											0.9, 16, 250, true, false,
											32, false, true),
									new EntityConfig.Minion(ModEntities.ELITE_DRAUGR.get(), 2, 250,
											0.7, 16, 400, true, false,
											32, false, true),
									new EntityConfig.Minion(ModEntities.ROYAL_DRAUGR.get(), 2, 300,
											0.5, 16, 700, true, false,
											32, false, true),
									new EntityConfig.Minion(ModEntities.APTRGANGR.get(), 1, 350,
											0.4, 16, 100, true, false,
											32, true, true)
							)));
		}

		// elite
		{
			addEntity(collector, 100, 30, ModEntities.ENDER_GOLEM,
					EntityConfig.trait(LHTraits.ADAPTIVE.get(), 0, 1),
					EntityConfig.trait(LHTraits.WEAKNESS.get(), 0, 1)
			);
			addEntity(collector, 100, 30, ModEntities.AMETHYST_CRAB,
					EntityConfig.trait(LHTraits.ADAPTIVE.get(), 0, 1),
					EntityConfig.trait(LHTraits.POISON.get(), 0, 1)
			);
			addEntity(collector, 100, 30, ModEntities.IGNITED_BERSERKER,
					EntityConfig.trait(LHTraits.REFLECT.get(), 0, 1),
					EntityConfig.trait(LHTraits.DRAIN.get(), 0, 1)
			);
			addEntity(collector, 100, 30, ModEntities.KOBOLEDIATOR,
					EntityConfig.trait(LHTraits.ADAPTIVE.get(), 0, 1),
					EntityConfig.trait(LHTraits.DISPELL.get(), 0, 1, 250, 1)
			);
			addEntity(collector, 100, 30, ModEntities.WADJET,
					EntityConfig.trait(LHTraits.PROTECTION.get(), 2, 3),
					EntityConfig.trait(LHTraits.DEMENTOR.get(), 0, 1, 250, 1)
			);

			addEntity(collector, 30, 10, ModEntities.DRAUGR,
					EntityConfig.trait(LHTraits.SPEEDY.get(), 1, 2),
					EntityConfig.trait(LHTraits.WEAKNESS.get(), 1, 2),
					EntityConfig.trait(LHTraits.UNDYING.get(), 1, 1),
					EntityConfig.trait(LHTraits.SPLIT.get(), 1, 1, 250, 1)
			);

			addEntity(collector, 70, 20, ModEntities.ELITE_DRAUGR,
					EntityConfig.trait(LHTraits.PROTECTION.get(), 2, 3),
					EntityConfig.trait(LHTraits.SLOWNESS.get(), 1, 2),
					EntityConfig.trait(LHTraits.UNDYING.get(), 1, 1),
					EntityConfig.trait(LHTraits.CORROSION.get(), 0, 1, 250, 1)
			);

			addEntity(collector, 70, 20, ModEntities.ROYAL_DRAUGR,
					EntityConfig.trait(LHTraits.PROTECTION.get(), 2, 3),
					EntityConfig.trait(LHTraits.WITHER.get(), 1, 2),
					EntityConfig.trait(LHTraits.UNDYING.get(), 1, 1),
					EntityConfig.trait(LHTraits.EROSION.get(), 0, 1, 250, 1)
			);

			addEntity(collector, 100, 30, ModEntities.APTRGANGR,
					EntityConfig.trait(LHTraits.PROTECTION.get(), 2, 3),
					EntityConfig.trait(LHTraits.CURSED.get(), 1, 2),
					EntityConfig.trait(LHTraits.DISPELL.get(), 1, 1, 200, 0.5f),
					EntityConfig.trait(LHTraits.ADAPTIVE.get(), 0, 1, 250, 1)
			);
		}

		// weapon
		{
			var config = new WeaponConfig();
			config.special_weapons.put(new LinkedHashSet<>(List.of(
					EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON
			)), new ArrayList<>(List.of(
					new WeaponConfig.ItemConfig(new ArrayList<>(List.of(ItemStack.EMPTY)), 0, 1000),
					weapon(130, 80, ModItems.CURSED_BOW.get(), adv("kill_maledictus")),
					weapon(160, 60, ModItems.LASER_GATLING.get(), adv("kill_harbinger")),
					weapon(190, 40, ModItems.WITHER_ASSULT_SHOULDER_WEAPON.get(), adv("kill_harbinger")),
					weapon(220, 40, ModItems.WRATH_OF_THE_DESERT.get(), adv("kill_remnant"), adv("kill_maledictus")),
					weapon(250, 20, ModItems.VOID_ASSULT_SHOULDER_WEAPON.get(), adv("kill_harbinger"), adv("kill_ender_golem"))
			)));

			config.special_weapons.put(new LinkedHashSet<>(List.of(
					EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK, EntityType.VINDICATOR,
					EntityType.ZOMBIFIED_PIGLIN, EntityType.WITHER_SKELETON
			)), new ArrayList<>(List.of(
					new WeaponConfig.ItemConfig(new ArrayList<>(List.of(ItemStack.EMPTY)), 0, 1000),
					weapon(130, 60, ModItems.ANCIENT_SPEAR.get(), adv("find_cursed_pyramid")),
					weapon(160, 40, ModItems.INFERNAL_FORGE.get(), adv("kill_monstrosity")),
					weapon(190, 40, ModItems.MEAT_SHREDDER.get(), adv("kill_harbinger")),
					weapon(190, 40, ModItems.SOUL_RENDER.get(), adv("kill_maledictus")),
					weapon(220, 20, ModItems.GAUNTLET_OF_MAELSTROM.get(), adv("kill_ender_guardian"), adv("kill_remnant")),
					weapon(250, 20, ModItems.VOID_FORGE.get(), adv("kill_monstrosity"), adv("kill_ender_golem"))
			)));

			collector.add(L2Hostility.WEAPON, new ResourceLocation(Cataclysm.MODID, "weapons"), config);
		}


	}

	private static ResourceLocation adv(String id) {
		return new ResourceLocation(Cataclysm.MODID, id);
	}

	private static WeaponConfig.ItemConfig weapon(int level, int weight, Item item, ResourceLocation... adv) {
		return new WeaponConfig.ItemConfig(new ArrayList<>(List.of(item.getDefaultInstance())), level, weight,
				new WeaponConfig.ItemCondition(new ArrayList<>(List.of(adv)), null));
	}

	private static ItemStack getIgnisWeapon() {
		ItemStack stack = ModItems.THE_INCINERATOR.get().getDefaultInstance();
		stack.enchant(LCEnchantments.VOID_TOUCH.get(), 10);
		stack.enchant(Enchantments.VANISHING_CURSE, 1);
		stack.enchant(LHEnchantments.VANISH.get(), 1);
		return stack;
	}

	private static EntityConfig.ItemPool divinity(int lv) {
		ItemStack stack = LHItems.RING_DIVINITY.asStack();
		stack.enchant(Enchantments.VANISHING_CURSE, 1);
		return EntityConfig.simplePool(lv, "curios/ring", stack);
	}

	private static EntityConfig.ItemPool breaker(int lv) {
		ItemStack stack = LHItems.IMAGINE_BREAKER.asStack();
		stack.enchant(Enchantments.VANISHING_CURSE, 1);
		return EntityConfig.simplePool(lv, "curios/hands", stack);
	}

	public static void genSlot(BiConsumer<String, Record> map) {
		map.accept("l2hostility/curios/entities/l2hostility_cataclysm", new CurioEntityBuilder(
				new ArrayList<>(List.of(
						ModEntities.ENDER_GUARDIAN.getId(),
						ModEntities.NETHERITE_MONSTROSITY.getId(),
						ModEntities.IGNIS.getId(),
						ModEntities.THE_HARBINGER.getId(),
						ModEntities.THE_LEVIATHAN.getId()
				)),
				new ArrayList<>(List.of("ring", "hands")),
				SlotCondition.of()
		));
	}
}
