package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class Abrahadabra extends CurseCurioItem {

	public Abrahadabra(Properties properties) {
		super(properties);
	}

	@Override
	public int getExtraLevel() {
		return LHConfig.SERVER.abrahadabraExtraLevel.get();
	}

	@Override
	public boolean reflectTrait(MobTrait trait) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ABRAHADABRA.get().withStyle(ChatFormatting.GOLD));
	}

	public boolean isOn(LivingEntity le) {
		return CurioCompat.hasItemInCurioChecked(le, this);
	}

}
