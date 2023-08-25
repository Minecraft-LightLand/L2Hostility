package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class DifficultyLevel {

	@SerialClass.SerialField
	private int level, exp;

	private int extraLevel;

	public static DifficultyLevel merge(DifficultyLevel difficulty, int extraLevel) {
		DifficultyLevel ans = new DifficultyLevel();
		ans.level = difficulty.level;
		ans.exp = difficulty.exp;
		ans.extraLevel = extraLevel;
		return ans;
	}

	public void grow(MobTraitCap cap) {
		exp += cap.getLevel() * cap.getLevel();
		int factor = LHConfig.COMMON.killPerLevel.get();
		while (exp >= level * level * factor) {
			exp -= level * level * factor;
			level++;
		}
	}

	public void decay() {
		level = Math.max(0, level - Math.max(1, (int) Math.ceil(level * (1 - LHConfig.COMMON.deathDecay.get()))));
		exp = 0;
	}

	public int getMaxExp() {
		int factor = LHConfig.COMMON.killPerLevel.get();
		return Math.max(1, level * level * factor);
	}

	public int getLevel() {
		return level + extraLevel;
	}

	public int getExp() {
		return exp;
	}

}
