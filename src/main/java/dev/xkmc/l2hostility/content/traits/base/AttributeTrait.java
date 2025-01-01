package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2hostility.content.logic.TraitManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class AttributeTrait extends MobTrait {

	public record AttributeEntry(String name, Supplier<Attribute> attribute, DoubleSupplier factor,
								 AttributeModifier.Operation op) {

	}

	private final AttributeEntry[] entries;

	public AttributeTrait(ChatFormatting style, AttributeEntry... entries) {
		super(style);
		this.entries = entries;
	}

	public AttributeTrait(IntSupplier color, AttributeEntry... entries) {
		super(color);
		this.entries = entries;
	}

	@Override
	public void initialize(LivingEntity le, int level) {
		for (var e : entries) {
			TraitManager.addAttribute(le, e.attribute.get(), e.name(), e.factor.getAsDouble() * level, e.op());
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		for (var e : entries) {
			double val = e.factor.getAsDouble();
			if (val == 0) continue;
			list.add(mapLevel(i -> (e.op == AttributeModifier.Operation.ADDITION ?
					Component.literal("+" + Math.round(val * i))
					: Component.literal("+" + Math.round(val * i * 100) + "%"))
					.withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
					Component.translatable(e.attribute.get().getDescriptionId()).withStyle(ChatFormatting.BLUE)));
		}
	}

}
