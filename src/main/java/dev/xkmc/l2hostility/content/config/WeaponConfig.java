package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class WeaponConfig extends BaseConfig {

	public static ItemStack getRandomWeapon(int level, RandomSource r) {
		WeaponConfig config = L2Hostility.WEAPON.getMerged();
		int total = 0;
		List<Config> list = new ArrayList<>();
		for (var e : config.melee_weapons) {
			if (e.level <= level) {
				list.add(e);
				total += e.weight();
			}
		}
		if (total == 0)
			return ItemStack.EMPTY;
		int val = r.nextInt(total);
		for (var e : list) {
			val -= e.weight();
			if (val <= 0) {
				return e.stack.get(r.nextInt(e.stack.size())).copy();
			}
		}
		return ItemStack.EMPTY;
	}

	@SerialClass.SerialField
	@ConfigCollect(CollectType.COLLECT)
	public final ArrayList<Config> melee_weapons = new ArrayList<>();

	public WeaponConfig putMeleeWeapon(int level, int weight, Item... items) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (var e : items) {
			list.add(e.getDefaultInstance());
		}
		melee_weapons.add(new Config(list, level, weight));
		return this;
	}

	public record Config(ArrayList<ItemStack> stack, int level, int weight) {

	}

}
