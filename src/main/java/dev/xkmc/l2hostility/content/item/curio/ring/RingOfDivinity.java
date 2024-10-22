package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import dev.xkmc.l2hostility.content.item.curio.core.SingletonItem;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class RingOfDivinity extends SingletonItem {

	public RingOfDivinity(Properties properties) {
		super(properties);
	}


	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_DIVINITY.get().withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity wearer = slotContext.entity();
		if (wearer == null) return;
		EffectUtil.refreshEffect(wearer, new MobEffectInstance(LCEffects.CLEANSE, 40,
				0, true, true), wearer);
	}

}
