package dev.xkmc.l2hostility.init.registrate;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2core.init.reg.ench.EnchColor;
import dev.xkmc.l2core.init.reg.ench.EnchReg;
import dev.xkmc.l2core.init.reg.ench.EnchVal;
import dev.xkmc.l2hostility.content.enchantments.RemoveTraitEnchantment;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;

public class LHEnchantments {

	private static final EnchReg DC = EnchReg.of(L2Hostility.REG, L2Hostility.REGISTRATE);
	public static final EnchVal INSULATOR, VANISH;

	public static final EnchVal.Legacy<RemoveTraitEnchantment> SPLIT_SUPPRESS;

	static {
		EnchColor green = new EnchColor(ChatFormatting.GREEN, ChatFormatting.GRAY);
		EnchColor red = new EnchColor(ChatFormatting.RED, ChatFormatting.GRAY);
		LCEnchantments.Order order = new LCEnchantments.Order();

		int armor = 0xff4fbfff;
		int weapon = 0xffff4f4f;
		int curse = 16733525;

		INSULATOR = DC.ench("insulator", "Insulator",
				"Reduce trait effects that pulls or pushes you",
				e -> e.maxLevel(3).group(EquipmentSlotGroup.ARMOR).items(ItemTags.ARMOR_ENCHANTABLE)
						.color(green).special(LCEnchantments.CRAFT, order.of(armor)));

		SPLIT_SUPPRESS = DC.enchLegacy("split_suppressor", "Split Suppressor",
				"Disable Split trait on enemies on hit",
				e -> e.group(EquipmentSlotGroup.MAINHAND).items(ItemTags.WEAPON_ENCHANTABLE)
						.color(green).special(LCEnchantments.CRAFT, order.of(weapon)),
				() -> new RemoveTraitEnchantment(LHTraits.SPLIT));

		VANISH = DC.ench("vanish", "Vanish",
				"This item vanishes when on ground or in hand of survival / adventure player",
				e -> e.color(red).special(LCEnchantments.CRAFT, order.of(curse)));

	}

	public static void register() {

	}

}
