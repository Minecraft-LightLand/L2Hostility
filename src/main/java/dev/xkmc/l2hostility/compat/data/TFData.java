package dev.xkmc.l2hostility.compat.data;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.data.RecipeGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.network.BaseConfig;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

import java.util.Map;

import static dev.xkmc.l2hostility.init.data.LHConfigGen.addEntity;
import static dev.xkmc.l2hostility.init.data.RecipeGen.getID;

public class TFData {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.NAGA_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.NAGA_TROPHY.get())
				.define('A', TFItems.NAGA_SCALE.get())
				.define('1', LHTraits.SPEEDY.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.NAGA_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.LICH_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.LICH_TROPHY.get())
				.define('A', Items.SKELETON_SKULL)
				.define('1', LHTraits.WEAKNESS.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.LICH_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.MINOSHROOM_TROPHY.get())
				.define('A', TFItems.RAW_MEEF.get())
				.define('1', LHTraits.TANK.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.ALPHA_YETI_TROPHY.get())
				.define('A', TFItems.ALPHA_YETI_FUR.get())
				.define('1', LHTraits.REGEN.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.HYDRA_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.HYDRA_TROPHY.get())
				.define('A', TFItems.FIERY_BLOOD.get())
				.define('1', LHTraits.SOUL_BURNER.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.HYDRA_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.KNIGHT_PHANTOM_TROPHY.get())
				.define('A', TFItems.KNIGHTMETAL_INGOT.get())
				.define('1', LHTraits.REFLECT.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.SNOW_QUEEN_TROPHY.get())
				.define('A', TFItems.ICE_BOMB.get())
				.define('1', LHTraits.FREEZING.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get()));

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), 4)::unlockedBy,
						LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA")
				.define('I', LHItems.CHAOS_INGOT.get())
				.define('T', TFBlocks.UR_GHAST_TROPHY.get())
				.define('A', TFItems.FIERY_TEARS.get())
				.define('1', LHTraits.WITHER.get().asItem())
				.define('2', LHTraits.CURSED.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, TwilightForestMod.ID),
						getID(TFBlocks.UR_GHAST_BOSS_SPAWNER.get()));

	}

	public static void genConfig(Map<String, BaseConfig> collector) {
		addEntity(collector, 50, 20, TFEntities.NAGA,
				new EntityConfig.TraitBase(LHTraits.SPEEDY.get(), 1, 2),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 0, 1)
		);
		addEntity(collector, 100, 30, TFEntities.LICH,
				new EntityConfig.TraitBase(LHTraits.WEAKNESS.get(), 2, 3),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 100, 30, TFEntities.MINOSHROOM,
				new EntityConfig.TraitBase(LHTraits.TANK.get(), 1, 3),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 100, 30, TFEntities.ALPHA_YETI,
				new EntityConfig.TraitBase(LHTraits.REGEN.get(), 1, 2),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.HYDRA,
				new EntityConfig.TraitBase(LHTraits.SOUL_BURNER.get(), 1, 1),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.KNIGHT_PHANTOM,
				new EntityConfig.TraitBase(LHTraits.REFLECT.get(), 0, 1),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.SNOW_QUEEN,
				new EntityConfig.TraitBase(LHTraits.FREEZING.get(), 1, 1),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
		addEntity(collector, 150, 50, TFEntities.UR_GHAST,
				new EntityConfig.TraitBase(LHTraits.WITHER.get(), 1, 1),
				new EntityConfig.TraitBase(LHTraits.CURSED.get(), 1, 1)
		);
	}

}
