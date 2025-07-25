package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TraitGenerator {

	public static void generateTraits(MobTraitCap cap, LivingEntity le, int lv, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		new TraitGenerator(cap, le, lv, traits, ins).generate();
	}

	private final LivingEntity entity;
	private final int mobLevel, maxTrait;
	private final MobDifficultyCollector ins;
	private final HashMap<MobTrait, Integer> traits;
	private final RandomSource rand;
	private final List<MobTrait> traitPool;
	private final boolean free;

	private int level, weights;

	private TraitGenerator(MobTraitCap cap, LivingEntity entity, int mobLevel, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		this.entity = entity;
		this.mobLevel = mobLevel;
		this.ins = ins;
		this.traits = traits;

		rand = entity.getRandom();
		level = mobLevel;
		free = ins.trait_cost < 0.01;

		var config = cap.getConfigCache(entity);
		int max = LHConfig.COMMON.maxTraitCount.get();
		if (config != null && config.maxTraitCount > 0)
			max = config.maxTraitCount;
		maxTrait = free ? -1 : (int) (max / ins.trait_cost);
		traitPool = new ArrayList<>(LHTraits.TRAITS.get().getValues().stream().filter(e ->
				(config == null || !config.blacklist().contains(e)) &&
						e.allow(entity, mobLevel, ins.getMaxTraitLevel())).toList());
		if (config != null) {
			for (var base : config.traits()) {
				if (base.condition() == null || base.condition().match(entity, mobLevel, ins))
					genBase(base);
			}
		}
		weights = 0;
		for (var e : traitPool) {
			weights += e.getConfig().weight;
		}

	}

	private int getRank(MobTrait e) {
		return traits.getOrDefault(e, 0);
	}

	private void setRank(MobTrait e, int rank) {
		if (rank == 0) {
			traits.remove(e);
		} else {
			traits.put(e, rank);
		}
	}

	private MobTrait pop() {
		int val = rand.nextInt(weights);
		MobTrait e = traitPool.get(0);
		for (var x : traitPool) {
			val -= x.getConfig().weight;
			if (val <= 0) {
				e = x;
				break;
			}
		}
		weights -= e.getConfig().weight;
		traitPool.remove(e);
		return e;
	}

	private void genBase(EntityConfig.TraitBase base) {
		MobTrait e = base.trait();
		if (e == null) return;
		int maxTrait = TraitManager.getMaxLevel() + 1;
		if (!e.allow(entity, mobLevel, maxTrait)) return;
		int max = e.getMaxLevel();// config bypass player trait cap
		int cost = e.getCost(ins.trait_cost);
		int old = Math.min(e.getMaxLevel(), Math.max(getRank(e), base.free()));
		int expected = Math.min(max, Math.max(old, base.min()));
		int rank = Math.min(expected, old + level / cost);
		setRank(e, Math.max(old, rank));
		if (rank > old) {
			level -= (rank - old) * cost;
		}
		if (base.cap()) {
			traitPool.remove(e);
		}
	}

	private void generate() {
		while (level > 0 && !traitPool.isEmpty()) {
			if (maxTrait > 0 && traits.size() >= maxTrait)
				break;
			MobTrait e = pop();
			int cost = e.getCost(ins.trait_cost);
			if (cost > level) {
				continue;
			}
			int max = Math.min(ins.getMaxTraitLevel(), e.getMaxLevel());
			int old = Math.min(e.getMaxLevel(), getRank(e));
			int rank = free ? max : Math.min(max, old + rand.nextInt(level / cost) + 1);
			if (rank <= old) {
				continue;
			}
			setRank(e, rank);
			level -= (rank - old) * cost;
			if (!ins.isFullChance() && rand.nextDouble() < ins.suppression()) {
				break;
			}
		}
		for (var e : traits.entrySet()) {
			e.getKey().initialize(entity, e.getValue());
		}
	}


}
