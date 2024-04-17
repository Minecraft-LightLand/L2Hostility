package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.TreeMap;

public class LHConfig {

	public static class Client {

		public final ForgeConfigSpec.BooleanValue showTraitOverHead;
		public final ForgeConfigSpec.BooleanValue showLevelOverHead;
		public final ForgeConfigSpec.IntValue overHeadRenderDistance;
		public final ForgeConfigSpec.BooleanValue overHeadRenderFullBright;
		public final ForgeConfigSpec.IntValue overHeadLevelColor;
		public final ForgeConfigSpec.IntValue overHeadLevelColorAbyss;
		public final ForgeConfigSpec.DoubleValue overHeadRenderOffset;
		public final ForgeConfigSpec.BooleanValue showOnlyWhenHovered;
		public final ForgeConfigSpec.IntValue glowingRangeHidden;
		public final ForgeConfigSpec.IntValue glowingRangeNear;
		public final ForgeConfigSpec.BooleanValue showUndyingParticles;

		Client(ForgeConfigSpec.Builder builder) {
			showTraitOverHead = builder.comment("Render Traits in name plate form")
					.define("showTraitOverHead", true);
			showLevelOverHead = builder.comment("Render mob level in name plate form")
					.define("showLevelOverHead", true);
			overHeadRenderDistance = builder.comment("Name plate render distance")
					.defineInRange("overHeadRenderDistance", 32, 0, 128);
			overHeadRenderOffset = builder.comment("Name plate render offset in lines, upward is positive")
					.defineInRange("overHeadRenderOffset", 0d, -100, 100);
			overHeadRenderFullBright = builder.comment("Overhead render text becomes full bright")
					.define("overHeadRenderFullBright", true);
			overHeadLevelColor = builder.comment("Overhead level color in decimal form, converted from hex form")
					.defineInRange("overHeadLevelColor", 11184810, Integer.MIN_VALUE, Integer.MAX_VALUE);
			overHeadLevelColorAbyss = builder.comment("Overhead level color for mobs affected by abyssal thorn")
					.defineInRange("overHeadLevelColorAbyss", 16733525, Integer.MIN_VALUE, Integer.MAX_VALUE);
			showOnlyWhenHovered = builder.comment("Show nameplate style trait and name only when hovered")
					.define("showOnlyWhenHovered", false);
			glowingRangeHidden = builder.comment("Detector Glasses glowing range for hidden mobs")
					.defineInRange("glowingRangeHidden", 32, 1, 256);
			glowingRangeNear = builder.comment("Detector Glasses glowing range for nearby mobs")
					.defineInRange("glowingRangeNear", 16, 1, 256);
			showUndyingParticles = builder.comment("Render undying particles")
					.define("showUndyingParticles", true);
		}

	}

	public static class Common {

		public final ForgeConfigSpec.IntValue killsPerLevel;
		public final ForgeConfigSpec.IntValue maxPlayerLevel;
		public final ForgeConfigSpec.IntValue newPlayerProtectRange;
		public final ForgeConfigSpec.DoubleValue playerDeathDecay;
		public final ForgeConfigSpec.BooleanValue keepInventoryRuleKeepDifficulty;
		public final ForgeConfigSpec.BooleanValue deathDecayDimension;
		public final ForgeConfigSpec.BooleanValue deathDecayTraitCap;
		public final ForgeConfigSpec.BooleanValue enableEntitySpecificDatapack;
		public final ForgeConfigSpec.DoubleValue healthFactor;
		public final ForgeConfigSpec.BooleanValue exponentialHealth;
		public final ForgeConfigSpec.DoubleValue damageFactor;
		public final ForgeConfigSpec.BooleanValue exponentialDamage;
		public final ForgeConfigSpec.DoubleValue expDropFactor;
		public final ForgeConfigSpec.DoubleValue drownedTridentChancePerLevel;
		public final ForgeConfigSpec.DoubleValue enchantmentFactor;
		public final ForgeConfigSpec.IntValue dimensionFactor;
		public final ForgeConfigSpec.DoubleValue distanceFactor;
		public final ForgeConfigSpec.DoubleValue globalApplyChance;
		public final ForgeConfigSpec.DoubleValue globalTraitChance;
		public final ForgeConfigSpec.DoubleValue globalTraitSuppression;
		public final ForgeConfigSpec.BooleanValue allowLegendary;
		public final ForgeConfigSpec.BooleanValue allowSectionDifficulty;
		public final ForgeConfigSpec.BooleanValue allowBypassMinimum;
		public final ForgeConfigSpec.BooleanValue allowHostilityOrb;
		public final ForgeConfigSpec.BooleanValue allowHostilitySpawner;
		public final ForgeConfigSpec.BooleanValue allowExtraEnchantments;
		public final ForgeConfigSpec.IntValue defaultLevelBase;
		public final ForgeConfigSpec.DoubleValue defaultLevelVar;
		public final ForgeConfigSpec.DoubleValue defaultLevelScale;
		public final ForgeConfigSpec.DoubleValue initialTraitChanceSlope;
		public final ForgeConfigSpec.BooleanValue allowNoAI;
		public final ForgeConfigSpec.BooleanValue allowPlayerAllies;
		public final ForgeConfigSpec.BooleanValue allowTraitOnOwnable;
		public final ForgeConfigSpec.DoubleValue dropRateFromSpawner;

