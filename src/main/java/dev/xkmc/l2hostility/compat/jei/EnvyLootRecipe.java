package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record EnvyLootRecipe() implements ITraitLootRecipe {

	@Override
	public List<ItemStack> getResults() {
		var opt = BuiltInRegistries.ITEM.getTag(LHTagGen.TRAIT_ITEM);
		if (opt.isEmpty()) return List.of();
		ArrayList<ItemStack> ans = new ArrayList<>();
		for (var e : opt.get()) {
			ans.add(e.value().getDefaultInstance());
		}
		return ans;
	}

	@Override
	public List<ItemStack> getCurioRequired() {
		return List.of(LHItems.CURSE_ENVY.asStack());
	}

	@Override
	public List<ItemStack> getInputs() {
		return List.of();
	}

	@Override
	public void addTooltip(Consumer<Component> l) {
		l.accept(LangData.TOOLTIP_JEI_ENVY.get().withStyle(ChatFormatting.YELLOW));
	}

}
