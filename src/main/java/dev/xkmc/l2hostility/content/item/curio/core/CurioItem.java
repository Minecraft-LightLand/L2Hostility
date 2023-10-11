package dev.xkmc.l2hostility.content.item.curio.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CurioItem extends Item {

	public CurioItem(Properties properties) {
		super(properties.stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

}
