package dev.xkmc.l2hostility.content.item.curio.curse;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseOfGreed extends CurseCurioItem {

	public CurseOfGreed(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel(ItemStack stack) {
		return LHConfig.COMMON.greedExtraLevel.get();
	}

	@Override
	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return LHConfig.COMMON.greedDropFactor.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		int rate = (int) Math.round(100 * (LHConfig.COMMON.greedDropFactor.get() - 1));
		int lv = LHConfig.COMMON.greedExtraLevel.get();
		list.add(LangData.ITEM_CHARM_GREED.get(rate).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_ADD_LEVEL.get(lv).withStyle(ChatFormatting.RED));
	}

}
