package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.serial.conditions.BooleanValueCondition;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SerialClass
public class WeaponConfig extends BaseConfig {

	public static ItemStack getRandomMeleeWeapon(int level, RandomSource r, @Nullable ServerPlayer player) {
		WeaponConfig config = L2Hostility.WEAPON.getMerged();
		return getRandomWeapon(config.melee_weapons, level, r, player);
	}

	public static ItemStack getRandomArmor(EquipmentSlot slot, int level, RandomSource r, @Nullable ServerPlayer player) {
		WeaponConfig config = L2Hostility.WEAPON.getMerged();
		return getRandomArmors(slot, config.armors, level, r, player);
	}


	public static ItemStack getRandomRangedWeapon(int level, RandomSource r, @Nullable ServerPlayer player) {
		WeaponConfig config = L2Hostility.WEAPON.getMerged();
		return getRandomWeapon(config.ranged_weapons, level, r, player);
	}

	public static ItemStack getRandomWeapon(ArrayList<ItemConfig> entries, int level, RandomSource r, @Nullable ServerPlayer player) {
		int total = 0;
		List<ItemConfig> list = new ArrayList<>();
		for (var e : entries) {
			if (e.test(level, player)) {
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

	private static ItemStack getRandomArmors(EquipmentSlot slot, ArrayList<ItemConfig> entries, int level, RandomSource r, @Nullable ServerPlayer player) {
		int total = 0;
		List<ItemConfig> list = new ArrayList<>();
		for (var e : entries) {
			if (!e.test(level, player)) continue;
			ArrayList<ItemStack> sub = new ArrayList<>();
			for (var item : e.stack) {
				if (item.isEmpty() ||
						item.getItem() instanceof ArmorItem eq && eq.getEquipmentSlot() == slot ||
						item.getEquipmentSlot() == slot)
					sub.add(item);
			}
			if (!sub.isEmpty()) {
				list.add(new ItemConfig(sub, e.level, e.weight));
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
	public final ArrayList<ItemConfig> melee_weapons = new ArrayList<>();

	@SerialClass.SerialField
	@ConfigCollect(CollectType.COLLECT)
	public final ArrayList<ItemConfig> armors = new ArrayList<>();

	@SerialClass.SerialField
	@ConfigCollect(CollectType.COLLECT)
	public final ArrayList<ItemConfig> ranged_weapons = new ArrayList<>();

	@SerialClass.SerialField
	@ConfigCollect(CollectType.MAP_COLLECT)
	public final LinkedHashMap<LinkedHashSet<EntityType<?>>, ArrayList<ItemConfig>> special_weapons = new LinkedHashMap<>();

	@SerialClass.SerialField
	@ConfigCollect(CollectType.COLLECT)
	public final ArrayList<EnchConfig> weapon_enchantments = new ArrayList<>();

	@SerialClass.SerialField
	@ConfigCollect(CollectType.COLLECT)
	public final ArrayList<EnchConfig> armor_enchantments = new ArrayList<>();

	public WeaponConfig putMeleeWeapon(int level, int weight, Item... items) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (var e : items) {
			list.add(e.getDefaultInstance());
		}
		melee_weapons.add(new ItemConfig(list, level, weight));
		return this;
	}

	public WeaponConfig putArmor(int level, int weight, Item... items) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (var e : items) {
			list.add(e.getDefaultInstance());
		}
		armors.add(new ItemConfig(list, level, weight));
		return this;
	}

	public WeaponConfig putRangedWeapon(int level, int weight, Item... items) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (var e : items) {
			list.add(e.getDefaultInstance());
		}
		ranged_weapons.add(new ItemConfig(list, level, weight));
		return this;
	}

	public WeaponConfig putWeaponEnch(int level, float chance, Enchantment... items) {
		ArrayList<Enchantment> list = new ArrayList<>(Arrays.asList(items));
		weapon_enchantments.add(new EnchConfig(list, level, chance));
		return this;
	}

	public WeaponConfig putArmorEnch(int level, float chance, Enchantment... items) {
		ArrayList<Enchantment> list = new ArrayList<>(Arrays.asList(items));
		armor_enchantments.add(new EnchConfig(list, level, chance));
		return this;
	}

	public record ItemConfig(ArrayList<ItemStack> stack, int level, int weight,
							 @Nullable ItemCondition condition) {

		public static final ItemConfig EMPTY = new WeaponConfig.ItemConfig(new ArrayList<>(List.of(ItemStack.EMPTY)), 0, 1000);

		public ItemConfig(ArrayList<ItemStack> stack, int level, int weight) {
			this(stack, level, weight, null);
		}

		public boolean test(int lv, @Nullable ServerPlayer player) {
			if (lv < level()) return false;
			return condition == null || condition.test(player);
		}
	}

	public record ItemCondition(
			ArrayList<ResourceLocation> advancements,
			@Nullable BooleanValueCondition config
	) {

		public boolean test(@Nullable ServerPlayer sp) {
			if (config != null) {
				if (!config.test(null)) {
					return false;
				}
			}
			if (!advancements.isEmpty()) {
				if (sp == null) return false;
				var server = sp.level().getServer();
				if (server == null) return false;
				var manager = server.getAdvancements();
				var spAdv = sp.getAdvancements();
				for (var e : advancements) {
					var adv = manager.getAdvancement(e);
					if (adv == null) return false;
					if (!spAdv.getOrStartProgress(adv).isDone()) return false;
				}
			}
			return true;
		}

	}

	public record EnchConfig(ArrayList<Enchantment> enchantments, int level, float chance) {

	}

}
