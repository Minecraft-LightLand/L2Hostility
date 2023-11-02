package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Abrahadabra extends CurseCurioItem {

	public Abrahadabra(Properties properties) {
		super(properties);
	}

	@Override
	public int getExtraLevel(ItemStack stack) {
		return LHConfig.COMMON.abrahadabraExtraLevel.get();
	}

	@Override
	public boolean reflectTrait(MobTrait trait) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ABRAHADABRA.get().withStyle(ChatFormatting.GOLD));
		int lv = LHConfig.COMMON.abrahadabraExtraLevel.get();
		list.add(LangData.ITEM_CHARM_ADD_LEVEL.get(lv).withStyle(ChatFormatting.RED));
	}

}
