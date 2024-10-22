package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.item.traits.DurabilityEater;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RingOfCorrosion extends CurseCurioItem {

	public RingOfCorrosion(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_CORROSION.get(Math.round(LHConfig.SERVER.ringOfCorrosionFactor.get() * 100)).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_RING_CORROSION_NEG.get(Math.round(LHConfig.SERVER.ringOfCorrosionFactor.get() * 100)).withStyle(ChatFormatting.RED));
	}

	@Override
	public void onHurtTarget(ItemStack stack, LivingEntity user, DamageData.Offence cache) {
		LivingEntity target = cache.getTarget();
		for (EquipmentSlot e : EquipmentSlot.values()) {
			DurabilityEater.flat(target, e, LHConfig.SERVER.ringOfCorrosionFactor.get());
		}
	}

	@Override
	public void onDamage(ItemStack stack, LivingEntity user, DamageData.Defence event) {
		for (EquipmentSlot e : EquipmentSlot.values()) {
			DurabilityEater.flat(user, e, LHConfig.SERVER.ringOfCorrosionPenalty.get());
		}
	}

}
