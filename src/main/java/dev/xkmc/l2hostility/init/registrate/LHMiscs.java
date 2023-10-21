package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2hostility.content.entity.BulletRenderer;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import dev.xkmc.l2hostility.content.menu.equipments.EquipmentsMenu;
import dev.xkmc.l2hostility.content.menu.equipments.EquipmentsScreen;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2hostility.init.L2Hostility.REGISTRATE;

public class LHMiscs {

	public static final MenuEntry<EquipmentsMenu> EQUIPMENTS =
			REGISTRATE.menu("equipments", EquipmentsMenu::fromNetwork, () -> EquipmentsScreen::new)
					.register();

	public static final EntityEntry<HostilityBullet> BULLET =
			REGISTRATE.<HostilityBullet>entity("hostility_bullet", HostilityBullet::new, MobCategory.MISC)
					.properties(e -> e.sized(0.3125F, 0.3125F).clientTrackingRange(8))
					.renderer(() -> BulletRenderer::new)
					.register();

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {

	}

}
