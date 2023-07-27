package dev.xkmc.l2complements.content.modifiers.base;

import dev.xkmc.l2complements.content.logic.ModifierManager;
import dev.xkmc.l2complements.content.modifiers.core.MobModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class AttributeMobModifier extends MobModifier {

	public final Supplier<Attribute> attribute;
	private final DoubleSupplier factor;

	public AttributeMobModifier(Supplier<Attribute> attribute, DoubleSupplier factor) {
		this.attribute = attribute;
		this.factor = factor;
	}

	@Override
	public void initialize(LivingEntity le, int level) {
		ModifierManager.addAttribute(le, attribute.get(), getID(), factor.getAsDouble() * level);
	}

}