		public final ForgeConfigSpec.IntValue bottleOfCurseLevel;
		public final ForgeConfigSpec.IntValue envyExtraLevel;
		public final ForgeConfigSpec.IntValue greedExtraLevel;
		public final ForgeConfigSpec.IntValue lustExtraLevel;
		public final ForgeConfigSpec.IntValue wrathExtraLevel;
		public final ForgeConfigSpec.IntValue abrahadabraExtraLevel;
		public final ForgeConfigSpec.IntValue nidhoggurExtraLevel;
		public final ForgeConfigSpec.DoubleValue nidhoggurDropFactor;
		public final ForgeConfigSpec.DoubleValue greedDropFactor;
		public final ForgeConfigSpec.DoubleValue envyDropRate;
		public final ForgeConfigSpec.DoubleValue gluttonyBottleDropRate;
		public final ForgeConfigSpec.DoubleValue prideDamageBonus;
		public final ForgeConfigSpec.DoubleValue prideHealthBonus;
		public final ForgeConfigSpec.DoubleValue prideTraitFactor;
		public final ForgeConfigSpec.DoubleValue wrathDamageBonus;
		public final ForgeConfigSpec.BooleanValue disableHostilityLootCurioRequirement;

		public final ForgeConfigSpec.IntValue hostilitySpawnCount;
		public final ForgeConfigSpec.IntValue hostilitySpawnLevelFactor;

