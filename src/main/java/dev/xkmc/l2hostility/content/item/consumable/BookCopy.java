package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public class BookCopy extends Item {

	public BookCopy(Properties properties) {
		super(properties);
	}

	public static int cost(Enchantment key, int value) {
		if (key.getMaxLevel() >= value) {
			return 1;
		}
		return 1 << Math.min(10, value - key.getMaxLevel());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_BOOK_COPY.get().withStyle(ChatFormatting.GRAY));
	}

}
