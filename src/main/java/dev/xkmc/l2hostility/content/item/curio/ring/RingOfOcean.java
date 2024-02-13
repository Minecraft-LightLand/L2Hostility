package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2hostility.content.item.curio.core.CurioItem;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RingOfOcean extends CurioItem {

	public RingOfOcean(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_RING_OCEAN.get().withStyle(ChatFormatting.GOLD));
	}

}
