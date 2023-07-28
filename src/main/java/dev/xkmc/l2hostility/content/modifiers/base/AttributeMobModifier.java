package dev.xkmc.l2hostility.content.modifiers.base;

import dev.xkmc.l2hostility.content.logic.ModifierManager;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class AttributeMobModifier extends MobModifier {

	public record AttributeEntry(String name, Supplier<Attribute> attribute, DoubleSupplier factor,
								 AttributeModifier.Operation op) {

	}

	private final AttributeEntry[] entries;

	public AttributeMobModifier(AttributeEntry... entries) {
		this.entries = entries;
	}

	@Override
	public void initialize(LivingEntity le, int level) {
		for (var e : entries) {
			ModifierManager.addAttribute(le, e.attribute.get(), e.name(), e.factor.getAsDouble() * level, e.op());
		}
	}

}
