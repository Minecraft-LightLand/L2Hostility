package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-trait datapack config declaring which traits are excluded (reduced in roll weight) when
 * this trait is present. Each entry maps an excluded trait id to an exclusion factor: the weight
 * of the excluded trait is multiplied by {@code 1 - factor} each time this trait is added, and the
 * trait is removed from the pool entirely when its weight reaches 0.
 */
@SerialClass
public class TraitExclusion extends BaseConfig {

	public static final TraitExclusion DEFAULT = new TraitExclusion();

	@SerialClass.SerialField
	public final HashMap<ResourceLocation, Double> excluded = new HashMap<>();

	@Deprecated
	public TraitExclusion() {
	}

	public TraitExclusion of(ResourceLocation trait, double v) {
		excluded.put(trait, v);
		return this;
	}

	public double getValue(ResourceLocation trait) {
		return excluded.getOrDefault(trait, 0d);
	}

	public Map<ResourceLocation, Double> getExcluded() {
		return excluded;
	}

	public boolean isEmpty() {
		return excluded.isEmpty();
	}

}
