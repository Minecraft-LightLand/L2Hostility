package dev.xkmc.l2hostility.content.item.curio.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class SingletonItem extends Item implements ICurioItem {

	public SingletonItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	public SingletonItem(Properties properties, int durability) {
		super(properties.durability(durability));
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		var repeat = CuriosApi.getCuriosInventory(slotContext.entity())
				.flatMap(e -> e.findFirstCurio(this));
		if (repeat.isEmpty()) return true;
		var rep = repeat.get().slotContext();
		return rep.identifier().equals(slotContext.identifier()) && rep.index() == slotContext.index();
	}

}
