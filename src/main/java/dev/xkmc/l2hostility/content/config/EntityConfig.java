package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.EntityType;

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

	@Override
	protected void postMerge() {
		for (var e : list) {
			for (var type : e.entities) {
				cache.put(type, e);
			}
		}
	}

	@Nullable
	public Config get(EntityType<?> type) {
		return cache.get(type);
	}

	@SerialClass
	public static class Config {

		@SerialClass.SerialField
		private final ArrayList<EntityType<?>> entities = new ArrayList<>();

		@SerialClass.SerialField
		private final ArrayList<TraitBase> traits = new ArrayList<>();

		@SerialClass.SerialField
		private final LinkedHashSet<MobTrait> blacklist = new LinkedHashSet<>();

		@SerialClass.SerialField
		private WorldDifficultyConfig.DifficultyConfig difficulty =
				new WorldDifficultyConfig.DifficultyConfig(0, 0, 0, 0, 1, 1);

		@Deprecated
		public Config() {

		}

		public Config(List<EntityType<?>> entities,
					  List<TraitBase> traits,
					  WorldDifficultyConfig.DifficultyConfig difficulty) {
			this.entities.addAll(entities);
			this.traits.addAll(traits);
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

	}

	public record TraitBase(MobTrait trait, int free, int min) {

	}

	public final EntityConfig putEntity(int min, int base, double var, double scale, List<EntityType<?>> keys, List<TraitBase> traits) {
		list.add(new Config(new ArrayList<>(keys), new ArrayList<>(traits),
				new WorldDifficultyConfig.DifficultyConfig(min, base, var, scale, 1, 1)));
		return this;
	}

}
