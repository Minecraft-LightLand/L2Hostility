package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

import static dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;

public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHBlocks.BURST_SPAWNER.get(), 1)::unlockedBy, Items.NETHER_STAR)
				.pattern("ADA").pattern("BCB").pattern("ABA")
				.define('C', Items.NETHER_STAR)
				.define('B', LCItems.EXPLOSION_SHARD)
				.define('A', Items.NETHERITE_INGOT)
				.define('D', LCItems.CURSED_DROPLET)
				.save(pvd);

		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.DETECTOR.get(), 1)::unlockedBy, Items.LIGHTNING_ROD)
				.pattern("ADA").pattern("BCB").pattern("ABA")
				.define('A', Items.ROTTEN_FLESH)
				.define('B', Items.BONE)
				.define('C', Items.IRON_INGOT)
				.define('D', Items.LIGHTNING_ROD)
				.save(pvd);
	}

	@SuppressWarnings("ConstantConditions")
	private static ResourceLocation getID(Enchantment item) {
		return new ResourceLocation(L2Hostility.MODID, currentFolder + ForgeRegistries.ENCHANTMENTS.getKey(item).getPath());
	}

	@SuppressWarnings("ConstantConditions")
	private static ResourceLocation getID(Enchantment item, String suffix) {
		return new ResourceLocation(L2Hostility.MODID, currentFolder + ForgeRegistries.ENCHANTMENTS.getKey(item).getPath() + suffix);
	}

	@SuppressWarnings("ConstantConditions")
	private static ResourceLocation getID(Item item) {
		return new ResourceLocation(L2Hostility.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath());
	}

	@SuppressWarnings("ConstantConditions")
	private static ResourceLocation getID(Item item, String suffix) {
		return new ResourceLocation(L2Hostility.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
	}

	private static void storage(RegistrateRecipeProvider pvd, ItemEntry<?> nugget, ItemEntry<?> ingot, BlockEntry<?> block) {
		storage(pvd, nugget::get, ingot::get);
		storage(pvd, ingot::get, block::get);
	}

	public static void storage(RegistrateRecipeProvider pvd, NonNullSupplier<ItemLike> from, NonNullSupplier<ItemLike> to) {
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, to.get(), 1)::unlockedBy, from.get().asItem())
				.pattern("XXX").pattern("XXX").pattern("XXX").define('X', from.get())
				.save(pvd, getID(to.get().asItem()) + "_storage");
		unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, from.get(), 9)::unlockedBy, to.get().asItem())
				.requires(to.get()).save(pvd, getID(to.get().asItem()) + "_unpack");
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

	public static void smithing(RegistrateRecipeProvider pvd, TagKey<Item> in, Item mat, Item out) {
		unlock(pvd, SmithingTransformRecipeBuilder.smithing(TEMPLATE_PLACEHOLDER, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.MISC, out)::unlocks, mat).save(pvd, getID(out));
	}

	public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
		unlock(pvd, SmithingTransformRecipeBuilder.smithing(TEMPLATE_PLACEHOLDER, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.MISC, out)::unlocks, mat).save(pvd, getID(out));
	}

	public static void smelting(RegistrateRecipeProvider pvd, Item source, Item result, float experience) {
		unlock(pvd, SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::unlockedBy, source)
				.save(pvd, getID(source));
	}

	public static void blasting(RegistrateRecipeProvider pvd, Item source, Item result, float experience) {
		unlock(pvd, SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::unlockedBy, source)
				.save(pvd, getID(source));
	}

}
