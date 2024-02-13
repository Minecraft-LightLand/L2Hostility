package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public record EnvyLootRecipe() implements ITraitLootRecipe {

	@Override
	public List<ItemStack> getResults() {
		var manager = ForgeRegistries.ITEMS.tags();
		if (manager == null) return List.of();
		ArrayList<ItemStack> ans = new ArrayList<>();
		for (var e : manager.getTag(LHTagGen.TRAIT_ITEM)) {
			ans.add(e.getDefaultInstance());
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
	public void addTooltip(List<Component> l) {
		l.add(LangData.TOOLTIP_JEI_ENVY.get().withStyle(ChatFormatting.YELLOW));
	}

}
