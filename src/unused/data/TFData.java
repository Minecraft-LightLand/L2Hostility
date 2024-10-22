package dev.xkmc.l2hostility.compat.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.data.RecipeGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;
import static dev.xkmc.l2hostility.init.data.RecipeGen.getID;

public class TFData {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.NAGA_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.NAGA_TROPHY.get())
				.define('A', TFItems.NAGA_SCALE.get())
				.define('1', LHTraits.SPEEDY.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.NAGA_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.LICH_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.LICH_TROPHY.get())
				.define('A', Items.SKELETON_SKULL)
				.define('1', LHTraits.WEAKNESS.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.LICH_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.MINOSHROOM_TROPHY.get())
				.define('A', TFItems.RAW_MEEF.get())
				.define('1', LHTraits.TANK.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.ALPHA_YETI_TROPHY.get())
				.define('A', TFItems.ALPHA_YETI_FUR.get())
				.define('1', LHTraits.REGEN.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.HYDRA_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.HYDRA_TROPHY.get())
				.define('A', TFItems.FIERY_BLOOD.get())
				.define('1', LHTraits.SOUL_BURNER.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.HYDRA_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.KNIGHT_PHANTOM_TROPHY.get())
				.define('A', TFItems.KNIGHTMETAL_INGOT.get())
				.define('1', LHTraits.REFLECT.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.SNOW_QUEEN_TROPHY.get())
				.define('A', TFItems.ICE_BOMB.get())
				.define('1', LHTraits.FREEZING.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT)
				.define('T', TFItems.UR_GHAST_TROPHY.get())
				.define('A', TFItems.FIERY_TEARS.get())
				.define('1', LHTraits.WITHER.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						RecipeGen.getID(TFBlocks.UR_GHAST_BOSS_SPAWNER.get()));

	}

	public static void genConfig(ConfigDataProvider.Collector collector) {
		addEntity(collector, 50, 20, TFEntities.NAGA,
				EntityConfig.trait(LHTraits.SPEEDY.get(), 1, 2),
				EntityConfig.trait(LHTraits.CURSED.get(), 0, 1)
		);
		addEntity(collector, 100, 30, TFEntities.LICH,
				EntityConfig.trait(LHTraits.WEAKNESS.get(), 2, 3),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 100, 30, TFEntities.MINOSHROOM,
				EntityConfig.trait(LHTraits.TANK.get(), 1, 3),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 100, 30, TFEntities.ALPHA_YETI,
				EntityConfig.trait(LHTraits.REGEN.get(), 1, 2),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.HYDRA,
				EntityConfig.trait(LHTraits.SOUL_BURNER.get(), 1, 1),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.KNIGHT_PHANTOM,
				EntityConfig.trait(LHTraits.REFLECT.get(), 0, 1),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.SNOW_QUEEN,
				EntityConfig.trait(LHTraits.FREEZING.get(), 1, 1),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.UR_GHAST,
				EntityConfig.trait(LHTraits.WITHER.get(), 1, 1),
				EntityConfig.trait(LHTraits.CURSED.get(), 1, 1)
		);
	}

}
