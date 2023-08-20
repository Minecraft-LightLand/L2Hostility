package dev.xkmc.l2hostility.compat;

import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.loot.TraitLootCondition;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class GLMRecipeCategory extends BaseRecipeCategory<TraitLootModifier, GLMRecipeCategory> {

	protected static final ResourceLocation BG = new ResourceLocation(L2Complements.MODID, "textures/jei/background.png");

	public GLMRecipeCategory() {
		super(new ResourceLocation(L2Hostility.MODID, "loot"), TraitLootModifier.class);
	}

	public GLMRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 0, 72, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.IRON_SWORD.getDefaultInstance());
		return this;
	}

	@Override
	public Component getTitle() {
		return LangData.LOOT_TITLE.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TraitLootModifier recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(Ingredient.of(recipe.trait.asItem()));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.result)
				.addTooltipCallback((v, l) -> addtooltip(v, l, recipe));
	}

	private void addtooltip(IRecipeSlotView view, List<Component> list, TraitLootModifier recipe) {
		int max = recipe.trait.getConfig().maxLevel;
		int min = 1;
		for (var c : recipe.getConditions()) {
			if (c instanceof TraitLootCondition cl && cl.trait == recipe.trait) {
				max = Math.min(max, cl.maxLevel);
				min = Math.max(min, cl.minLevel);
			}
		}
		for (int lv = min; lv <= max; lv++) {
			list.add(LangData.CHANCE.get(Math.round((recipe.chance + recipe.rankBonus * lv) * 100), lv));
		}
	}

}
