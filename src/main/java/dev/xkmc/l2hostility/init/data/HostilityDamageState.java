package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.Locale;
import java.util.function.Consumer;

public enum HostilityDamageState implements DamageState {
	BYPASS_COOLDOWN(DamageTypeTags.BYPASSES_COOLDOWN);

	private final TagKey<DamageType>[] tags;

	@SafeVarargs
	HostilityDamageState(TagKey<DamageType>... tags) {
		this.tags = tags;
	}

	@Override
	public void gatherTags(Consumer<TagKey<DamageType>> collector) {
		for (var tag : tags) {
			collector.accept(tag);
		}
	}

	@Override
	public void removeTags(Consumer<TagKey<DamageType>> consumer) {

	}

	@Override
	public ResourceLocation getId() {
		return new ResourceLocation(L2Hostility.MODID, name().toLowerCase(Locale.ROOT));
	}

	@Override
	public boolean overrides(DamageState state) {
		return false;
	}


}

