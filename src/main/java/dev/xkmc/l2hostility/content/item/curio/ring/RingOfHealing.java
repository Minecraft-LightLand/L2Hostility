package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2hostility.content.item.curio.core.SingletonItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class RingOfHealing extends SingletonItem {

	public RingOfHealing(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_HEALING.get(Math.round(LHConfig.SERVER.ringOfHealingRate.get() * 100)).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity wearer = slotContext.entity();
		if (wearer == null) return;
		if (wearer.tickCount % 20 != 0) return;
		wearer.heal((float) (LHConfig.SERVER.ringOfHealingRate.get() * wearer.getMaxHealth()));
	}

}
