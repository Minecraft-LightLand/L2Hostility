package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.mob_weapon_api.example.vanilla.VanillaMobManager;
import dev.xkmc.mob_weapon_api.init.MobWeaponAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Set;

public class ItemPopulator {

	static void populateArmors(LivingEntity le, int lv, @Nullable ServerPlayer sp) {
		if (isApothBoss(le)) return;
		var r = le.getRandom();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				if (le.getItemBySlot(slot).isEmpty()) {
					ItemStack stack = WeaponConfig.getRandomArmor(slot, lv, r, sp);
					if (!stack.isEmpty()) {
						le.setItemSlot(slot, stack);
						if (le instanceof Mob mob) {
							mob.setDropChance(slot, LHConfig.COMMON.equipmentDropRate.get().floatValue());
						}
					}
				}
			}
		}
	}

	static void populateWeapons(LivingEntity le, MobTraitCap cap, RandomSource r, @Nullable ServerPlayer sp) {
		if (isApothBoss(le) || isApothWeapon(le.getMainHandItem())) return;
		populateSimpleWeapons(le, cap, r, sp);
		ArrayList<WeaponConfig.ItemConfig> list = new ArrayList<>();
		for (var ent : L2Hostility.WEAPON.getMerged().special_weapons.entrySet()) {
			if (ent.getKey().contains(le.getType())) {
				for (var e : ent.getValue()) {
					boolean nonEmpty = false;
					for (var stack : e.stack()) {
						nonEmpty |= !stack.isEmpty();
					}
					if (!nonEmpty) continue;
					list.add(e);
				}
			}
		}
		if (list.isEmpty()) return;
		list.add(WeaponConfig.ItemConfig.EMPTY);
		ItemStack stack = WeaponConfig.getRandomWeapon(list, cap.getLevel(), le.getRandom(), sp);
		if (stack.isEmpty()) return;
		le.setItemSlot(EquipmentSlot.MAINHAND, stack);
		if (le instanceof PathfinderMob e && VanillaMobManager.attachGoal(e, stack)) {
			e.addTag(MobWeaponAPI.MODID + "_applied");
		}
		if (le instanceof Mob mob) {
			mob.setDropChance(EquipmentSlot.MAINHAND, LHConfig.COMMON.equipmentDropRate.get().floatValue());
		}
	}

	static void populateSimpleWeapons(LivingEntity le, MobTraitCap cap, RandomSource r, @Nullable ServerPlayer sp) {
		if (le instanceof Drowned && le.getMainHandItem().isEmpty()) {
			double factor = cap.getLevel() * LHConfig.COMMON.drownedTridentChancePerLevel.get();
			if (factor > le.getRandom().nextDouble()) {
				le.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TRIDENT));
				return;
			}
		}
		if (le.getType().is(LHTagGen.MELEE_WEAPON_TARGET)) {
			if (le.getMainHandItem().isEmpty()) {
				ItemStack stack = WeaponConfig.getRandomMeleeWeapon(cap.getLevel(), r, sp);
				if (!stack.isEmpty()) {
					le.setItemSlot(EquipmentSlot.MAINHAND, stack);
					if (le instanceof Mob mob) {
						mob.setDropChance(EquipmentSlot.MAINHAND, LHConfig.COMMON.equipmentDropRate.get().floatValue());
						return;
					}
				}
			}
		}
		if (le.getType().is(LHTagGen.RANGED_WEAPON_TARGET)) {
			ItemStack stack = WeaponConfig.getRandomRangedWeapon(cap.getLevel(), r, sp);
			if (!stack.isEmpty()) {
				le.setItemSlot(EquipmentSlot.MAINHAND, stack);
				if (le instanceof Mob mob) {
					mob.setDropChance(EquipmentSlot.MAINHAND, LHConfig.COMMON.equipmentDropRate.get().floatValue());
				}
			}
		}
	}

	static void generateItems(MobTraitCap cap, LivingEntity le, EntityConfig.ItemPool pool) {
		if (cap.getLevel() < pool.level()) return;
		if (le.getRandom().nextFloat() > pool.chance()) return;
		var slot = CurioCompat.decode(pool.slot(), le);
		if (slot == null) return;
		var list = pool.entries();
		int total = 0;
		for (var e : list) total += e.weight();
		if (total <= 0) return;
		total = le.getRandom().nextInt(total);
		for (var e : list) {
			total -= e.weight();
			if (total <= 0) {
				slot.set(e.stack().copy());
				return;
			}
		}
	}

	public static void fillEnch(int level, RandomSource source, ItemStack stack, EquipmentSlot slot) {
		if (isApothWeapon(stack)) return;
		var config = L2Hostility.WEAPON.getMerged();
		if (slot == EquipmentSlot.OFFHAND) return;
		var list = slot == EquipmentSlot.MAINHAND ?
				config.weapon_enchantments : config.armor_enchantments;
		var map = stack.getAllEnchantments();
		for (var e : list) {
			int elv = e.level() <= 0 ? 1 : e.level();
			if (elv > level) continue;
			for (var ench : e.enchantments()) {
				if (e.chance() < source.nextDouble()) continue;
				if (!stack.canApplyAtEnchantingTable(ench)) continue;
				if (!isValid(map.keySet(), ench)) continue;
				int max = Math.min(level / elv, ench.getMaxLevel());
				map.put(ench, Math.max(max, map.getOrDefault(ench, 0)));
			}
		}
		EnchantmentHelper.setEnchantments(map, stack);
	}

	private static boolean isValid(Set<Enchantment> old, Enchantment ench) {
		for (var other : old) {
			if (ench == other)
				return true;
		}
		for (var other : old) {
			if (!ench.isCompatibleWith(other))
				return false;
		}
		return true;
	}

	public static void postFill(MobTraitCap cap, LivingEntity le) {
		ServerPlayer sp = PlayerFinder.getNearestPlayer(le.level(), le) instanceof ServerPlayer player ? player : null;
		// add weapon
		RandomSource r = le.getRandom();
		populateWeapons(le, cap, r, sp);
		// enchant
		for (var e : EquipmentSlot.values()) {
			ItemStack stack = le.getItemBySlot(e);
			if (!stack.isEnchantable()) continue;
			if (!stack.isEnchanted()) {
				float lvl = Mth.clamp(cap.getLevel() * 0.02f, 0, 1) *
						r.nextInt(30) +
						cap.getEnchantBonus();
				stack = EnchantmentHelper.enchantItem(r, stack, (int) lvl, false);
			}
			if (LHConfig.COMMON.allowExtraEnchantments.get())
				fillEnch(cap.getLevel(), le.getRandom(), stack, e);
			le.setItemSlot(e, stack);
		}
		var config = cap.getConfigCache(le);
		if (config != null && !config.items.isEmpty()) {
			for (var pool : config.items) {
				generateItems(cap, le, pool);
			}
		}
	}

	private static boolean isApothBoss(LivingEntity mob) {
		return mob.getPersistentData().getBoolean("apoth.boss");
	}

	private static boolean isApothWeapon(ItemStack stack) {
		var tag = stack.getTag();
		return tag != null && tag.getBoolean("apoth_boss");
	}

}
