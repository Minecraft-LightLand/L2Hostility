package dev.xkmc.l2hostility.compat.curios;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CurioCompat {

	public static boolean hasItem(Player player, Item item) {
		for (EquipmentSlot e : EquipmentSlot.values()) {
			if (player.getItemBySlot(e).is(item)) {
				return true;
			}
		}
		if (ModList.get().isLoaded(CuriosApi.MODID)) {
			return hasItemImpl(player, item);
		}
		return false;
	}

	public static List<ItemStack> getItems(Player player, Predicate<ItemStack> pred) {
		List<ItemStack> ans = new ArrayList<>();
		for (EquipmentSlot e : EquipmentSlot.values()) {
			ItemStack stack = player.getItemBySlot(e);
			if (pred.test(stack)) {
				ans.add(stack);
			}
		}
		if (ModList.get().isLoaded(CuriosApi.MODID)) {
			getItemImpl(ans, player, pred);
		}
		return ans;
	}

	private static boolean hasItemImpl(Player player, Item item) {
		var opt = CuriosApi.getCuriosHelper().getCuriosHandler(player);
		if (opt.resolve().isEmpty()) {
			return false;
		}
		for (var e : opt.resolve().get().getCurios().values()) {
			for (int i = 0; i < e.getStacks().getSlots(); i++) {
				if (e.getStacks().getStackInSlot(i).is(item)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void getItemImpl(List<ItemStack> list, Player player, Predicate<ItemStack> pred) {
		var opt = CuriosApi.getCuriosHelper().getCuriosHandler(player);
		if (opt.resolve().isEmpty()) {
			return;
		}
		for (var e : opt.resolve().get().getCurios().values()) {
			for (int i = 0; i < e.getStacks().getSlots(); i++) {
				ItemStack stack = e.getStacks().getStackInSlot(i);
				if (pred.test(stack)) {
					list.add(stack);
				}
			}
		}
	}

}
