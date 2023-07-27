package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.builders.EnchantmentBuilder;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static dev.xkmc.l2hostility.init.L2Hostility.REGISTRATE;

public class LHEnchantments {

	private static <T extends Enchantment> EnchantmentBuilder<T, L2Registrate> reg(
			String id, EnchantmentCategory category,
			EnchantmentBuilder.EnchantmentFactory<T> fac, String desc
	) {
		return REGISTRATE.enchantment(id, category, fac, desc);
	}

	public static void register() {
	}

}
