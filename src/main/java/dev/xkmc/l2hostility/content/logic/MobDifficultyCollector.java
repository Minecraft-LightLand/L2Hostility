package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class MobDifficultyCollector {

	public static MobDifficultyCollector noTrait(int lv) {
		var ans = new MobDifficultyCollector();
		ans.trait_chance = 0;
		ans.base = lv;
		return ans;
	}

	public int min, base, count, difficulty, cap = Integer.MAX_VALUE, traitCap = TraitManager.getMaxLevel() + 1;
	public double scale, varSq, apply_chance, trait_chance, trait_cost, finalFactor = 1;

	private ServerPlayer player;
	private boolean fullChance, fullDrop, delegateTrait;

	public MobDifficultyCollector() {
		apply_chance = LHConfig.COMMON.globalApplyChance.get();
		trait_chance = LHConfig.COMMON.globalTraitChance.get();
		trait_cost = 1;
	}

	public void acceptConfig(WorldDifficultyConfig.DifficultyConfig config) {
		min = Math.max(min, config.min());
		base += config.base();
		scale += config.scale();
		varSq += config.variation() * config.variation();
		count++;
		apply_chance *= config.apply_chance();
		trait_chance *= config.trait_chance();
		fullChance |= min > 0;
	}

	public void acceptBonus(DifficultyLevel difficulty) {
		this.difficulty += difficulty.getLevel();
	}

	public void acceptBonusLevel(int difficulty) {
		this.base += difficulty;
	}

	public void acceptBonusFactor(double finalFactor) {
		this.finalFactor *= finalFactor;
	}

	public void traitCostFactor(double factor) {
		trait_cost *= factor;
	}

	public void setCap(int cap) {
		if (LHConfig.COMMON.allowBypassMinimum.get()) {
			this.min = Math.min(this.min, cap);
		} else {
			cap = Math.max(this.min, cap);
		}
		this.cap = Math.min(this.cap, cap);
	}

	public int getDifficulty(RandomSource random) {
		double mean = base + difficulty * scale;
		if (count > 0) {
			mean += random.nextGaussian() * Math.sqrt(varSq);
		}
		mean *= finalFactor;
		return (int) Math.round(Mth.clamp(mean, min, cap));
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

	public double trait_chance(int lv) {
		if (delegateTrait) return 0;
		return fullChance ? 1 : trait_chance * Math.min(1, lv * LHConfig.COMMON.initialTraitChanceSlope.get());
	}

	public int getBase() {
		return (int) Math.round(base + difficulty * scale);
	}

	public void setFullChance() {
		fullChance = true;
	}

	public void delegateTrait(){
		delegateTrait = true;
	}

	public boolean isFullChance() {
		return fullChance;
	}

	public void setFullDrop() {
		fullDrop = true;
	}

	public boolean isFullDrop() {
		return fullDrop;
	}

	public void setPlayer(Player player) {
		this.player = player instanceof ServerPlayer sp ? sp : null;
	}

	public boolean hasAdvancement(ResourceLocation id) {
		if (player == null) return true;
		var adv = player.server.getAdvancements().getAdvancement(id);
		if (adv == null) return false;
		var prog = player.getAdvancements().getOrStartProgress(adv);
		return prog.isDone();
	}

}
