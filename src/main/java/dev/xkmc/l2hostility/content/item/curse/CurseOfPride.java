package dev.xkmc.l2hostility.content.item.curse;

import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseOfPride extends CurseCurioItem {

	public CurseOfPride(Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		int protect = (int) Math.round(100 * LHConfig.COMMON.prideProtectionBonus.get());
		int damage = (int) Math.round(100 * LHConfig.COMMON.prideDamageBonus.get());
		int trait = (int) Math.round(100 * (1 / LHConfig.COMMON.prideTraitFactor.get() - 1));
		list.add(LangData.ITEM_CHARM_PRIDE.get(protect, damage).withStyle(ChatFormatting.GOLD));
		list.add(LangData.ITEM_CHARM_TRAIT_CHEAP.get(trait).withStyle(ChatFormatting.RED));
	}

}
