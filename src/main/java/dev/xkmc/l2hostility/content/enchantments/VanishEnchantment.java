package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Set;

public class VanishEnchantment extends UnobtainableEnchantment {

	public VanishEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	public ChatFormatting getColor() {
		return ChatFormatting.RED;
	}

	@Override
	public Set<Integer> getCraftableLevels() {
		return Set.of();
	}
}
