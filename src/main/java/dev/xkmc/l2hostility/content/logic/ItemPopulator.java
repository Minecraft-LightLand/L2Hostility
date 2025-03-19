package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Set;

public class ItemPopulator {

	static void populateArmors(LivingEntity le, int lv) {
		var r = le.getRandom();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				if (le.getItemBySlot(slot).isEmpty()) {
					ItemStack stack = WeaponConfig.getRandomArmor(slot, lv, r);
					if (!stack.isEmpty()) {
						le.setItemSlot(slot, stack);
						if (le instanceof Mob mob) {
							mob.setDropChance(slot, LHConfig.SERVER.equipmentDropRate.get().floatValue());
						}
					}
				}
			}
		}
	}

	static void populateWeapons(LivingEntity le, MobTraitCap cap, RandomSource r) {
		if (le instanceof Drowned && le.getMainHandItem().isEmpty()) {
			double factor = cap.getLevel() * LHConfig.SERVER.drownedTridentChancePerLevel.get();
			if (factor > le.getRandom().nextDouble()) {
				le.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TRIDENT));
			}
		}
		if (le.getType().is(LHTagGen.MELEE_WEAPON_TARGET)) {
			if (le.getMainHandItem().isEmpty()) {
				ItemStack stack = WeaponConfig.getRandomMeleeWeapon(cap.getLevel(), r);
				if (!stack.isEmpty()) {
					le.setItemSlot(EquipmentSlot.MAINHAND, stack);
					if (le instanceof Mob mob) {
						mob.setDropChance(EquipmentSlot.MAINHAND, LHConfig.SERVER.equipmentDropRate.get().floatValue());
					}
				}
			}
		}
		if (le.getType().is(LHTagGen.RANGED_WEAPON_TARGET)) {
			ItemStack stack = WeaponConfig.getRandomRangedWeapon(cap.getLevel(), r);
			if (!stack.isEmpty()) {
				le.setItemSlot(EquipmentSlot.MAINHAND, stack);
				if (le instanceof Mob mob) {
					mob.setDropChance(EquipmentSlot.MAINHAND, LHConfig.SERVER.equipmentDropRate.get().floatValue());
				}
			}
		}
		for (var ent : L2Hostility.WEAPON.getMerged().special_weapons.entrySet()) {
			if (le.getType().is(ent.getKey())) {
				ItemStack stack = WeaponConfig.getRandomWeapon(ent.getValue(), cap.getLevel(), le.getRandom());
				if (!stack.isEmpty()) {
					le.setItemSlot(EquipmentSlot.MAINHAND, stack);
					if (le instanceof Mob mob) {
						mob.setDropChance(EquipmentSlot.MAINHAND, LHConfig.SERVER.equipmentDropRate.get().floatValue());
					}
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

	public static void fillEnch(RegistryAccess access, int level, RandomSource source, ItemStack stack, EquipmentSlot slot) {
		var config = L2Hostility.WEAPON.getMerged();
		if (slot == EquipmentSlot.OFFHAND) return;
		var list = slot == EquipmentSlot.MAINHAND ?
				config.weapon_enchantments : config.armor_enchantments;
		var map = new ItemEnchantments.Mutable(stack.getAllEnchantments(access.lookupOrThrow(Registries.ENCHANTMENT)));
		for (var e : list) {
			int elv = e.level() <= 0 ? 1 : e.level();
			if (elv > level) continue;
			for (var key : e.enchantments()) {
				if (e.chance() < source.nextDouble()) continue;
				var holder = access.holderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, key));
				if (!stack.isPrimaryItemFor(holder)) continue;
				if (!isValid(map.keySet(), holder)) continue;
				int max = Math.min(level / elv, holder.value().getMaxLevel());
				map.set(holder, Math.max(max, map.getLevel(holder)));
			}
		}
		EnchantmentHelper.setEnchantments(stack, map.toImmutable());
	}

	private static boolean isValid(Set<Holder<Enchantment>> old, Holder<Enchantment> ench) {
		for (var other : old) {
			if (ench.equals(other))
				return true;
		}
		for (var other : old) {
			if (!Enchantment.areCompatible(ench, other))
				return false;
		}
		return true;
	}

	public static void postFill(MobTraitCap cap, LivingEntity le) {
		// add weapon
		RandomSource r = le.getRandom();
		populateWeapons(le, cap, r);
		// enchant
		for (var e : EquipmentSlot.values()) {
			ItemStack stack = le.getItemBySlot(e);
			if (!stack.isEnchantable()) continue;
			if (LHConfig.SERVER.allowExtraEnchantments.get())
				fillEnch(le.level().registryAccess(), cap.getLevel(), le.getRandom(), stack, e);
			le.setItemSlot(e, stack);
		}
		var config = cap.getConfigCache(le);
		if (config != null && !config.items.isEmpty()) {
			for (var pool : config.items) {
				generateItems(cap, le, pool);
			}
		}
	}
}
