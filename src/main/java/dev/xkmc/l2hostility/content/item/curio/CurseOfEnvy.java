package dev.xkmc.l2hostility.content.item.curio;

import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseOfEnvy extends CurseCurioItem {

	public CurseOfEnvy(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel(ItemStack stack) {
		return LHConfig.COMMON.envyExtraLevel.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		int rate = (int) Math.round(100 * LHConfig.COMMON.envyDropRate.get());
		int lv = LHConfig.COMMON.envyExtraLevel.get();
		list.add(LangData.ITEM_CHARM_ENVY.get(rate).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_ADD_LEVEL.get(lv).withStyle(ChatFormatting.RED));
	}

}
