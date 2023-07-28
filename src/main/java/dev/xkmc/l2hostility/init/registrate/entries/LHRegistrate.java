package dev.xkmc.l2hostility.init.registrate.entries;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2hostility.content.config.ModifierConfig;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2library.base.L2Registrate;

public class LHRegistrate extends L2Registrate {

	public LHRegistrate(String modid) {
		super(modid);
	}

	public final <T extends MobModifier> MobModifierBuilder<T, LHRegistrate> regModifier(String id, NonNullSupplier<T> sup, ModifierConfig config) {
		return this.entry(id, cb -> new MobModifierBuilder<>(this, this, id, cb, sup, config));
	}

}
