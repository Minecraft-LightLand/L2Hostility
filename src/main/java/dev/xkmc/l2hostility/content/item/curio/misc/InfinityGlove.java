package dev.xkmc.l2hostility.content.item.curio.misc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2hostility.content.item.curio.core.SingletonItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class InfinityGlove extends SingletonItem {

	public InfinityGlove(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
		CuriosApi.addSlotModifier(map, "ring", id, 5, AttributeModifier.Operation.ADD_VALUE);
		CuriosApi.addSlotModifier(map, "charm", id, 1, AttributeModifier.Operation.ADD_VALUE);
		return map;
	}

}
