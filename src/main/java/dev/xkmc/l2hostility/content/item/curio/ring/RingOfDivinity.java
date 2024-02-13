package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.item.curio.core.CurioItem;
import dev.xkmc.l2hostility.content.item.curio.core.ISimpleCapItem;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class RingOfDivinity extends CurioItem implements ISimpleCapItem {

	public RingOfDivinity(Properties properties) {
		super(properties);
	}


	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_DIVINITY.get().withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void curioTick(ItemStack stack, SlotContext slotContext) {
		LivingEntity wearer = slotContext.entity();
		if (wearer == null) return;
		EffectUtil.refreshEffect(wearer, new MobEffectInstance(LCEffects.CLEANSE.get(), 40,
				0, true, true), EffectUtil.AddReason.SELF, wearer);
	}

}
