package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RingOfCorrosion extends CurseCurioItem {

	public RingOfCorrosion(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_CORROSION.get(Math.round(LHConfig.COMMON.ringOfCorrosionFactor.get() * 100)).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
		LivingEntity target = cache.getAttackTarget();
		for (EquipmentSlot e : EquipmentSlot.values()) {
			DurabilityEater.flat(target, e, LHConfig.COMMON.ringOfCorrosionFactor.get());
		}
	}

}
