package dev.xkmc.l2hostility.content.item.curio.core;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;

public class MultiSlotItem extends SingletonItem {
	public MultiSlotItem(Properties properties) {
		super(properties);
	}

	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
		tooltips = super.getAttributesTooltip(tooltips, stack);
		List<List<Component>> ans = new ArrayList<>();
		List<Component> cur = null;
		for (var e : tooltips) {
			if (cur == null || e.getContents() == ComponentContents.EMPTY) {
				cur = new ArrayList<>();
				ans.add(cur);
			}
			cur.add(e);
		}
		if (ans.isEmpty()) {
			return tooltips;
		}
		if (ans.size() == 1) {
			return ans.get(0);
		}
		tooltips = ans.get(0);
		for (int i = 0; i < tooltips.size(); i++) {
			if (tooltips.get(i).getContents() instanceof TranslatableContents tr) {
				if (tr.getKey().startsWith("curios.modifiers")) {
					tooltips.set(i, Component.translatable("curios.modifiers.curio")
							.withStyle(ChatFormatting.GOLD));
					break;
				}
			}
		}
		return tooltips;
	}

}
