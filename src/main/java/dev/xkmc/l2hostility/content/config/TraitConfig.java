package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.EntityType;

import java.util.LinkedHashSet;

@SerialClass
public class TraitConfig extends BaseConfig {

	public static final TraitConfig DEFAULT = new TraitConfig(10, 1, 1);

	@SerialClass.SerialField
	public int cost, maxLevel;

	@SerialClass.SerialField
	public double chance;

	@SerialClass.SerialField
	public LinkedHashSet<EntityType<?>> blacklist = new LinkedHashSet<>();

	@SerialClass.SerialField
	public LinkedHashSet<EntityType<?>> whitelist = new LinkedHashSet<>();

	@Deprecated
	public TraitConfig() {
	}

	public TraitConfig(int cost, double chance, int maxLevel) {
		this.cost = cost;
		this.chance = chance;
		this.maxLevel = maxLevel;
	}


}
