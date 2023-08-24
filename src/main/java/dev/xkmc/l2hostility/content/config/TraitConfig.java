package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.LinkedHashSet;

@SerialClass
public class TraitConfig extends BaseConfig {

	public static final TraitConfig DEFAULT = new TraitConfig(10, 100, 1, 10);

	@SerialClass.SerialField
	public int min_level, cost, max_rank, weight;

	@SerialClass.SerialField
	public LinkedHashSet<EntityType<?>> blacklist = new LinkedHashSet<>();

	@SerialClass.SerialField
	public LinkedHashSet<EntityType<?>> whitelist = new LinkedHashSet<>();

	@Deprecated
	public TraitConfig() {
	}

	public TraitConfig(int cost, int weight, int maxRank, int minLevel) {
		this.cost = cost;
		this.weight = weight;
		this.max_rank = maxRank;
		this.min_level = minLevel;
	}

	public TraitConfig whitelist(EntityType<?>... types) {
		whitelist.addAll(Arrays.asList(types));
		return this;
	}

}
