package dev.xkmc.l2hostility.content.item.curse;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;

public class CurseCurioItem extends Item {

	public static List<GenericItemStack<CurseCurioItem>> getFromPlayer(Player player) {
		var list = CurioCompat.getItems(player, e -> e.getItem() instanceof CurseCurioItem);
		List<GenericItemStack<CurseCurioItem>> ans = new ArrayList<>();
		for (var e : list) {
			if (e.getItem() instanceof CurseCurioItem item) {
				ans.add(new GenericItemStack<>(item, e));
			}
		}
		return ans;
	}

	public CurseCurioItem(Properties props) {
		super(props.stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	public int getExtraLevel(ItemStack stack) {
		return 0;
	}

	public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 1;
	}

	public double getGrowFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
		return 1;
	}

}
