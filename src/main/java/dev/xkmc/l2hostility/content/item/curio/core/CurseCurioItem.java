package dev.xkmc.l2hostility.content.item.curio.core;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class CurseCurioItem extends CurioItem {

	public static List<GenericItemStack<CurseCurioItem>> getFromPlayer(LivingEntity player) {
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
		super(props);
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

	public boolean reflectTrait(MobTrait trait) {
		return false;
	}

	public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
	}

	public void onDamage(ItemStack stack, LivingEntity user, LivingDamageEvent event) {
	}

}
