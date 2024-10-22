package dev.xkmc.l2hostility.compat.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface ITraitLootRecipe {

	List<ItemStack> getResults();

	List<ItemStack> getCurioRequired();

	List<ItemStack> getInputs();


	void addTooltip(Consumer<Component> l);
}
