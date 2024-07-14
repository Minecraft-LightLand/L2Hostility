package dev.xkmc.l2hostility.content.item.curio.core;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class SingletonItem extends CurioItem implements ICurioItem {

	public SingletonItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		var repeat = CuriosApi.getCuriosInventory(slotContext.entity()).resolve()
				.flatMap(e -> e.findFirstCurio(this));
		if (repeat.isEmpty()) return true;
		var rep = repeat.get().slotContext();
		return rep.identifier().equals(slotContext.identifier()) && rep.index() == slotContext.index();
	}

}
