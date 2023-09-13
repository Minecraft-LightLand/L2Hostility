package dev.xkmc.l2hostility.content.item.tools.equipment;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseOfGluttony extends CurseCurioItem {

	public CurseOfGluttony(Properties props) {
		super(props);
	}

	@Override
	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 0;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		int rate = (int) Math.round(100 * LHConfig.COMMON.gluttonyDropRate.get());
		list.add(LangData.ITEM_CHARM_GLUTTONY.get(rate).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_NO_DROP.get().withStyle(ChatFormatting.RED));
	}

}
