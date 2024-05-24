package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class TraitManager {

	public static void addAttribute(LivingEntity le, Attribute attr, String name, double factor, AttributeModifier.Operation op) {
		var ins = le.getAttribute(attr);
		if (ins == null) return;
		var modifier = new AttributeModifier(MathHelper.getUUIDFromString(name), name, factor, op);
		if (ins.hasModifier(modifier)) {
			ins.removeModifier(modifier.getId());
		}
		ins.addPermanentModifier(modifier);
	}

	public static void scale(LivingEntity le, int lv) {
		if (!le.getType().is(LHTagGen.NO_SCALING)) {
			double factor;
			if (LHConfig.COMMON.exponentialHealth.get()) {
				factor = Math.pow(1 + LHConfig.COMMON.healthFactor.get(), lv) - 1;
			} else {
				factor = lv * LHConfig.COMMON.healthFactor.get();
			}
			addAttribute(le, Attributes.MAX_HEALTH, "hostility_health", factor,
					AttributeModifier.Operation.MULTIPLY_TOTAL);
		}
	}

	public static int fill(MobTraitCap cap, LivingEntity le, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		int lv = ins.getDifficulty(le.getRandom());
		int ans = 0;
		if (ins.apply_chance() < le.getRandom().nextDouble()) {
			return ans;
		}
		// add attributes
		if (!le.getType().is(LHTagGen.NO_SCALING)) {
			scale(le, lv);
			ans = lv;
		}
		// armor
		if (le.getType().is(LHTagGen.ARMOR_TARGET)) {
			ItemPopulator.populateArmors(le, lv);
		}
		// add traits

		if (ins.trait_chance(lv) >= le.getRandom().nextDouble()) {
			if (!le.getType().is(LHTagGen.NO_TRAIT)) {
				TraitGenerator.generateTraits(cap, le, lv, traits, ins);
			}
			ans = lv;
		}
		le.setHealth(le.getMaxHealth());
		return ans;
	}

	public static int getMaxLevel() {
		return 5;
	}

	public static int getTraitCap(int maxRankKilled, DifficultyLevel diff) {
		return maxRankKilled + 1;
	}

}
