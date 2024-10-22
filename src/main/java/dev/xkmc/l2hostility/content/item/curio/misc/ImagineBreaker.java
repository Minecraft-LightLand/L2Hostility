package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImagineBreaker extends CurseCurioItem {

	public ImagineBreaker(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_IMAGINE_BREAKER.get().withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_NO_DROP.get().withStyle(ChatFormatting.RED));
	}

	@Override
	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 0;
	}
}
