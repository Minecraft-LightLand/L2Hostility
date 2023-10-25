package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

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
		if (ins.trait_chance() >= le.getRandom().nextDouble()) {
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
		if (le.getType().is(TagGen.MELEE_WEAPON_TARGET)) {
			var manager = ForgeRegistries.ITEMS.tags();
			if (manager != null && le.getMainHandItem().isEmpty()) {
				ItemStack stack = WeaponConfig.getRandomWeapon(cap.getLevel(), r);
				if (!stack.isEmpty()) {
					le.setItemSlot(EquipmentSlot.MAINHAND, stack);
				}
			}
		}
		// enchant
		for (var e : EquipmentSlot.values()) {
			ItemStack stack = le.getItemBySlot(e);
			if (!stack.isEnchanted() && stack.isEnchantable()) {
				float lvl = Mth.clamp(cap.getLevel() * 0.02f, 0, 1) *
						r.nextInt(30) +
						cap.getEnchantBonus();
				le.setItemSlot(e, EnchantmentHelper.enchantItem(r, stack, (int) lvl, false));
			}
		}
	}

	public static int getMaxLevel() {
		return 5;
	}

	public static int getTraitCap(int maxRankKilled, DifficultyLevel diff) {
		return maxRankKilled + 1;
	}

}
