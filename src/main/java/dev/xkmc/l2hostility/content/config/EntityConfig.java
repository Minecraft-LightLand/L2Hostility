package dev.xkmc.l2hostility.content.config;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

@SerialClass
public class EntityConfig extends BaseConfig {

	public static boolean allow(EntityType<?> type, MobTrait trait) {
		Config config = L2Hostility.ENTITY.getMerged().get(type);
		if (config == null) return true;
		return !config.blacklist.contains(trait);
	}

	@SerialClass.SerialField
	@ConfigCollect(CollectType.COLLECT)
	public final ArrayList<Config> list = new ArrayList<>();

	private final Map<EntityType<?>, Config> cache = new HashMap<>();
	private final Map<ResourceLocation, ArrayList<Pair<SpecialConfigCondition<?>, Config>>> conditions = new HashMap<>();

	@Override
	protected void postMerge() {
		for (var e : list) {
			if (e.specialConditions.isEmpty()) {
				for (var type : e.entities) {
					cache.put(type, e);
				}
			} else {
				for (var str : e.specialConditions) {
					conditions.computeIfAbsent(str.id, k -> new ArrayList<>()).add(Pair.of(str, e));
				}
			}
		}
	}

	@Nullable
	public Config get(EntityType<?> type) {
		if (!LHConfig.COMMON.enableEntitySpecificDatapack.get())
			return null;
		return cache.get(type);
	}

	@Nullable
	public <T> Config get(EntityType<?> type, ResourceLocation id, Class<T> cls, T obj) {
		if (!LHConfig.COMMON.enableEntitySpecificDatapack.get())
			return null;
		var list = conditions.get(id);
		if (list == null) return null;
		for (var pair : list) {
			var cond = pair.getFirst();
			if (!pair.getSecond().entities.contains(type)) continue;
			if (cond.cls() == cls && cond.test(Wrappers.cast(obj))) {
				return pair.getSecond();
			}
		}
		return null;
	}

	@SerialClass
	public static class Config {

		@SerialClass.SerialField
		public final ArrayList<EntityType<?>> entities = new ArrayList<>();

		@SerialClass.SerialField
		private final ArrayList<SpecialConfigCondition<?>> specialConditions = new ArrayList<>();

		@SerialClass.SerialField
		private final ArrayList<TraitBase> traits = new ArrayList<>();

		@SerialClass.SerialField
		private final LinkedHashSet<MobTrait> blacklist = new LinkedHashSet<>();

		@SerialClass.SerialField
		private WorldDifficultyConfig.DifficultyConfig difficulty =
				new WorldDifficultyConfig.DifficultyConfig(0, 0, 0, 0, 1, 1);

		@SerialClass.SerialField
		public final ArrayList<ItemPool> items = new ArrayList<>();

		@SerialClass.SerialField
		public int minSpawnLevel = 0, maxLevel = 0;

		@SerialClass.SerialField
		public MasterConfig asMaster = null;

		@Deprecated
		public Config() {

		}

		public Config(List<EntityType<?>> entities,
					  WorldDifficultyConfig.DifficultyConfig difficulty) {
			this.entities.addAll(entities);
			this.difficulty = difficulty;
		}

		public Set<MobTrait> blacklist() {
			return blacklist;
		}

		public List<TraitBase> traits() {
			return traits;
		}

		public WorldDifficultyConfig.DifficultyConfig difficulty() {
			return difficulty;
		}

		public Config minLevel(int level) {
			minSpawnLevel = level;
			return this;
		}

		public Config trait(List<TraitBase> list) {
			traits.addAll(list);
			return this;
		}

		public Config item(List<ItemPool> list) {
			items.addAll(list);
			return this;
		}

		public Config conditions(SpecialConfigCondition<?> list) {
			specialConditions.add(list);
			return this;
		}

		public Config blacklist(MobTrait... list) {
			Collections.addAll(blacklist, list);
			return this;
		}

		public Config master(int maxTotal, int interval, Minion... minions) {
			this.asMaster = new MasterConfig(maxTotal, interval, new ArrayList<>(List.of(minions)));
			return this;
		}

	}

	public record MasterConfig(
			int maxTotalCount,
			int spawnInterval,
			ArrayList<Minion> minions
	) {

	}

	public record Minion(
			EntityType<?> type, int maxCount, int minLevel, double maxHealthPercentage,
			int spawnRange, int cooldown, boolean copyLevel, boolean copyTrait,
			double linkDistance, boolean protectMaster, boolean discardOnUnlink) {

	}

	public record ItemEntry(int weight, ItemStack stack) {

	}

	public record ItemPool(int level, float chance, String slot, ArrayList<ItemEntry> entries) {

	}

	public record TraitBase(MobTrait trait, int free, int min, @Nullable TraitCondition condition) {

	}

	public record TraitCondition(int lv, float chance, @Nullable ResourceLocation id) {

		public boolean match(LivingEntity entity, int mobLevel, MobDifficultyCollector ins) {
			if (entity.getRandom().nextDouble() > chance) return false;
			if (mobLevel < lv) return false;
			if (id != null && !ins.hasAdvancement(id)) return false;
			return true;
		}

	}

	public final EntityConfig putEntity(int min, int base, double var, double scale, List<EntityType<?>> keys, List<TraitBase> traits) {
		return putEntityAndItem(min, base, var, scale, keys, traits, List.of());
	}

	public final EntityConfig putEntityAndItem(int min, int base, double var, double scale, List<EntityType<?>> keys, List<TraitBase> traits, List<ItemPool> items) {
		return put(entity(min, base, var, scale, keys).trait(traits).item(items));
	}

	public final EntityConfig put(Config config) {
		list.add(config);
		return this;
	}

	public static Config entity(int min, int base, double var, double scale, List<EntityType<?>> keys) {
		return new Config(new ArrayList<>(keys),
				new WorldDifficultyConfig.DifficultyConfig(min, base, var, scale, 1, 1));
	}

	public static ItemPool simplePool(int level, String slot, ItemStack stack) {
		return new ItemPool(level, 1, slot, new ArrayList<>(List.of(new ItemEntry(100, stack))));
	}

	public static TraitBase trait(MobTrait trait, int free, int min) {
		return new TraitBase(trait, free, min, null);
	}

	public static TraitBase trait(MobTrait trait, int free, int min, int lv, float chance) {
		return new TraitBase(trait, free, min, new TraitCondition(lv, chance, null));
	}

}
