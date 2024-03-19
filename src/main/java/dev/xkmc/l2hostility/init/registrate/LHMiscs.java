package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2damagetracker.contents.attributes.WrappedAttribute;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2hostility.compat.curios.EntityCuriosListMenu;
import dev.xkmc.l2hostility.compat.curios.EntityCuriosListScreen;
import dev.xkmc.l2hostility.content.menu.equipments.EquipmentsMenu;
import dev.xkmc.l2hostility.content.menu.equipments.EquipmentsScreen;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2hostility.init.L2Hostility.REGISTRATE;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class LHMiscs {

	public static final MenuEntry<EquipmentsMenu> EQUIPMENTS =
			REGISTRATE.menu("equipments", EquipmentsMenu::fromNetwork, () -> EquipmentsScreen::new)
					.register();

	public static final MenuEntry<EntityCuriosListMenu> CURIOS =
			REGISTRATE.menu("curios", EntityCuriosListMenu::fromNetwork, () -> EntityCuriosListScreen::new)
					.register();

	public static final RegistryEntry<WrappedAttribute> ADD_LEVEL = L2DamageTracker.regWrapped(REGISTRATE,
			"extra_difficulty", 0, 0, 1000, "Extra Difficulty", L2DamageTracker.NEGATIVE);

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {

	}

}
