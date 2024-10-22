package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class GreedOfNidhoggur extends CurseCurioItem {

	public GreedOfNidhoggur(Properties props) {
		super(props);
	}

	@Override
	public int getExtraLevel() {
		return LHConfig.SERVER.nidhoggurExtraLevel.get();
	}

	@Override
	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return LHConfig.SERVER.greedDropFactor.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		int nid = (int) Math.round(100 * LHConfig.SERVER.nidhoggurDropFactor.get());
		list.add(LangData.ITEM_CHARM_GREED.get(LHConfig.SERVER.greedDropFactor.get()).withStyle(ChatFormatting.GOLD));
		list.add(LangData.NIDHOGGUR.get(nid).withStyle(ChatFormatting.GOLD));
	}

}
