package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static void fill(LivingEntity le, int lv, HashMap<MobTrait, Integer> traits, int maxModLv) {
		// add attributes
		if (!le.getType().is(TagGen.NO_SCALING)) {
			addAttribute(le, Attributes.MAX_HEALTH, "hostility_health",
					lv * LHConfig.COMMON.healthFactor.get(),
					AttributeModifier.Operation.MULTIPLY_TOTAL);
		}
		// add traits
		if (!le.getType().is(TagGen.NO_TRAIT)) {
			List<MobTrait> list = new ArrayList<>(LHTraits.TRAITS.get().getValues().stream().filter(e -> e.allow(le, lv)).toList());
			var rand = le.getRandom();
			int level = lv;
			while (level > 0) {
				if (list.size() == 0) break;
				MobTrait e = list.remove(rand.nextInt(list.size()));
				int cost = e.getCost();
				if (cost == 0) {
					level--;
					continue;
				}
				int maxLv = Math.min(Math.min(maxModLv, level / cost), e.getMaxLevel());
				if (maxLv == 0) {
					level--;
					continue;
				}
				level -= maxLv * cost;
				traits.put(e, maxLv);
			}
			for (var e : traits.entrySet()) {
				e.getKey().initialize(le, e.getValue());
			}
		}
		le.setHealth(le.getMaxHealth());
	}

	public static int getMaxLevel() {
		return 5;
	}

	public static int getTraitCap(int maxRankKilled, DifficultyLevel playerDifficulty) {
		return Math.max(maxRankKilled + 1, playerDifficulty.level / LHConfig.COMMON.traitCapPerLevel.get());
	}

}
