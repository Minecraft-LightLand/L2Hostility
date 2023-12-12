package dev.xkmc.l2hostility.init.entries;

import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.item.traits.TraitSymbol;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfigGen;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.builders.AbstractBuilder;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.builders.ItemBuilder;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonnullType;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TraitBuilder<T extends MobTrait, P> extends AbstractBuilder<MobTrait, T, P, TraitBuilder<T, P>> {

	private final NonNullSupplier<T> sup;

	public TraitBuilder(LHRegistrate owner, P parent, String name, BuilderCallback callback,
						NonNullSupplier<T> sup, Function<ResourceLocation, TraitConfig> config) {
		super(owner, parent, name, callback, LHTraits.TRAITS.key());
		this.sup = sup;
		ResourceLocation rl = new ResourceLocation(getOwner().getModid(), getName());
		var entry = config.apply(rl);
		LHConfigGen.LIST.add(e -> L2Hostility.TRAIT.add(e, rl, entry));
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
		return new TraitEntry<>(Wrappers.cast(this.getOwner()), delegate);
	}

	public TraitBuilder<T, P> lang(String name) {
		return this.lang(NamedEntry::getDescriptionId, name);
	}

	public <I extends TraitSymbol> ItemBuilder<I, TraitBuilder<T, P>> item(NonNullFunction<Item.Properties, I> sup) {
		return getOwner().item(this, getName(), sup)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/bg"),
						pvd.modLoc("item/trait/" + ctx.getName())))
				.setData(ProviderType.LANG, NonNullBiConsumer.noop())
				.tag(LHTagGen.TRAIT_ITEM);
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

	public TraitBuilder<T, P> desc(String s) {
		getOwner().addRawLang("trait." + getOwner().getModid() + "." + getName() + ".desc", s);
		return this;
	}

}
