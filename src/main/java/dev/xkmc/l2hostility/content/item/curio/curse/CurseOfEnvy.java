package dev.xkmc.l2hostility.content.item.curio.curse;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CurseOfEnvy extends CurseCurioItem {

	public CurseOfEnvy(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel() {
		return LHConfig.SERVER.envyExtraLevel.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		int rate = (int) Math.round(100 * LHConfig.SERVER.envyDropRate.get());
		list.add(LangData.ITEM_CHARM_ENVY.get(rate).withStyle(ChatFormatting.GOLD));
	}

}
