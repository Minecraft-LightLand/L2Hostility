package dev.xkmc.l2complements.content.logic;

import dev.xkmc.l2complements.content.config.WorldDifficultyConfig;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class DifficultyInstance {

	private int base, count, difficulty, cap = Integer.MAX_VALUE;
	private double scale, varSq;

	public void acceptConfig(WorldDifficultyConfig.DifficultyConfig config) {
		base += config.base();
		scale += config.scale();
		varSq += config.variation() * config.variation();
		count++;
	}

	public void acceptBonus(int difficulty) {
		this.difficulty += difficulty;
	}

	public void setCap(int cap) {
		this.cap = Math.min(this.cap, cap);
	}

	public int getDifficulty(RandomSource random) {
		double mean = base + difficulty * scale;
		if (count > 0) {
			mean += random.nextGaussian() * Math.sqrt(varSq / count);
		}
		return Math.round((int) Mth.clamp(mean, 0, cap));
	}

}
