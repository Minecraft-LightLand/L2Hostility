package dev.xkmc.l2complements.content.logic;

import dev.xkmc.l2complements.content.capability.mob.MobModifierCap;
import dev.xkmc.l2complements.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class DifficultyLevel {

	@SerialClass.SerialField
	public int level;

	@SerialClass.SerialField
	private int exp;

	public void grow(MobModifierCap cap) {
		exp += cap.getLevel() * cap.getLevel();
		int factor = LHConfig.COMMON.killPerLevel.get();
		while (exp >= level * level * factor) {
			exp -= level * level * factor;
			level++;
		}
	}

	public void decay() {
		level *= LHConfig.COMMON.deathDecay.get();
		exp = 0;
	}

}
