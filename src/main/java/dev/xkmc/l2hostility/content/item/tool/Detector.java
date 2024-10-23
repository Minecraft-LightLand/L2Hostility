package dev.xkmc.l2hostility.content.item.tool;

import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class Detector extends Item {

	public Detector(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_DETECTOR.get().withStyle(ChatFormatting.GRAY));
		list.add(LangData.sectionRender().withStyle(ChatFormatting.DARK_GREEN));
	}

}
