package dev.xkmc.l2hostility.content.item.curio.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.content.item.curios.ICapItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public interface ISimpleCapItem extends ICapItem<SimpleCurioData> {

	@Override
	default SimpleCurioData create(ItemStack stack) {
		return new SimpleCurioData(this, stack);
	}

	default Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
		return LinkedHashMultimap.create();
	}

	default void curioTick(ItemStack stack, SlotContext slotContext) {
	}

}
