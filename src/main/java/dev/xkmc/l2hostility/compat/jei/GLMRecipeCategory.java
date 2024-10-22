package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LangData;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class GLMRecipeCategory extends BaseRecipeCategory<ITraitLootRecipe, GLMRecipeCategory> {

	protected static final ResourceLocation BG = L2Complements.loc("textures/jei/background.png");

	public GLMRecipeCategory() {
		super(L2Hostility.loc("loot"), ITraitLootRecipe.class);
	}

	public GLMRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 18, 90, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.IRON_SWORD.getDefaultInstance());
		return this;
	}

	@Override
	public Component getTitle() {
		return LangData.LOOT_TITLE.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ITraitLootRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(recipe.getCurioRequired());
		builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addItemStacks(recipe.getInputs());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 1).addItemStacks(recipe.getResults())
				.addRichTooltipCallback((v, l) -> recipe.addTooltip(l::add));
	}

}
