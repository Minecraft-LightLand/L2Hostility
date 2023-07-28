package dev.xkmc.l2hostility.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2hostility.content.config.ModifierConfig;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfigGen;
import dev.xkmc.l2hostility.init.registrate.LHModifiers;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MobModifierBuilder<T extends MobModifier, P> extends AbstractBuilder<MobModifier, T, P, MobModifierBuilder<T, P>> {

	private final NonNullSupplier<T> sup;

	public MobModifierBuilder(LHRegistrate owner, P parent, String name, BuilderCallback callback,
							  NonNullSupplier<T> sup, ModifierConfig config) {
		super(owner, parent, name, callback, LHModifiers.MODIFIERS.key());
		this.sup = sup;
		LHConfigGen.LIST.add(e -> e.add(L2Hostility.MODIFIER, new ResourceLocation(getOwner().getModid(), getName()), config));
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
		return new MobModifierEntry<>(Wrappers.cast(this.getOwner()), delegate);
	}

	public MobModifierBuilder<T, P> lang(String name) {
		return this.lang(NamedEntry::getDescriptionId, name);
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

}