		public final ForgeConfigSpec.DoubleValue tankHealth;
		public final ForgeConfigSpec.DoubleValue tankArmor;
		public final ForgeConfigSpec.DoubleValue tankTough;
		public final ForgeConfigSpec.DoubleValue speedy;
		public final ForgeConfigSpec.DoubleValue regen;
		public final ForgeConfigSpec.DoubleValue adaptFactor;
		public final ForgeConfigSpec.DoubleValue reflectFactor;
		public final ForgeConfigSpec.IntValue dispellTime;
		public final ForgeConfigSpec.IntValue fieryTime;
		public final ForgeConfigSpec.IntValue weakTime;
		public final ForgeConfigSpec.IntValue slowTime;
		public final ForgeConfigSpec.IntValue poisonTime;
		public final ForgeConfigSpec.IntValue witherTime;
		public final ForgeConfigSpec.IntValue levitationTime;
		public final ForgeConfigSpec.IntValue blindTime;
		public final ForgeConfigSpec.IntValue confusionTime;
		public final ForgeConfigSpec.IntValue soulBurnerTime;
		public final ForgeConfigSpec.IntValue freezingTime;
		public final ForgeConfigSpec.IntValue curseTime;
		public final ForgeConfigSpec.IntValue teleportDuration;
		public final ForgeConfigSpec.IntValue teleportRange;
		public final ForgeConfigSpec.IntValue repellRange;
		public final ForgeConfigSpec.DoubleValue repellStrength;
		public final ForgeConfigSpec.DoubleValue corrosionDurability;
		public final ForgeConfigSpec.DoubleValue erosionDurability;
		public final ForgeConfigSpec.DoubleValue corrosionDamage;
		public final ForgeConfigSpec.DoubleValue erosionDamage;
		public final ForgeConfigSpec.IntValue ragnarokTime;
		public final ForgeConfigSpec.BooleanValue ragnarokSealBackpack;
		public final ForgeConfigSpec.BooleanValue ragnarokSealSlotAdder;
		public final ForgeConfigSpec.IntValue killerAuraDamage;
		public final ForgeConfigSpec.IntValue killerAuraRange;
		public final ForgeConfigSpec.IntValue killerAuraInterval;
		public final ForgeConfigSpec.IntValue shulkerInterval;
		public final ForgeConfigSpec.IntValue grenadeInterval;
		public final ForgeConfigSpec.DoubleValue drainDamage;
		public final ForgeConfigSpec.DoubleValue drainDuration;
		public final ForgeConfigSpec.IntValue drainDurationMax;
		public final ForgeConfigSpec.IntValue counterStrikeDuration;
		public final ForgeConfigSpec.IntValue counterStrikeRange;
		public final ForgeConfigSpec.IntValue pullingRange;
		public final ForgeConfigSpec.DoubleValue pullingStrength;
		public final ForgeConfigSpec.DoubleValue reprintDamage;
		public final ForgeConfigSpec.IntValue reprintBypass;

		public final ForgeConfigSpec.DoubleValue ringOfLifeMaxDamage;
		public final ForgeConfigSpec.IntValue flameThornTime;
		public final ForgeConfigSpec.IntValue ringOfReflectionRadius;
		public final ForgeConfigSpec.IntValue witchWandFactor;
		public final ForgeConfigSpec.DoubleValue ringOfCorrosionFactor;
		public final ForgeConfigSpec.DoubleValue ringOfCorrosionPenalty;
		public final ForgeConfigSpec.DoubleValue ringOfHealingRate;
		public final ForgeConfigSpec.IntValue witchChargeMinDuration;
		public final ForgeConfigSpec.DoubleValue insulatorFactor;
		public final ForgeConfigSpec.IntValue orbRadius;
		public final ForgeConfigSpec.DoubleValue splitDropRateFactor;

		public final Map<String, ForgeConfigSpec.BooleanValue> map = new TreeMap<>();
		public final Map<String, ForgeConfigSpec.IntValue> range = new TreeMap<>();

