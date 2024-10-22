package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class DivinityLight extends CurseCurioItem implements ICurioItem {

	public DivinityLight(Properties props) {
		super(props);
	}

	@Override
	public double getGrowFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 0;
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		if (slotContext.entity() instanceof Player player) {
			LHMiscs.PLAYER.type().getOrCreate(player).getLevelEditor(player).setBase(0);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.DIVINITY_LIGHT.get().withStyle(ChatFormatting.GOLD));
	}

}
