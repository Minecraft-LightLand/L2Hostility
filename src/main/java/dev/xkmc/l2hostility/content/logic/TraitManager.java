package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Set;

public class TraitManager {

	public static void addAttribute(LivingEntity le, Attribute attr, String name, double factor, AttributeModifier.Operation op) {
		var ins = le.getAttribute(attr);
		if (ins == null) return;
		var modifier = new AttributeModifier(MathHelper.getUUIDFromString(name), name, factor, op);
		if (ins.hasModifier(modifier)) {
			ins.removeModifier(modifier.getId());
		}
		ins.addPermanentModifier(modifier);
	}

	private static void populateArmors(LivingEntity le, int lv) {
		int rank = Math.min(4, lv / LHConfig.COMMON.armorFactor.get() - 1);
		if (rank < 0) return;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				ItemStack stack = le.getItemBySlot(slot);
				if (stack.isEmpty()) {
					Item item = Mob.getEquipmentForSlot(slot, rank);
					if (item != null) {
						le.setItemSlot(slot, new ItemStack(item));
					}
				}
			}
		}
	}

	private static void populateWeapons(LivingEntity le, MobTraitCap cap, RandomSource r) {
		var manager = ForgeRegistries.ITEMS.tags();
		if (manager == null) return;
		if (le.getType().is(TagGen.MELEE_WEAPON_TARGET)) {
			if (le.getMainHandItem().isEmpty()) {
				ItemStack stack = WeaponConfig.getRandomMeleeWeapon(cap.getLevel(), r);
				if (!stack.isEmpty()) {
					le.setItemSlot(EquipmentSlot.MAINHAND, stack);
				}
			}
		} else if (le.getType().is(TagGen.RANGED_WEAPON_TARGET)) {
			ItemStack stack = WeaponConfig.getRandomRangedWeapon(cap.getLevel(), r);
			if (!stack.isEmpty()) {
				le.setItemSlot(EquipmentSlot.MAINHAND, stack);
			}
		}
	}

	public static int fill(LivingEntity le, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
		int lv = ins.getDifficulty(le.getRandom());
		int ans = 0;
		if (ins.apply_chance() < le.getRandom().nextDouble()) {
			return ans;
		}
		// add attributes
		if (!le.getType().is(TagGen.NO_SCALING)) {
			double factor;
			if (LHConfig.COMMON.exponentialHealth.get()) {
				factor = Math.pow(1 + LHConfig.COMMON.healthFactor.get(), lv) - 1;
			} else {
				factor = lv * LHConfig.COMMON.healthFactor.get();
			}
			addAttribute(le, Attributes.MAX_HEALTH, "hostility_health", factor,
					AttributeModifier.Operation.MULTIPLY_TOTAL);
			ans = lv;
		}
		// armor
		if (le.getType().is(TagGen.ARMOR_TARGET)) {
			populateArmors(le, lv);
		}
		// add traits

		if (ins.trait_chance(lv) >= le.getRandom().nextDouble()) {
			if (!le.getType().is(TagGen.NO_TRAIT)) {
				TraitGenerator.generateTraits(le, lv, traits, ins);
			}
			ans = lv;
		}
		le.setHealth(le.getMaxHealth());
		return ans;
	}

	public static void postFill(MobTraitCap cap, LivingEntity le) {
		// add weapon
		RandomSource r = le.getRandom();
		populateWeapons(le, cap, r);
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
	}

	public static void fillEnch(int level, RandomSource source, ItemStack stack, EquipmentSlot slot) {
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

	public static int getMaxLevel() {
		return 5;
	}

	public static int getTraitCap(int maxRankKilled, DifficultyLevel diff) {
		return maxRankKilled + 1;
	}

}