		Common(ForgeConfigSpec.Builder builder) {
			enableEntitySpecificDatapack = builder.comment("Allow entity specific difficulty configs to load")
					.define("enableEntitySpecificDatapack", true);
			builder.push("scaling");
			{
				healthFactor = builder.comment("Health factor per level")
						.defineInRange("healthFactor", 0.03, 0, 1000);
				exponentialHealth = builder.comment("Use exponential health")
						.define("exponentialHealth", false);
				damageFactor = builder.comment("Damage factor per level")
						.defineInRange("damageFactor", 0.02, 0, 1000);
				exponentialDamage = builder.comment("Use exponential damage")
						.define("exponentialDamage", false);
				expDropFactor = builder.comment("Experience drop factor per level")
						.defineInRange("expDropFactor", 0.05, 0, 1000);
				drownedTridentChancePerLevel = builder.comment("Chance per level for drowned to hold trident")
						.defineInRange("drownedTridentChancePerLevel", 0.005d, 0, 1000);
				enchantmentFactor = builder.comment("Enchantment bonus per level.",
								"Note: use it only when Apotheosis is installed",
								"Otherwise too high enchantment level will yield no enchantment")
						.defineInRange("enchantmentFactor", 0d, 0, 1000);
				dimensionFactor = builder.comment("Difficulty bonus per level visited")
						.defineInRange("dimensionFactor", 10, 0, 1000);
				distanceFactor = builder.comment("Difficulty bonus per block from origin")
						.defineInRange("distanceFactor", 0.003, 0, 1000);
				globalApplyChance = builder.comment("Chance for health/damage bonus and trait to apply")
						.comment("Not applicable to mobs with minimum level.")
						.defineInRange("globalApplyChance", 1d, 0, 1);
				globalTraitChance = builder.comment("Chance for trait to apply")
						.comment("Not applicable to mobs with minimum level.")
						.defineInRange("globalTraitChance", 1d, 0, 1);
				globalTraitSuppression = builder.comment("Chance to stop adding traits after adding a trait")
						.comment("Not applicable to mobs with minimum level.")
						.defineInRange("globalTraitSuppression", 0.1d, 0, 1);
				allowLegendary = builder.comment("Allow legendary traits")
						.define("allowLegendary", true);
				allowSectionDifficulty = builder.comment("Allow chunk section to accumulate difficulty")
						.define("allowSectionDifficulty", true);
				allowBypassMinimum = builder.comment("Allow difficulty clearing bypass mob minimum level")
						.define("allowBypassMinimum", true);
				allowExtraEnchantments = builder.comment("Allow level-related extra enchantment spawning")
						.define("allowExtraEnchantments", true);
				defaultLevelBase = builder.comment("Default dimension base difficulty for mod dimensions")
						.defineInRange("defaultLevelBase", 20, 0, 1000);
				defaultLevelVar = builder.comment("Default dimension difficulty variation for mod dimensions")
						.defineInRange("defaultLevelVar", 16d, 0, 1000);
				defaultLevelScale = builder.comment("Default dimension difficulty scale for mod dimensions")
						.defineInRange("defaultLevelScale", 1.5, 0, 10);
				initialTraitChanceSlope = builder.comment("Mobs at Lv.N will have N x k% chance to have trait")
						.comment("Default k% = 0.01, so Lv.N mobs with have N% chance to have trait")
						.comment("Mobs with entity config and trait chance of 1 will not be affected")
						.defineInRange("initialTraitChanceSlope", 0.01, 0, 1);
				splitDropRateFactor = builder.comment("Slimes hostility loot drop rate decay per split")
						.defineInRange("splitDropRateFactor", 0.25d, 0, 1);
				allowNoAI = builder.comment("Allow mobs without AI to have levels")
						.define("allowNoAI", false);
				allowPlayerAllies = builder.comment("Allow mobs allied to player to have levels")
						.define("allowPlayerAllies", false);
				allowTraitOnOwnable = builder.comment("Keep traits on mobs tamed by player")
						.define("allowTraitOnOwnable", false);
				dropRateFromSpawner = builder.comment("Drop rate of hostility loot from mobs from spawner")
						.defineInRange("dropRateFromSpawner", 0.5d, 0, 1);

			}
			builder.pop();

			builder.push("difficulty");
			{
				maxPlayerLevel = builder.comment("Max player adaptive level")
						.defineInRange("maxPlayerLevel", 2000, 600, 100000);
				killsPerLevel = builder.comment("Difficulty increment takes this many kills of same level mob")
						.defineInRange("killsPerLevel", 30, 1, 100000);
				playerDeathDecay = builder.comment("Decay in player difficulty on death")
						.defineInRange("playerDeathDecay", 0.8, 0, 2);
				keepInventoryRuleKeepDifficulty = builder.comment("Allow KeepInventory to keep difficulty as well")
						.define("keepInventoryRuleKeepDifficulty", false);
				deathDecayDimension = builder.comment("On player death, clear dimension penalty")
						.define("deathDecayDimension", true);
				deathDecayTraitCap = builder.comment("On player death, reduce max trait spawned by 1")
						.define("deathDecayTraitCap", true);
				newPlayerProtectRange = builder.comment("Mobs spawned within this range will use lowest player level in range instead of nearest player's level to determine mob level")
						.defineInRange("newPlayerProtectRange", 48, 0, 128);
			}
			builder.pop();

			builder.push("orb_and_spawner");
			{
				allowHostilityOrb = builder.comment("Allow to use hostility orb")
						.define("allowHostilityOrb", true);
				orbRadius = builder.comment("Radius for Hostility Orb to take effect.")
						.comment("0 means 1x1x1 section, 1 means 3x3x3 sections, 2 means 5x5x5 sections")
						.defineInRange("orbRadius", 2, 0, 10);
				allowHostilitySpawner = builder.comment("Allow to use hostility spawner")
						.define("allowHostilitySpawner", true);
				hostilitySpawnCount = builder.comment("Number of mobs to spawn in Hostility Spawner")
						.defineInRange("hostilitySpawnCount", 16, 1, 64);
				hostilitySpawnLevelFactor = builder.comment("Level bonus factor for mobs to spawn in Hostility Spawner")
						.defineInRange("hostilitySpawnLevelFactor", 2, 1, 10000);
			}
			builder.pop();

			builder.push("items");
			{
				disableHostilityLootCurioRequirement = builder.comment("Disable curio requirement for hostility loot")
						.define("disableHostilityLootCurioRequirement", false);
				bottleOfCurseLevel = builder.comment("Number of level to add when using bottle of curse")
						.defineInRange("bottleOfCurseLevel", 50, 1, 1000);

				witchChargeMinDuration = builder.comment("Minimum duration for witch charge to be effective, in ticks")
						.defineInRange("witchChargeMinDuration", 200, 20, 10000);

				ringOfLifeMaxDamage = builder.comment("Max percentage of max health a damage can hurt wearer of Ring of Life")
						.defineInRange("ringOfLifeMaxDamage", 0.9, 0, 1);

				flameThornTime = builder.comment("Time in ticks of Soul Flame to inflict")
						.defineInRange("flameThornTime", 100, 1, 10000);

				ringOfReflectionRadius = builder.comment("Radius in blocks for Ring of Reflection to work")
						.defineInRange("ringOfReflectionRadius", 16, 1, 256);

				witchWandFactor = builder.comment("Factor of effect duration for witch wand, to make up for splash decay")
						.defineInRange("witchWandFactor", 4, 1, 100);

				ringOfCorrosionFactor = builder.comment("Factor of maximum durability to cost for ring of corrosion")
						.defineInRange("ringOfCorrosionFactor", 0.2, 0, 1);

				ringOfCorrosionPenalty = builder.comment("Penalty of maximum durability to cost for ring of corrosion")
						.defineInRange("ringOfCorrosionPenalty", 0.1, 0, 1);

				ringOfHealingRate = builder.comment("Percentage of health to heal every second")
						.defineInRange("ringOfHealingRate", 0.05, 0, 1);

				// curse
				{
					envyExtraLevel = builder.comment("Number of level to add when using Curse of Envy")
							.defineInRange("envyExtraLevel", 50, 0, 1000);
					greedExtraLevel = builder.comment("Number of level to add when using Curse of Greed")
							.defineInRange("greedExtraLevel", 50, 0, 1000);
					lustExtraLevel = builder.comment("Number of level to add when using Curse of Lust")
							.defineInRange("lustExtraLevel", 50, 0, 1000);
					wrathExtraLevel = builder.comment("Number of level to add when using Curse of Wrath")
							.defineInRange("wrathExtraLevel", 50, 0, 1000);

					greedDropFactor = builder.comment("Hostility loot drop factor when using Curse of Greed")
							.defineInRange("greedDropFactor", 2d, 1, 10);
					envyDropRate = builder.comment("Trait item drop rate per rank when using Curse of Envy")
							.defineInRange("envyDropRate", 0.02, 0, 1);
					gluttonyBottleDropRate = builder.comment("Bottle of Curse drop rate per level when using Curse of Gluttony")
							.defineInRange("gluttonyBottleDropRate", 0.02, 0, 1);
					wrathDamageBonus = builder.comment("Damage bonus per level difference when using Curse of Wrath")
							.defineInRange("wrathDamageBonus", 0.05, 0, 1);
					prideDamageBonus = builder.comment("Damage bonus per level when using Curse of Pride")
							.defineInRange("prideDamageBonus", 0.02, 0, 1);
					prideHealthBonus = builder.comment("Health boost per level in percentage when using Curse of Pride")
							.defineInRange("prideHealthBonus", 0.02, 0, 1);
					prideTraitFactor = builder.comment("Trait cost multiplier when using Curse of Pride")
							.defineInRange("prideTraitFactor", 0.5, 0.01, 1);
				}


				abrahadabraExtraLevel = builder.comment("Number of level to add when using Abrahadabra")
						.defineInRange("abrahadabraExtraLevel", 100, 0, 1000);
				nidhoggurExtraLevel = builder.comment("Number of level to add when using Greed of Nidhoggur")
						.defineInRange("nidhoggurExtraLevel", 100, 0, 1000);
				nidhoggurDropFactor = builder.comment("All loot drop factor when using Greed of Nidhoggur")
						.defineInRange("nidhoggurDropFactor", 0.01, 0, 10);

				insulatorFactor = builder.comment("Insulator Enchantment factor for reducing pushing")
						.defineInRange("insulatorFactor", 0.8, 0, 1);
			}
			builder.pop();

			LHTraits.register();

			builder.push("traits");
			{
				tankHealth = builder.comment("Health bonus for Tank trait per level")
						.defineInRange("tankHealth", 0.5, 0, 1000);
				tankArmor = builder.comment("Armor bonus for Tank trait per level")
						.defineInRange("tankArmor", 10d, 0, 1000);
				tankTough = builder.comment("Toughness bonus for Tank trait per level")
						.defineInRange("tankTough", 4d, 0, 1000);
				speedy = builder.comment("Speed bonus for Speedy trait per level")
						.defineInRange("speedy", 0.2, 0, 1000);
				regen = builder.comment("Regen rate for Regeneration trait per second per level")
						.defineInRange("regen", 0.02, 0, 1000);
				adaptFactor = builder.comment("Damage factor for Adaptive. Higher means less reduction")
						.defineInRange("adaptFactor", 0.5, 0, 1000);
				reflectFactor = builder.comment("Reflect factor per level for Reflect. 0.5 means +50% extra damage")
						.defineInRange("reflectFactor", 0.3, 0, 1000);
				dispellTime = builder.comment("Duration in ticks for enchantments to be disabled per level for Dispell")
						.defineInRange("dispellTime", 200, 1, 60000);
				fieryTime = builder.comment("Duration in seconds to set target on fire by Fiery")
						.defineInRange("fieryTime", 5, 0, 3000);
				weakTime = builder.comment("Duration in ticks for Weakness")
						.defineInRange("weakTime", 200, 0, 3000);
				slowTime = builder.comment("Duration in ticks for Slowness")
						.defineInRange("slowTime", 200, 0, 3000);
				poisonTime = builder.comment("Duration in ticks for Poison")
						.defineInRange("poisonTime", 200, 0, 3000);
				witherTime = builder.comment("Duration in ticks for Wither")
						.defineInRange("witherTime", 200, 0, 3000);
				levitationTime = builder.comment("Duration in ticks for Levitation")
						.defineInRange("levitationTime", 200, 0, 3000);
				blindTime = builder.comment("Duration in ticks for Blindness")
						.defineInRange("blindTime", 200, 0, 3000);
				confusionTime = builder.comment("Duration in ticks for Nausea")
						.defineInRange("confusionTime", 200, 0, 3000);
				soulBurnerTime = builder.comment("Duration in ticks for Soul Burner")
						.defineInRange("soulBurnerTime", 60, 0, 3000);
				freezingTime = builder.comment("Duration in ticks for Freezing")
						.defineInRange("freezingTime", 200, 0, 3000);
				curseTime = builder.comment("Duration in ticks for Cursed")
						.defineInRange("curseTime", 200, 0, 3000);
				teleportDuration = builder.comment("Interval in ticks for Teleport")
						.defineInRange("teleportDuration", 100, 0, 3000);
				teleportRange = builder.comment("Range in blocks for Teleport")
						.defineInRange("teleportRange", 16, 0, 64);
				repellRange = builder.comment("Range in blocks for Repell")
						.defineInRange("repellRange", 10, 0, 64);
				repellStrength = builder.comment("Repell force strength, default is 0.2")
						.defineInRange("repellStrength", 0.2, 0, 1);

				corrosionDurability = builder.comment("Fraction of remaining durability to corrode, per trait rank")
						.defineInRange("corrosionDurability", 0.3, 0, 1);
				corrosionDamage = builder.comment("Damage bonus when nothing to corrode")
						.defineInRange("corrosionDamage", 0.25, 0, 1);
				erosionDurability = builder.comment("Fraction of lost durability to erode, per trait rank")
						.defineInRange("erosionDurability", 0.1, 0, 1);
				erosionDamage = builder.comment("Damage bonus when nothing to erode")
						.defineInRange("erosionDamage", 0.25, 0, 1);
				ragnarokTime = builder.comment("Seal time per level for Ragnarok")
						.defineInRange("ragnarokTime", 20, 1, 1000);
				ragnarokSealBackpack = builder.comment("Allow Ragnarok to seal items with Backpack in its id")
						.define("ragnarokSealBackpack", false);
				ragnarokSealSlotAdder = builder.comment("Allow Ragnarok to seal curios items that adds curios slot")
						.define("ragnarokSealSlotAdder", false);
				killerAuraDamage = builder.comment("Damage for killer aura")
						.defineInRange("killerAuraDamage", 10, 1, 10000);
				killerAuraRange = builder.comment("Range for for killer aura")
						.defineInRange("killerAuraRange", 6, 1, 32);
				killerAuraInterval = builder.comment("Interval for for killer aura")
						.defineInRange("killerAuraInterval", 120, 1, 10000);

				shulkerInterval = builder.comment("Interval for for shulker")
						.defineInRange("shulkerInterval", 40, 1, 10000);
				grenadeInterval = builder.comment("Interval for for explode shulker")
						.defineInRange("explodeShulkerInterval", 60, 1, 10000);
				drainDamage = builder.comment("Damage bonus for each negative effects")
						.defineInRange("drainDamage", 0.1, 0, 100);
				drainDuration = builder.comment("Duration boost for negative effects")
						.defineInRange("drainDuration", 0.50, 0, 100);
				drainDurationMax = builder.comment("Max duration boost for negative effects")
						.defineInRange("drainDurationMax", 1200, 0, 10000);

				counterStrikeDuration = builder.comment("Interval in ticks for Counter Strike")
						.defineInRange("counterStrikeDuration", 100, 0, 3000);
				counterStrikeRange = builder.comment("Range in blocks for Counter Strike")
						.defineInRange("counterStrikeRange", 6, 0, 64);

				pullingRange = builder.comment("Range in blocks for Pulling")
						.defineInRange("pullingRange", 10, 0, 64);
				pullingStrength = builder.comment("Pulling force strength, default is 0.2")
						.defineInRange("pullingStrength", 0.2, 0, 10);
				reprintDamage = builder.comment("Reprint damage factor per enchantment point")
						.defineInRange("reprintDamage", 0.02, 0, 1);
				reprintBypass = builder.comment("Reprint will gain Void Touch 20 and Vanishing Curse when it hits a mob with max Enchantment level of X or higher")
						.defineInRange("reprintBypass", 10, 0, 10000);

				effectAura(builder, "gravity", 10);
				effectAura(builder, "moonwalk", 10);
				effectAura(builder, "arena", 24);


			}
			builder.pop();

			builder.push("Trait toggle");
			for (var e : L2Hostility.REGISTRATE.getList()) {
				map.put(e, builder.define("allow_" + e, true));
			}
			builder.pop();
		}

		private void effectAura(ForgeConfigSpec.Builder builder, String str, int def) {
			range.put(str, builder.comment("Effect range for trait " + str)
					.defineInRange(str + "Range", def, 0, 100));
		}

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}

	public static void init() {
		register(ModConfig.Type.CLIENT, CLIENT_SPEC);
		register(ModConfig.Type.COMMON, COMMON_SPEC);
	}

	private static void register(ModConfig.Type type, IConfigSpec<?> spec) {
		var mod = ModLoadingContext.get().getActiveContainer();
		String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
		ModLoadingContext.get().registerConfig(type, spec, path);
	}

}
