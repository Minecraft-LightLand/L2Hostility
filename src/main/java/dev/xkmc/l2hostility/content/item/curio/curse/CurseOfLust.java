package dev.xkmc.l2hostility.content.item.curio.curse;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CurseOfLust extends CurseCurioItem {

	public CurseOfLust(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel() {
		return LHConfig.SERVER.lustExtraLevel.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_CHARM_LUST.get().withStyle(ChatFormatting.GOLD));
	}

}
