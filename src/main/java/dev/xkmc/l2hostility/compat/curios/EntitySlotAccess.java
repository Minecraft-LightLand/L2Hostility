package dev.xkmc.l2hostility.compat.curios;

import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public interface EntitySlotAccess {

	ItemStack get();

	void set(ItemStack stack);

	default ItemStack insert(ItemStack stack) {
		if (get().isEmpty()) {
			set(stack);
			return ItemStack.EMPTY;
		}
		return stack;
	}

	default void modify(Function<ItemStack, ItemStack> func) {
		set(func.apply(get()));
	}

	String getID();

}
