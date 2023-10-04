package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

import static dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;

public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {

		//misc
		{

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

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.DETECTOR_GLASSES.get(), 1)::unlockedBy, Items.ENDER_EYE)
					.pattern("ADA")
					.define('A', Items.ENDER_EYE)
					.define('D', Items.IRON_INGOT)
					.save(pvd);

			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, LHItems.BOTTLE_SANITY.get(), 3)::unlockedBy, LHItems.HOSTILITY_ORB.get())
					.requires(LHItems.HOSTILITY_ORB.get()).requires(Items.GLASS_BOTTLE, 3)
					.save(pvd, getID(LHItems.BOTTLE_SANITY.get(), "_craft"));

			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, LHItems.BOTTLE_SANITY.get(), 3)::unlockedBy, LCItems.LIFE_ESSENCE.get())
					.requires(LHItems.BOTTLE_SANITY.get()).requires(LCItems.LIFE_ESSENCE).requires(Items.GLASS_BOTTLE, 3)
					.save(pvd, getID(LHItems.BOTTLE_SANITY.get(), "_renew"));

			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, LHItems.BOTTLE_CURSE.get(), 3)::unlockedBy, LCItems.CURSED_DROPLET.get())
					.requires(LCItems.CURSED_DROPLET).requires(Items.GLASS_BOTTLE, 3)
					.save(pvd);

		}

		//curio
		{
			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CHAOS_INGOT.get(), 1)::unlockedBy, LHItems.HOSTILITY_ORB.get())
					.pattern("B4B").pattern("1A2").pattern("B3B")
					.define('A', LHItems.HOSTILITY_ORB.get())
					.define('B', LHItems.BOTTLE_CURSE.get())
					.define('1', LCItems.SOUL_FLAME.get())
					.define('2', LCItems.HARD_ICE.get())
					.define('3', LCItems.EXPLOSION_SHARD.get())
					.define('4', LCItems.CAPTURED_WIND.get())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_SLOTH.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CIC").pattern("BAB")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.BLACKSTONE_CORE.get())
					.define('1', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
					.define('B', Items.COPPER_INGOT)
					.define('C', LHItems.BOTTLE_SANITY.get())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_ENVY.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CIC").pattern("B2B")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1))
					.define('2', new EnchantmentIngredient(Enchantments.SILK_TOUCH, 1))
					.define('B', Items.PRISMARINE_SHARD)
					.define('C', Items.ENDER_EYE)
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_LUST.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CID").pattern("B2B")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1))
					.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
					.define('B', Items.PHANTOM_MEMBRANE)
					.define('C', LHTraits.REGEN.get().asItem())
					.define('D', LHTraits.INVISIBLE.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_GREED.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CID").pattern("B2B")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1))
					.define('2', new EnchantmentIngredient(Enchantments.BLOCK_FORTUNE, 1))
					.define('B', Items.GOLD_INGOT)
					.define('C', LHTraits.SPEEDY.get().asItem())
					.define('D', LHTraits.TANK.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_GLUTTONY.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CID").pattern("B2B")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1))
					.define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
					.define('B', Items.NETHERITE_INGOT)
					.define('C', LHTraits.CURSED.get().asItem())
					.define('D', LHTraits.WITHER.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_WRATH.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CID").pattern("B2B")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('1', LHTraits.FIERY.get().asItem())
					.define('2', LHTraits.SOUL_BURNER.get().asItem())
					.define('B', LCMats.SCULKIUM.getIngot())
					.define('C', LHTraits.ENDER.get().asItem())
					.define('D', LHTraits.REFLECT.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_PRIDE.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("B1B").pattern("CID").pattern("B2B")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('1', LHTraits.WEAKNESS.get().asItem())
					.define('2', LHTraits.PROTECTION.get().asItem())
					.define('B', LCMats.ETERNIUM.getIngot())
					.define('C', LHTraits.DEMENTOR.get().asItem())
					.define('D', LHTraits.ADAPTIVE.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_OCEAN.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("BAB").pattern("DID").pattern("BAB")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.GUARDIAN_EYE.get())
					.define('B', LCMats.POSEIDITE.getIngot())
					.define('D', LHTraits.CONFUSION.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_LIFE.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("BAB").pattern("DID").pattern("BAB")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LHTraits.UNDYING.get().asItem())
					.define('B', LCMats.SHULKERATE.getIngot())
					.define('D', LHTraits.REPELLING.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_DIVINITY.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("BAB").pattern("DID").pattern("BAB")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.LIFE_ESSENCE.get())
					.define('B', LCMats.TOTEMIC_GOLD.getIngot())
					.define('D', LHTraits.DISPELL.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_REFLECTION.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("1A2").pattern("DID").pattern("3A4")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.FORCE_FIELD.get())
					.define('1', LHTraits.POISON.get().asItem())
					.define('2', LHTraits.SLOWNESS.get().asItem())
					.define('3', LHTraits.CONFUSION.get().asItem())
					.define('4', LHTraits.BLIND.get().asItem())
					.define('D', LHTraits.REFLECT.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.FLAMING_THORN.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("BAB").pattern("DID").pattern("BAB")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.SOUL_FLAME.get())
					.define('B', LCItems.WARDEN_BONE_SHARD.get())
					.define('D', LHTraits.SOUL_BURNER.get().asItem())
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.WITCH_WAND.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("123").pattern("7I4").pattern("S65")
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('S', Items.STICK)
					.define('1', LHTraits.POISON.get().asItem())
					.define('2', LHTraits.WITHER.get().asItem())
					.define('3', LHTraits.SLOWNESS.get().asItem())
					.define('4', LHTraits.WEAKNESS.get().asItem())
					.define('5', LHTraits.LEVITATION.get().asItem())
					.define('6', LHTraits.FREEZING.get().asItem())
					.define('7', LHTraits.CURSED.get().asItem())
					.save(pvd);

			recycle(pvd, TagGen.CHAOS_CURIO, LHItems.CHAOS_INGOT.get(), 1f);

		}

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

	public static void recycle(RegistrateRecipeProvider pvd, TagKey<Item> source, Item result, float experience) {
		unlock(pvd, SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::unlockedBy, result)
				.save(pvd, getID(result, "_recycle"));
	}

}
