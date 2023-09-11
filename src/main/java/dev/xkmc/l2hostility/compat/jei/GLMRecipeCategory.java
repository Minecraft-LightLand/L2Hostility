package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.loot.MobCapLootCondition;
import dev.xkmc.l2hostility.init.loot.TraitLootCondition;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(getTraits(recipe));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.result)
				.addTooltipCallback((v, l) -> addTooltip(v, l, recipe));
	}

	private List<ItemStack> getTraits(TraitLootModifier recipe) {
		Set<MobTrait> set = new LinkedHashSet<>();
		set.add(recipe.trait);
		for (var c : recipe.getConditions()) {
			if (c instanceof TraitLootCondition cl) {
				set.add(cl.trait);
			}
		}
		List<ItemStack> ans = new ArrayList<>();
		for (var e : set) {
			ans.add(e.asItem().getDefaultInstance());
		}
		return ans;
	}

	private void addTooltip(IRecipeSlotView view, List<Component> list, TraitLootModifier recipe) {
		int max = recipe.trait.getConfig().max_rank;
		int min = 1;
		int minLevel = 0;
		List<TraitLootCondition> other = new ArrayList<>();
		for (var c : recipe.getConditions()) {
			if (c instanceof TraitLootCondition cl) {
				if (cl.trait == recipe.trait) {
					max = Math.min(max, cl.maxLevel);
					min = Math.max(min, cl.minLevel);
				} else {
					other.add(cl);
				}
			} else if (c instanceof MobCapLootCondition cl) {
				minLevel = cl.minLevel;
			}
		}
		if (minLevel > 0) {
			list.add(LangData.LOOT_MIN_LEVEL.get(minLevel).withStyle(ChatFormatting.LIGHT_PURPLE));
		}
		for (int lv = min; lv <= max; lv++) {
			list.add(LangData.LOOT_CHANCE.get(Math.round((recipe.chance + recipe.rankBonus * lv) * 100), recipe.trait.getFullDesc(null), lv)
					.withStyle(ChatFormatting.AQUA));
		}
		for (var c : other) {
			int cmin = Math.max(c.minLevel, 1);
			int cmax = Math.min(c.maxLevel, c.trait.getMaxLevel());
			String str = cmax == cmin ?
					cmin + "" :
					cmax >= c.trait.getMaxLevel() ?
							cmin + "+" :
							cmin + "-" + cmax;
			list.add(LangData.LOOT_OTHER_TRAIT.get(c.trait.getFullDesc(null), str).withStyle(ChatFormatting.RED));
		}
	}

}
