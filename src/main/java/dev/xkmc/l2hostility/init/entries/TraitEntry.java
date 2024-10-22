package dev.xkmc.l2hostility.init.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TraitEntry<T extends MobTrait> extends RegistryEntry<MobTrait, T> {

	public TraitEntry(LHRegistrate owner, DeferredHolder<MobTrait, T> delegate) {
		super(owner, delegate);
	}

}
