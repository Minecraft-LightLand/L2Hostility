package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
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
		if (!LHConfig.COMMON.enableEntitySpecificDatapack.get())
			return null;
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

		@SerialClass.SerialField
		public final ArrayList<ItemPool> items = new ArrayList<>();

		@Deprecated
		public Config() {

		}

		public Config(List<EntityType<?>> entities,
					  List<TraitBase> traits,
					  WorldDifficultyConfig.DifficultyConfig difficulty,
					  List<ItemPool> items) {
			this.entities.addAll(entities);
			this.traits.addAll(traits);
			this.difficulty = difficulty;
			this.items.addAll(items);
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
		list.add(new Config(new ArrayList<>(keys), new ArrayList<>(traits),
				new WorldDifficultyConfig.DifficultyConfig(min, base, var, scale, 1, 1),
				items));
		return this;
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
