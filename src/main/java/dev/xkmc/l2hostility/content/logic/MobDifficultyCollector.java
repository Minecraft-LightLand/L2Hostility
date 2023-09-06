package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MobDifficultyCollector {

	public int min, base, count, difficulty, cap = Integer.MAX_VALUE, traitCap = TraitManager.getMaxLevel() + 1;
	public double scale, varSq, apply_chance, trait_chance;

	private boolean fullChance = false;

	public MobDifficultyCollector() {
		apply_chance = LHConfig.COMMON.globalApplyChance.get();
		trait_chance = LHConfig.COMMON.globalTraitChance.get();
	}

	public void acceptConfig(WorldDifficultyConfig.DifficultyConfig config) {
		min = Math.max(min, config.min());
		base += config.base();
		scale += config.scale();
		varSq += config.variation() * config.variation();
		count++;
		apply_chance *= config.apply_chance();
		trait_chance *= config.trait_chance();
	}

	public void acceptBonus(DifficultyLevel difficulty) {
		this.difficulty += difficulty.getLevel();
	}


	public void acceptBonusLevel(int difficulty) {
		this.base += difficulty;
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

	public double apply_chance() {
		return fullChance ? 1 : apply_chance;
	}

	public double trait_chance() {
		return fullChance ? 1 : trait_chance;
	}

	public int getBase() {
		return (int) Math.round(base + difficulty * scale);
	}

	public void setFullChance() {
		fullChance = true;
	}

	public boolean isFullChance() {
		return fullChance;
	}

}
