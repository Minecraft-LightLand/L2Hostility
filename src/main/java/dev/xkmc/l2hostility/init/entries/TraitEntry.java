package dev.xkmc.l2hostility.init.entries;


import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class TraitEntry<T extends MobTrait> extends RegistryEntry<T> {

	public TraitEntry(LHRegistrate owner, RegistryObject<T> delegate) {
		super(owner, delegate);
	}

}
