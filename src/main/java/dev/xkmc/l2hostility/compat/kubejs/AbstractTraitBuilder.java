package dev.xkmc.l2hostility.compat.kubejs;

import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import java.util.function.IntSupplier;

public abstract class AbstractTraitBuilder<T extends AbstractTraitBuilder<T>> extends BuilderBase<MobTrait> {

	protected IntSupplier color;

	public AbstractTraitBuilder(ResourceLocation id) {
		super(id);
	}

	public T self() {
		return Wrappers.cast(this);
	}

	public T color(ChatFormatting chat) {
		this.color = chat::getColor;
		return self();
	}

	public T color(int color) {
		this.color = () -> color;
		return self();
	}

	@Override
	public RegistryInfo<MobTrait> getRegistryType() {
		return LHKJSPlugin.TRAITS;
	}

	@Override
	public void generateDataJsons(DataJsonGenerator generator) {
		super.generateDataJsons(generator);
	}
}
