package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.core.Holder;

import java.util.LinkedHashMap;

public record TraitExclusion(LinkedHashMap<Holder<MobTrait>, Double> excluded) {

	public static final TraitExclusion DEFAULT = new TraitExclusion(new LinkedHashMap<>());

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final LinkedHashMap<Holder<MobTrait>, Double> excluded = new LinkedHashMap<>();


		public Builder of(Holder<MobTrait> trait, double v) {
			excluded.put(trait, v);
			return this;
		}

		public TraitExclusion build() {
			return new TraitExclusion(excluded);
		}

	}

}
