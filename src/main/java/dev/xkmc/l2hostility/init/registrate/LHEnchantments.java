package dev.xkmc.l2hostility.init.registrate;


import dev.xkmc.l2hostility.content.enchantments.HostilityEnchantment;
import dev.xkmc.l2hostility.content.enchantments.RemoveTraitEnchantment;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.EnchantmentBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LHEnchantments {

	public static final RegistryEntry<HostilityEnchantment> INSULATOR;
	public static final RegistryEntry<RemoveTraitEnchantment> SPLIT_SUPPRESS;

	static {
		INSULATOR = reg("insulator", EnchantmentCategory.ARMOR, (r, t, s) ->
						new HostilityEnchantment(r, t, s, 3),
				"Reduce trait effects that pulls or pushes you")
				.addArmorSlots().register();

		SPLIT_SUPPRESS = reg("split_suppressor", EnchantmentCategory.WEAPON, (r, t, s) ->
						new RemoveTraitEnchantment(r, t, s, LHTraits.SPLIT::get),
				"Disable Split trait on enemies on hit").addSlots(EquipmentSlot.MAINHAND).register();
	}

	private static <T extends HostilityEnchantment> EnchantmentBuilder<T, L2Registrate> reg(
			String id, EnchantmentCategory category,
			EnchantmentBuilder.EnchantmentFactory<T> fac, String desc
	) {
		L2Hostility.REGISTRATE.addRawLang("enchantment." + L2Hostility.MODID + "." + id + ".desc", desc);
		return L2Hostility.REGISTRATE.enchantment(id, category, fac);
	}

	public static void register() {

	}

}
