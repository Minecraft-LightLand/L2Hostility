package dev.xkmc.l2hostility.content.item.curio.core;

import com.google.common.collect.Multimap;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.UUID;

public record SimpleCurioData(ISimpleCapItem item, ItemStack stack) implements ICurio {

	@Override
	public ItemStack getStack() {
		return stack;
	}

	@Override
	public void curioTick(SlotContext slotContext) {
		item.curioTick(stack, slotContext);
	}

	@Override
	public boolean canEquip(SlotContext slotContext) {
		return !CurioCompat.hasItem(slotContext.entity(), stack.getItem());
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
		return item.getAttributeModifiers(slotContext, uuid);
	}

}
