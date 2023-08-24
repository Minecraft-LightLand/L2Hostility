package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.util.math.MathHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private static void generateTraits(LivingEntity le, int lv, HashMap<MobTrait, Integer> traits, int maxModLv) {
		List<MobTrait> list = new ArrayList<>(LHTraits.TRAITS.get().getValues().stream().filter(e -> e.allow(le, lv, maxModLv)).toList());
		int weights = 0;
		for (var e : list) {
			weights += e.getConfig().weight;
		}
		var rand = le.getRandom();
		int level = lv;
		while (level > 0) {
			if (list.size() == 0) break;
			int val = rand.nextInt(weights);
			MobTrait e = list.get(0);
			for (var x : list) {
				val -= x.getConfig().weight;
				if (val <= 0) {
					e = x;
					break;
				}
			}
			weights -= e.getConfig().weight;
			list.remove(e);
			int cost = e.getCost();
			if (cost > level) {
				level--;
				continue;
			}
			int maxLv = Math.min(Math.min(maxModLv, rand.nextInt(level / cost) + 1), e.getMaxLevel());
			if (maxLv == 0) {
				level--;
				continue;
			}
			level -= maxLv * cost;
			traits.put(e, maxLv);
		}
		for (var e : traits.entrySet()) {
			e.getKey().initialize(le, e.getValue());
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
			addAttribute(le, Attributes.MAX_HEALTH, "hostility_health",
					lv * LHConfig.COMMON.healthFactor.get(),
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
				generateTraits(le, lv, traits, ins.getMaxTraitLevel());
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
				manager.getTag(TagGen.VALID_MELEE_WEAPONS).getRandomElement(r)
						.ifPresent(item -> le.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance()));
			}
		}
		// enchant
		for (var e : EquipmentSlot.values()) {
			ItemStack stack = le.getItemBySlot(e);
			if (!stack.isEnchanted() && stack.isEnchantable()) {
				int lvl = 5 + r.nextInt(18) + cap.getEnchantBonus();
				le.setItemSlot(e, EnchantmentHelper.enchantItem(r, stack, lvl, false));
			}
		}
	}

	public static int getMaxLevel() {
		return 5;
	}

	public static int getTraitCap(int maxRankKilled, DifficultyLevel playerDifficulty) {
		return Math.max(maxRankKilled + 1, playerDifficulty.getLevel() / LHConfig.COMMON.traitCapPerLevel.get());
	}

}
