package dev.xkmc.l2hostility.compat.curios;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

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

}
