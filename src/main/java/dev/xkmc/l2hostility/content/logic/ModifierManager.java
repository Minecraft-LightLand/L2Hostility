package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifierInstance;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2hostility.init.registrate.LHModifiers;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public class ModifierManager {

	public static void addAttribute(LivingEntity le, Attribute attr, String name, double factor) {
		var ins = le.getAttribute(attr);
		if (ins == null) return;
		ins.addPermanentModifier(new AttributeModifier(MathHelper.getUUIDFromString(name),
				name, factor, AttributeModifier.Operation.MULTIPLY_TOTAL));
	}

	public static void fill(LivingEntity le, int lv, ArrayList<MobModifierInstance> modifiers, int maxModLv) {
		// add attributes
		if (!le.getType().is(TagGen.NO_SCALING)) {
			addAttribute(le, Attributes.MAX_HEALTH, "hostility_health", lv * LHConfig.COMMON.healthFactor.get());
		}
		// add modifiers

		if (le.getType().is(TagGen.NO_MODIFIER)) return;

		List<MobModifier> list = new ArrayList<>(LHModifiers.MODIFIERS.get().getValues().stream().filter(e -> e.allow(le, lv)).toList());
		var rand = le.getRandom();
		int level = lv;
		while (level > 0) {
			if (list.size() == 0) break;
			MobModifier e = list.remove(rand.nextInt(list.size()));
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
			modifiers.add(new MobModifierInstance(e, maxLv));
		}
		for (var e : modifiers) {
			e.modifier().initialize(le, e.level());
		}
	}

	public static int getMaxLevel() {
		return 5;
	}

	public static int getModifierCap(int maxRankKilled, DifficultyLevel playerDifficulty) {
		return Math.max(maxRankKilled + 1, playerDifficulty.level / LHConfig.COMMON.modifierCapPerLevel.get());
	}

}
