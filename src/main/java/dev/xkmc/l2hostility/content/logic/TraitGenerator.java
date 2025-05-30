package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
	private final RegistryAccess access;
	private final TraitPool pool;

	private int level;

	private TraitGenerator(MobTraitCap cap, LivingEntity entity, int mobLevel, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		this.entity = entity;
		this.mobLevel = mobLevel;
		this.ins = ins;
		this.traits = traits;
		this.access = entity.level().registryAccess();

		rand = entity.getRandom();
		level = mobLevel;

		var config = cap.getConfigCache(entity);
		int max = LHConfig.SERVER.maxTraitCount.get();
		if (config != null) {
			if (config.maxTraitCount > 0) max = config.maxTraitCount;
			if (ins.trait_cost < 0.01) maxTrait = -1;
			else maxTrait = (int) (max / ins.trait_cost);
			for (var base : config.traits()) {
				if (base.condition() == null || base.condition().match(entity, mobLevel, ins))
					genBase(base);
			}
		} else maxTrait = max;

		var list = LHTraits.TRAITS.get().stream().filter(e ->
				(config == null || !config.blacklist().contains(e)) &&
						!traits.containsKey(e) &&
						e.allow(entity, mobLevel, ins.getMaxTraitLevel())).toList();
		pool = new TraitPool(list, traits);

	}

	private int getRank(MobTrait e) {
		return traits.getOrDefault(e, 0);
	}

	private void setRank(MobTrait e, int rank) {
		if (rank == 0) {
			traits.remove(e);
		} else {
			if (pool != null && getRank(e) == 0)
				pool.update(e);
			traits.put(e, rank);
		}
	}

	private void genBase(EntityConfig.TraitBase base) {
		MobTrait e = base.trait();
		if (e == null) return;
		int maxTrait = TraitManager.getMaxLevel() + 1;
		if (!e.allow(entity, mobLevel, maxTrait)) return;
		int max = e.getMaxLevel(access);// config bypass player trait cap
		int cost = e.getCost(access, ins.trait_cost);
		int old = Math.min(e.getMaxLevel(access), Math.max(getRank(e), base.free()));
		int expected = Math.min(max, Math.max(old, base.min()));
		int rank = Math.min(expected, old + level / cost);
		setRank(e, Math.max(old, rank));
		if (rank > old) {
			level -= (rank - old) * cost;
		}
	}

	private void generate() {
		while (level > 0 && !pool.isEmpty()) {
			if (maxTrait > 0 && traits.size() >= maxTrait)
				break;
			MobTrait e = pool.pop();
			int cost = e.getCost(access, ins.trait_cost);
			if (cost > level) {
				continue;
			}
			int max = Math.min(ins.getMaxTraitLevel(), e.getMaxLevel(access));
			int old = Math.min(e.getMaxLevel(access), getRank(e));
			int rank = Math.min(max, old + rand.nextInt(level / cost) + 1);
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

	private class TraitPool {

		private final LinkedList<TraitEntry> list = new LinkedList<>();
		private final LinkedHashMap<MobTrait, TraitEntry> map = new LinkedHashMap<>();
		private int weights = 0;

		public TraitPool(List<MobTrait> available, HashMap<MobTrait, Integer> existing) {
			for (var trait : available) {
				var ent = new TraitEntry(trait);
				list.add(ent);
				map.put(trait, ent);
			}
			for (var e : this.list) {
				weights += e.weight();
			}
			for (var trait : existing.keySet()) {
				update(trait);
			}
			purge();
		}

		private MobTrait pop() {
			int val = rand.nextInt(weights);
			var e = list.getFirst();
			var itr = list.iterator();
			while (itr.hasNext()) {
				var x = itr.next();
				val -= x.weight();
				if (val <= 0) {
					e = x;
					itr.remove();
					map.remove(e.trait);
					weights -= e.weight();
					return e.trait;
				}
			}
			//supposed to be unreachable
			list.remove(e);
			map.remove(e.trait);
			weights -= e.weight();
			return e.trait;
		}

		public boolean isEmpty() {
			return list.isEmpty();
		}

		public void update(MobTrait trait) {
			var data = trait.getExclusion(access);
			for (var pair : data.excluded().entrySet()) {
				var entry = map.get(pair.getKey().value());
				if (entry == null) continue;
				weights -= entry.updateExclusion(pair.getValue());
			}
			for (var e : list) {
				var exc = e.trait.getExclusion(access);
				var val = exc.excluded().getOrDefault(trait.holder(), 0d);
				if (val == 0) continue;
				weights -= e.updateExclusion(val);
			}
			purge();
		}

		private void purge() {
			var itr = list.iterator();
			while (itr.hasNext()) {
				var x = itr.next();
				if (x.weight() == 0) {
					itr.remove();
					map.remove(x.trait);
				}
			}
		}

	}

	private class TraitEntry {

		private final MobTrait trait;
		private final int baseWeight;
		private double chance;
		private int weight;

		private TraitEntry(MobTrait trait) {
			this.trait = trait;
			chance = 1;
			baseWeight = trait.getConfig(access).weight();
			weight = baseWeight;
		}

		public int weight() {
			return weight;
		}

		public int updateExclusion(double value) {
			int old = weight;
			chance *= 1 - value;
			weight = (int) (baseWeight * chance);
			return old - weight;
		}

	}


}
