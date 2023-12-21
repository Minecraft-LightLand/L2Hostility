package dev.xkmc.l2hostility.compat.data;

import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;

public class CataclysmData {

	public static void genRecipe(RegistrateRecipeProvider pvd) {

	}

	public static void genConfig(ConfigDataProvider.Collector collector) {
		int equipLevel = 600;
		addEntity(collector, 200, 50, ModEntities.ENDER_GUARDIAN, List.of(
						new EntityConfig.TraitBase(LHTraits.TANK.get(), 2, 3),
						new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 2, 2),
						new EntityConfig.TraitBase(LHTraits.WEAKNESS.get(), 3, 5)),
				List.of(divinity(equipLevel), breaker(equipLevel)));
		addEntity(collector, 200, 50, ModEntities.NETHERITE_MONSTROSITY, List.of(
						new EntityConfig.TraitBase(LHTraits.TANK.get(), 2, 3),
						new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 2, 2),
						new EntityConfig.TraitBase(LHTraits.SLOWNESS.get(), 3, 5)),
				List.of(divinity(equipLevel), breaker(equipLevel)));
		addEntity(collector, 200, 50, ModEntities.IGNIS, List.of(
						new EntityConfig.TraitBase(LHTraits.TANK.get(), 2, 3),
						new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1),
						new EntityConfig.TraitBase(LHTraits.SOUL_BURNER.get(), 2, 3)),
				List.of(
						EntityConfig.simplePool(equipLevel, "equipment/mainhand", getIgnisWeapon()),
						divinity(equipLevel), breaker(equipLevel)
				));
		addEntity(collector, 200, 50, ModEntities.THE_HARBINGER, List.of(
						new EntityConfig.TraitBase(LHTraits.TANK.get(), 2, 3),
						new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1),
						new EntityConfig.TraitBase(LHTraits.WITHER.get(), 2, 3)),
				List.of(divinity(equipLevel)));

		addEntity(collector, 200, 50, ModEntities.THE_LEVIATHAN, List.of(
						new EntityConfig.TraitBase(LHTraits.TANK.get(), 2, 3),
						new EntityConfig.TraitBase(LHTraits.REFLECT.get(), 2, 2),
						new EntityConfig.TraitBase(LHTraits.FREEZING.get(), 2, 3)),
				List.of(divinity(equipLevel)));

		addEntity(collector, 200, 50, ModEntities.ANCIENT_REMNANT, List.of(
						new EntityConfig.TraitBase(LHTraits.TANK.get(), 2, 3),
						new EntityConfig.TraitBase(LHTraits.REPRINT.get(), 1, 1),
						new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 2, 3)),
				List.of(divinity(equipLevel)));

		addEntity(collector, 100, 30, ModEntities.ENDER_GOLEM,
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 0, 1),
				new EntityConfig.TraitBase(LHTraits.WEAKNESS.get(), 0, 1)
		);
		addEntity(collector, 100, 30, ModEntities.AMETHYST_CRAB,
				new EntityConfig.TraitBase(LHTraits.ADAPTIVE.get(), 0, 1),
				new EntityConfig.TraitBase(LHTraits.POISON.get(), 0, 1)
		);
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
