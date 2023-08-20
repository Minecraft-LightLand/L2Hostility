package dev.xkmc.l2hostility.compat;


import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin {

	public static final ResourceLocation ID = new ResourceLocation(L2Hostility.MODID, "main");

	public final GLMRecipeCategory LOOT = new GLMRecipeCategory();


	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(LOOT.init(helper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(LOOT.getRecipeType(), LootDataToClient.LIST_CACHE);
	}

}
