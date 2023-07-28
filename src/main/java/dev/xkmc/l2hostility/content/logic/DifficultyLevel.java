package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class DifficultyLevel {

	@SerialClass.SerialField
	public int level, exp;

	public void grow(MobTraitCap cap) {
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

	public int getMaxExp() {
		int factor = LHConfig.COMMON.killPerLevel.get();
		return level * level * factor;
	}
}
