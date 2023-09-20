package dev.xkmc.l2hostility.content.item.curse;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseOfSloth extends CurseCurioItem {

	public CurseOfSloth(Properties props) {
		super(props);
	}

	@Override
	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 0;
	}

	@Override
	public double getGrowFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 0;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_CHARM_SLOTH.get().withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_NO_DROP.get().withStyle(ChatFormatting.RED));
	}

}
