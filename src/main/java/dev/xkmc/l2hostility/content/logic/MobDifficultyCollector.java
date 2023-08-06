package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MobDifficultyCollector {

	public int min, base, count, difficulty, cap = Integer.MAX_VALUE, traitCap = TraitManager.getMaxLevel();
	public double scale, varSq;

	public void acceptConfig(WorldDifficultyConfig.DifficultyConfig config) {
		min = Math.max(min, config.min());
		base += config.base();
		scale += config.scale();
		varSq += config.variation() * config.variation();
		count++;
	}

	public void acceptBonus(DifficultyLevel difficulty) {
		this.difficulty += difficulty.getLevel();
	}


	public void acceptBonusLevel(int difficulty) {
		this.difficulty += difficulty;
	}

	public void setCap(int cap) {
		this.cap = Math.min(this.cap, Math.max(min, cap));
	}

	public int getDifficulty(RandomSource random) {
		double mean = base + difficulty * scale;
		if (count > 0) {
			mean += random.nextGaussian() * Math.sqrt(varSq / count);
		}
		return Math.round((int) Mth.clamp(mean, min, cap));
	}

	public void setTraitCap(int cap) {
		traitCap = Math.min(cap, traitCap);
	}

	public int getMaxTraitLevel() {
		return traitCap;
	}

}
