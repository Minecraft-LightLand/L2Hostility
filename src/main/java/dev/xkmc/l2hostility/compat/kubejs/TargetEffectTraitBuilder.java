package dev.xkmc.l2hostility.compat.kubejs;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class TargetEffectTraitBuilder extends AbstractTraitBuilder<TargetEffectTraitBuilder> {

	private Function<Integer, MobEffectInstance> func;

	public TargetEffectTraitBuilder(ResourceLocation id) {
		super(id);
	}

	public TargetEffectTraitBuilder fixedLevel(String effect, int duration, int amplifier) {
		this.func = i -> new MobEffectInstance(
				ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effect)),
				duration * i, amplifier);
		return this;
	}

	public TargetEffectTraitBuilder fixedDuration(String effect, int duration) {
		this.func = i -> new MobEffectInstance(
				ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effect)),
				duration, i - 1);
		return this;
	}

	@Override
	public MobTrait createObject() {
		if (func == null) func = i -> new MobEffectInstance(MobEffects.WEAKNESS, 100, i - 1);
		return color == null ? new TargetEffectTrait(func) : new TargetEffectTrait(color, func);
	}

}
