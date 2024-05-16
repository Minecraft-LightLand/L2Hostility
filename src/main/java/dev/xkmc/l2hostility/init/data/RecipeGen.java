package dev.xkmc.l2hostility.init.data;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.shadowsoffire.gateways.Gateways;
import dev.xkmc.l2complements.content.enchantment.core.EnchantmentRecipeBuilder;
import dev.xkmc.l2complements.content.recipe.BurntRecipeBuilder;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2hostility.compat.data.TFData;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;

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

			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, LHItems.BOOSTER_POTION.get(), 1)::unlockedBy, LHItems.WITCH_DROPLET.get())
					.requires(LHItems.WITCH_DROPLET).requires(LHItems.BOTTLE_SANITY).requires(LCItems.LIFE_ESSENCE)
					.save(pvd);

			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, LHItems.WITCH_CHARGE.get(), 1)::unlockedBy, LHItems.WITCH_DROPLET.get())
					.requires(LHItems.WITCH_DROPLET).requires(LCItems.CURSED_DROPLET).requires(Items.GUNPOWDER).requires(Items.BLAZE_POWDER)
					.save(pvd);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.ETERNAL_WITCH_CHARGE.get(), 1)::unlockedBy, LHItems.WITCH_DROPLET.get())
					.pattern("ABA").pattern("BCB").pattern("DBD")
					.define('A', Items.GUNPOWDER)
					.define('D', Items.BLAZE_POWDER)
					.define('B', LCItems.BLACKSTONE_CORE.get())
					.define('C', LHItems.WITCH_DROPLET)
					.save(pvd);

			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, LHItems.BOOK_OMNISCIENCE.get(), 1)::unlockedBy, LHItems.BOOK_COPY.get())
					.requires(LHItems.BOOK_COPY.get())
					.requires(LHTraits.REPRINT.get().asItem())
					.requires(LHTraits.SPLIT.get().asItem())
					.requires(Items.NETHER_STAR)
					.save(pvd);

			pvd.storage(LHItems.CHAOS_INGOT, RecipeCategory.MISC, LHBlocks.CHAOS);
			pvd.storage(LHItems.MIRACLE_INGOT, RecipeCategory.MISC, LHBlocks.MIRACLE);


			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHBlocks.HOSTILITY_BEACON.get(), 1)::unlockedBy, LHItems.HOSTILITY_ESSENCE.get())
					.pattern("E3E").pattern("1B1").pattern("C2C")
					.define('B', Items.BEACON)
					.define('C', Items.CRYING_OBSIDIAN)
					.define('E', LHItems.HOSTILITY_ESSENCE)
					.define('1', LHTraits.KILLER_AURA.get())
					.define('2', LHTraits.GRAVITY.get())
					.define('3', LHTraits.DRAIN.get())
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


			convert(pvd, LHItems.BOTTLE_CURSE.get(), LHItems.HOSTILITY_ESSENCE.get(), 512);
			recycle(pvd, LHTagGen.CHAOS_CURIO, LHItems.CHAOS_INGOT.get(), 1f);
			recycle(pvd, LHTagGen.TRAIT_ITEM, LHItems.MIRACLE_POWDER.get(), 1f);

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.MIRACLE_INGOT.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("ABA").pattern("ACA").pattern("ABA")
					.define('C', LHItems.CHAOS_INGOT.get())
					.define('B', LHItems.HOSTILITY_ESSENCE.get())
					.define('A', LHItems.MIRACLE_POWDER.get())
					.save(pvd);

			// loot
			{

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.LOOT_1.get(), 1)::unlockedBy, Items.EMERALD)
						.pattern(" A ").pattern("DID").pattern(" A ")
						.define('I', Items.EMERALD)
						.define('A', Items.GOLD_INGOT)
						.define('D', Items.COPPER_INGOT)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.LOOT_2.get(), 1)::unlockedBy, Items.DIAMOND)
						.pattern(" A ").pattern("DID").pattern(" A ")
						.define('I', Items.DIAMOND)
						.define('A', Items.BLAZE_POWDER)
						.define('D', Items.DRAGON_BREATH)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.LOOT_3.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern(" A ").pattern("DID").pattern(" A ")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('A', LCItems.LIFE_ESSENCE)
						.define('D', LHItems.WITCH_DROPLET)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.LOOT_4.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern(" A ").pattern("DID").pattern(" A ")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('A', LCItems.BLACKSTONE_CORE)
						.define('D', LCItems.FORCE_FIELD)
						.save(pvd);
			}

			// curse
			{
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
						.pattern("314").pattern("5I6").pattern("B2B")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('B', LHItems.HOSTILITY_ESSENCE.get())
						.define('1', LHTraits.FIERY.get().asItem())
						.define('2', LHTraits.REPRINT.get().asItem())
						.define('3', LHTraits.SHULKER.get().asItem())
						.define('4', LHTraits.GRENADE.get().asItem())
						.define('5', LHTraits.STRIKE.get().asItem())
						.define('6', LHTraits.REFLECT.get().asItem())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.CURSE_PRIDE.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern("515").pattern("3I4").pattern("B2B")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('B', LHItems.HOSTILITY_ESSENCE.get())
						.define('1', LHTraits.KILLER_AURA.get().asItem())
						.define('2', LHTraits.PROTECTION.get().asItem())
						.define('3', LHTraits.DEMENTOR.get().asItem())
						.define('4', LHTraits.ADAPTIVE.get().asItem())
						.define('5', LHTraits.GROWTH.get().asItem())
						.save(pvd);
			}

			// ring
			{
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

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_HEALING.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern("BAB").pattern("DID").pattern("BAB")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('A', Items.GHAST_TEAR)
						.define('B', LCMats.TOTEMIC_GOLD.getIngot())
						.define('D', LHTraits.REGEN.get().asItem())
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

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_CORROSION.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern("BAB").pattern("DID").pattern("BAB")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('A', LHTraits.CORROSION.get().asItem())
						.define('B', LCItems.CURSED_DROPLET.get())
						.define('D', LHTraits.EROSION.get().asItem())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RING_INCARCERATION.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern("BAB").pattern("1I2").pattern("BAB")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('1', LHTraits.SLOWNESS.get().asItem())
						.define('2', LHTraits.FREEZING.get().asItem())
						.define('A', LHTraits.KILLER_AURA.get().asItem())
						.define('B', LCItems.BLACKSTONE_CORE.get())
						.save(pvd);
			}

			// misc
			{
				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.FLAMING_THORN.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern("BAB").pattern("DID").pattern("BAB")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('A', LHTraits.DRAIN.get().asItem())
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

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.INFINITY_GLOVE.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern("BAB").pattern("III").pattern("DID")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('A', LHTraits.SPLIT.get().asItem())
						.define('B', LHTraits.ENDER.get().asItem())
						.define('D', LHTraits.PULLING.get().asItem())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.ODDEYES_GLASSES.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern(" A ").pattern("1I2")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('A', Items.GOLD_INGOT)
						.define('1', Items.CYAN_STAINED_GLASS_PANE)
						.define('2', Items.MAGENTA_STAINED_GLASS_PANE)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.TRIPLE_STRIP_CAPE.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
						.pattern(" I ").pattern("CCC").pattern("FFF")
						.define('I', LHItems.CHAOS_INGOT.get())
						.define('C', ItemTags.BANNERS)
						.define('F', LCItems.RESONANT_FEATHER.get())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.ABRAHADABRA.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("AIA").pattern("EOE").pattern("BIC")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('E', LCMats.ETERNIUM.getIngot())
						.define('O', LHItems.RING_REFLECTION.get())
						.define('A', LHTraits.RAGNAROK.get().asItem())
						.define('B', LHTraits.REPELLING.get().asItem())
						.define('C', LHTraits.PULLING.get().asItem())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.NIDHOGGUR.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("AIA").pattern("EOE").pattern("BIB")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('E', LCMats.ETERNIUM.getIngot())
						.define('O', LHItems.CURSE_GREED.get())
						.define('A', LHTraits.RAGNAROK.get().asItem())
						.define('B', LHTraits.PULLING.get().asItem())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.PLATINUM_STAR.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("BIB").pattern("ISI").pattern("BIB")
						.define('S', LHItems.MIRACLE_INGOT.get())
						.define('B', LHTraits.KILLER_AURA.get().asItem())
						.define('I', Items.NETHER_STAR)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.RESTORATION.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("BLB").pattern("SIS").pattern("BGB")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('B', LCItems.BLACKSTONE_CORE.get())
						.define('S', LHTraits.DISPELL.get().asItem())
						.define('L', LHTraits.MOONWALK.get().asItem())
						.define('G', LHTraits.GRAVITY.get().asItem())
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.ABYSSAL_THORN.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("AIA").pattern("IEI").pattern("XIX")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('E', LCMats.ETERNIUM.getIngot())
						.define('A', LCItems.GUARDIAN_EYE)
						.define('X', LCItems.BLACKSTONE_CORE)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.DIVINITY_CROSS.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("STS").pattern("TIT").pattern("ETA")
						.define('I', LHItems.MIRACLE_INGOT.get())
						.define('T', LCItems.LIFE_ESSENCE)
						.define('E', LHTraits.DRAIN.get().asItem())
						.define('A', LHTraits.KILLER_AURA.get().asItem())
						.define('S', LHItems.WITCH_DROPLET)
						.save(pvd);

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHItems.DIVINITY_LIGHT.get(), 1)::unlockedBy, LHItems.MIRACLE_INGOT.get())
						.pattern("STS").pattern("TIT").pattern("ETA")
						.define('T', LHItems.MIRACLE_INGOT.get())
						.define('I', LHItems.CURSE_SLOTH)
						.define('E', LHTraits.GRAVITY.get().asItem())
						.define('A', LHTraits.KILLER_AURA.get().asItem())
						.define('S', LHItems.HOSTILITY_ORB)
						.save(pvd);
			}

		}

		// ench
		{
			unlock(pvd, new EnchantmentRecipeBuilder(LHEnchantments.INSULATOR.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("AIA").pattern("DBD").pattern("ACA")
					.define('B', Items.BOOK)
					.define('D', Items.LAPIS_LAZULI)
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.FORCE_FIELD.get())
					.define('C', LHItems.BOTTLE_SANITY.get())
					.save(pvd);

			unlock(pvd, new EnchantmentRecipeBuilder(LHEnchantments.SPLIT_SUPPRESS.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
					.pattern("AIA").pattern("DBD").pattern("ACA")
					.define('B', Items.BOOK)
					.define('D', Items.LAPIS_LAZULI)
					.define('I', LHItems.CHAOS_INGOT.get())
					.define('A', LCItems.GUARDIAN_EYE.get())
					.define('C', LHItems.BOTTLE_SANITY.get())
					.save(pvd);
		}

		// compat
		if (ModList.get().isLoaded(TwilightForestMod.ID)) {
			TFData.genRecipe(pvd);
		}
		if (ModList.get().isLoaded(Cataclysm.MODID)) {
			CataclysmData.genRecipe(pvd);
		}
		if (ModList.get().isLoaded(Gateways.MODID)){
			GatewayConfigGen.genRecipe(pvd);
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
	public static ResourceLocation getID(ItemLike item) {
		return new ResourceLocation(L2Hostility.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item.asItem()).getPath());
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

	private static void convert(RegistrateRecipeProvider pvd, Item in, Item out, int count) {
		unlock(pvd, new BurntRecipeBuilder(Ingredient.of(in), out.getDefaultInstance(), count)::unlockedBy, in).save(pvd, getID(out));
	}

	public static void recycle(RegistrateRecipeProvider pvd, TagKey<Item> source, Item result, float experience) {
		unlock(pvd, SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::unlockedBy, result)
				.save(pvd, getID(result, "_recycle"));
	}

}
