package dev.xkmc.l2hostility.init.registrate.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import net.minecraftforge.registries.RegistryObject;

public class MobModifierEntry<T extends MobModifier> extends RegistryEntry<T> {

	public MobModifierEntry(LHRegistrate owner, RegistryObject<T> delegate) {
		super(owner, delegate);
	}

}
