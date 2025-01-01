package dev.xkmc.l2hostility.compat.kubejs;

import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class AttributeTraitBuilder extends AbstractTraitBuilder<AttributeTraitBuilder> {

	private final List<AttributeTrait.AttributeEntry> list = new ArrayList<>();

	public AttributeTraitBuilder(ResourceLocation id) {
		super(id);
	}

	public AttributeTraitBuilder attribute(String name, String attribute, double factor, String operation) {
		AttributeModifier.Operation op = switch (operation) {
			case "%", "+%", "base", "BASE", "mult_base", "MULT_BASE",
				 "multiply_base", "MULTIPLY_BASE" -> AttributeModifier.Operation.MULTIPLY_BASE;
			case "*", "x", "*%", "x%", "total", "TOTAL", "mult_total", "MULT_TOTAL",
				 "multiply_total", "MULTIPLY_TOTAL" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
			default -> AttributeModifier.Operation.ADDITION;
		};
		list.add(new AttributeTrait.AttributeEntry(
				name, () -> ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute)),
				() -> factor, op
		));
		return this;
	}

	@Override
	public MobTrait createObject() {
		if (color == null) color(ChatFormatting.BLUE);
		return new AttributeTrait(color, list.toArray(AttributeTrait.AttributeEntry[]::new));
	}

}
