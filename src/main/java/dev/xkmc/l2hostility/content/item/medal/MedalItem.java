package dev.xkmc.l2hostility.content.item.medal;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class MedalItem extends Item {

	public MedalItem(Properties properties) {
		super(properties.stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}



}
