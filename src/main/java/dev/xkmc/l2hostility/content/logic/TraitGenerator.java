package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TraitGenerator {

	public static void generateTraits(LivingEntity le, int lv, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		new TraitGenerator(le, lv, traits, ins).generate();
	}

	private final LivingEntity entity;
	private final int mobLevel;
	private final MobDifficultyCollector ins;
	private final HashMap<MobTrait, Integer> traits;
	private final RandomSource rand;
	private final List<MobTrait> traitPool;

	private int level, weights;

	private TraitGenerator(LivingEntity entity, int mobLevel, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		this.entity = entity;
		this.mobLevel = mobLevel;
		this.ins = ins;
		this.traits = traits;

		rand = entity.getRandom();
		level = mobLevel;

		var config = L2Hostility.ENTITY.getMerged().get(entity.getType());
		if (config != null) {
			for (var base : config.traits()) {
				genBase(base);
			}
		}

		traitPool = new ArrayList<>(LHTraits.TRAITS.get().getValues().stream().filter(e ->
				!traits.containsKey(e) && e.allow(entity, mobLevel, ins.getMaxTraitLevel())).toList());
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
		}
		traits.put(e, rank);
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
	}

	private void generate() {
		while (level > 0 && !traitPool.isEmpty()) {
			MobTrait e = pop();
			int cost = e.getCost(ins.trait_cost);
			if (cost > level) {
				continue;
			}
			int max = Math.min(ins.getMaxTraitLevel(), e.getMaxLevel());
			int old = Math.min(e.getMaxLevel(), getRank(e));
			int rank = Math.min(max, old + rand.nextInt(level / cost) + 1);
			if (rank <= old) {
				continue;
			}
			setRank(e, rank);
			level -= (rank - old) * cost;
			if (!ins.isFullChance() && rand.nextDouble() < LHConfig.COMMON.globalTraitSuppression.get()) {
				break;
			}
		}
		for (var e : traits.entrySet()) {
			e.getKey().initialize(entity, e.getValue());
		}
	}


}
