package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class DifficultyLevel {

	@SerialClass.SerialField
	protected int level, exp;

	@SerialClass.SerialField
	protected int extraLevel;

	public static DifficultyLevel merge(DifficultyLevel difficulty, int extraLevel) {
		DifficultyLevel ans = new DifficultyLevel();
		ans.level = difficulty.level;
		ans.exp = difficulty.exp;
		ans.extraLevel = difficulty.extraLevel + extraLevel;
		return ans;
	}

	public void grow(double growFactor, MobTraitCap cap) {
		exp += growFactor * cap.getLevel() * cap.getLevel();
		int factor = LHConfig.COMMON.killsPerLevel.get();
		while (exp >= level * level * factor) {
			exp -= level * level * factor;
			level++;
		}
	}

	public void decay() {
		level = Math.max(0, level - Math.max(1, (int) Math.ceil(level * (1 - LHConfig.COMMON.playerDeathDecay.get()))));
		exp = 0;
	}

	public int getMaxExp() {
		int factor = LHConfig.COMMON.killsPerLevel.get();
		return Math.max(1, level * level * factor);
	}

	public int getLevel() {
		return Math.max(0, level + extraLevel);
	}

	public int getExp() {
		return exp;
	}

}
