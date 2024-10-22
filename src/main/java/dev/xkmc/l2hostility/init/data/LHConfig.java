package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;
import java.util.TreeMap;

public class LHConfig {

	public static class Client extends ConfigInit {

		public final ModConfigSpec.BooleanValue showTraitOverHead;
		public final ModConfigSpec.BooleanValue showLevelOverHead;
		public final ModConfigSpec.IntValue overHeadRenderDistance;
		public final ModConfigSpec.BooleanValue overHeadRenderFullBright;
		public final ModConfigSpec.IntValue overHeadLevelColor;
		public final ModConfigSpec.IntValue overHeadLevelColorAbyss;
		public final ModConfigSpec.DoubleValue overHeadRenderOffset;
		public final ModConfigSpec.BooleanValue showOnlyWhenHovered;
		public final ModConfigSpec.IntValue glowingRangeHidden;
		public final ModConfigSpec.IntValue glowingRangeNear;
		public final ModConfigSpec.BooleanValue showUndyingParticles;

		Client(Builder builder) {
			markL2();
			showTraitOverHead = builder.text("Render Traits in name plate form")
					.define("showTraitOverHead", true);
			showLevelOverHead = builder.text("Render mob level in name plate form")
					.define("showLevelOverHead", true);
			overHeadRenderDistance = builder.text("Name plate render distance")
					.defineInRange("overHeadRenderDistance", 32, 0, 128);
			overHeadRenderOffset = builder.text("Name plate render offset in lines, upward is positive")
					.defineInRange("overHeadRenderOffset", 0d, -100, 100);
			overHeadRenderFullBright = builder.text("Overhead render text becomes full bright")
					.define("overHeadRenderFullBright", true);
			overHeadLevelColor = builder.text("Overhead level color in decimal form, converted from hex form")
					.defineInRange("overHeadLevelColor", 11184810, Integer.MIN_VALUE, Integer.MAX_VALUE);
			overHeadLevelColorAbyss = builder.text("Overhead level color for mobs affected by abyssal thorn")
					.defineInRange("overHeadLevelColorAbyss", 16733525, Integer.MIN_VALUE, Integer.MAX_VALUE);
			showOnlyWhenHovered = builder.text("Show nameplate style trait and name only when hovered")
					.define("showOnlyWhenHovered", false);
			glowingRangeHidden = builder.text("Detector Glasses glowing range for hidden mobs")
					.defineInRange("glowingRangeHidden", 32, 1, 256);
			glowingRangeNear = builder.text("Detector Glasses glowing range for nearby mobs")
					.defineInRange("glowingRangeNear", 16, 1, 256);
			showUndyingParticles = builder.text("Render undying particles")
					.define("showUndyingParticles", true);
		}

	}

	public static class Server extends ConfigInit {

		public final ModConfigSpec.IntValue killsPerLevel;
		public final ModConfigSpec.IntValue maxPlayerLevel;
		public final ModConfigSpec.IntValue maxMobLevel;
		public final ModConfigSpec.IntValue newPlayerProtectRange;
		public final ModConfigSpec.DoubleValue playerDeathDecay;
		public final ModConfigSpec.BooleanValue keepInventoryRuleKeepDifficulty;
		public final ModConfigSpec.BooleanValue deathDecayDimension;
		public final ModConfigSpec.BooleanValue deathDecayTraitCap;
		public final ModConfigSpec.BooleanValue enableEntitySpecificDatapack;
		public final ModConfigSpec.BooleanValue enableStructureSpecificDatapack;
		public final ModConfigSpec.DoubleValue healthFactor;
		public final ModConfigSpec.BooleanValue exponentialHealth;
		public final ModConfigSpec.DoubleValue damageFactor;
		public final ModConfigSpec.BooleanValue exponentialDamage;
		public final ModConfigSpec.DoubleValue expDropFactor;
		public final ModConfigSpec.DoubleValue drownedTridentChancePerLevel;
		public final ModConfigSpec.DoubleValue enchantmentFactor;
		public final ModConfigSpec.IntValue dimensionFactor;
		public final ModConfigSpec.DoubleValue distanceFactor;
		public final ModConfigSpec.DoubleValue globalApplyChance;
		public final ModConfigSpec.DoubleValue globalTraitChance;
		public final ModConfigSpec.DoubleValue globalTraitSuppression;
		public final ModConfigSpec.BooleanValue allowLegendary;
		public final ModConfigSpec.BooleanValue allowSectionDifficulty;
		public final ModConfigSpec.BooleanValue allowBypassMinimum;
		public final ModConfigSpec.BooleanValue allowHostilityOrb;
		public final ModConfigSpec.BooleanValue allowHostilitySpawner;
		public final ModConfigSpec.BooleanValue allowExtraEnchantments;
		public final ModConfigSpec.IntValue defaultLevelBase;
		public final ModConfigSpec.DoubleValue defaultLevelVar;
		public final ModConfigSpec.DoubleValue defaultLevelScale;
		public final ModConfigSpec.DoubleValue initialTraitChanceSlope;
		public final ModConfigSpec.BooleanValue allowNoAI;
		public final ModConfigSpec.BooleanValue allowPlayerAllies;
		public final ModConfigSpec.BooleanValue allowTraitOnOwnable;
		public final ModConfigSpec.DoubleValue dropRateFromSpawner;

		public final ModConfigSpec.IntValue bottleOfCurseLevel;
		public final ModConfigSpec.IntValue envyExtraLevel;
		public final ModConfigSpec.IntValue greedExtraLevel;
		public final ModConfigSpec.IntValue lustExtraLevel;
		public final ModConfigSpec.IntValue wrathExtraLevel;
		public final ModConfigSpec.IntValue abrahadabraExtraLevel;
		public final ModConfigSpec.IntValue nidhoggurExtraLevel;
		public final ModConfigSpec.DoubleValue nidhoggurDropFactor;
		public final ModConfigSpec.DoubleValue greedDropFactor;
		public final ModConfigSpec.DoubleValue envyDropRate;
		public final ModConfigSpec.DoubleValue gluttonyBottleDropRate;
		public final ModConfigSpec.DoubleValue prideDamageBonus;
		public final ModConfigSpec.DoubleValue prideHealthBonus;
		public final ModConfigSpec.DoubleValue prideTraitFactor;
		public final ModConfigSpec.DoubleValue wrathDamageBonus;
		public final ModConfigSpec.BooleanValue disableHostilityLootCurioRequirement;
		public final ModConfigSpec.BooleanValue banBottles;
		public final ModConfigSpec.BooleanValue nidhoggurCapAtItemMaxStack;
		public final ModConfigSpec.BooleanValue bookOfReprintSpread;

		public final ModConfigSpec.IntValue hostilitySpawnCount;
		public final ModConfigSpec.IntValue hostilitySpawnLevelFactor;

		public final ModConfigSpec.DoubleValue tankHealth;
		public final ModConfigSpec.DoubleValue tankArmor;
		public final ModConfigSpec.DoubleValue tankTough;
		public final ModConfigSpec.DoubleValue speedy;
		public final ModConfigSpec.DoubleValue regen;
		public final ModConfigSpec.DoubleValue adaptFactor;
		public final ModConfigSpec.DoubleValue reflectFactor;
		public final ModConfigSpec.IntValue dispellTime;
		public final ModConfigSpec.IntValue fieryTime;
		public final ModConfigSpec.IntValue weakTime;
		public final ModConfigSpec.IntValue slowTime;
		public final ModConfigSpec.IntValue poisonTime;
		public final ModConfigSpec.IntValue witherTime;
		public final ModConfigSpec.IntValue levitationTime;
		public final ModConfigSpec.IntValue blindTime;
		public final ModConfigSpec.IntValue confusionTime;
		public final ModConfigSpec.IntValue soulBurnerTime;
		public final ModConfigSpec.IntValue freezingTime;
		public final ModConfigSpec.IntValue curseTime;
		public final ModConfigSpec.IntValue teleportDuration;
		public final ModConfigSpec.IntValue teleportRange;
		public final ModConfigSpec.IntValue repellRange;
		public final ModConfigSpec.DoubleValue repellStrength;
		public final ModConfigSpec.DoubleValue corrosionDurability;
		public final ModConfigSpec.DoubleValue erosionDurability;
		public final ModConfigSpec.DoubleValue corrosionDamage;
		public final ModConfigSpec.DoubleValue erosionDamage;
		public final ModConfigSpec.IntValue ragnarokTime;
		public final ModConfigSpec.BooleanValue ragnarokSealBackpack;
		public final ModConfigSpec.BooleanValue ragnarokSealSlotAdder;
		public final ModConfigSpec.IntValue killerAuraDamage;
		public final ModConfigSpec.IntValue killerAuraRange;
		public final ModConfigSpec.IntValue killerAuraInterval;
		public final ModConfigSpec.IntValue shulkerInterval;
		public final ModConfigSpec.IntValue grenadeInterval;
		public final ModConfigSpec.DoubleValue drainDamage;
		public final ModConfigSpec.DoubleValue drainDuration;
		public final ModConfigSpec.IntValue drainDurationMax;
		public final ModConfigSpec.IntValue counterStrikeDuration;
		public final ModConfigSpec.IntValue counterStrikeRange;
		public final ModConfigSpec.IntValue pullingRange;
		public final ModConfigSpec.DoubleValue pullingStrength;
		public final ModConfigSpec.DoubleValue reprintDamage;
		public final ModConfigSpec.IntValue reprintBypass;

		public final ModConfigSpec.DoubleValue ringOfLifeMaxDamage;
		public final ModConfigSpec.IntValue flameThornTime;
		public final ModConfigSpec.IntValue ringOfReflectionRadius;
		public final ModConfigSpec.IntValue witchWandFactor;
		public final ModConfigSpec.DoubleValue ringOfCorrosionFactor;
		public final ModConfigSpec.DoubleValue ringOfCorrosionPenalty;
		public final ModConfigSpec.DoubleValue ringOfHealingRate;
		public final ModConfigSpec.IntValue witchChargeMinDuration;
		public final ModConfigSpec.DoubleValue insulatorFactor;
		public final ModConfigSpec.IntValue orbRadius;
		public final ModConfigSpec.DoubleValue splitDropRateFactor;
		public final ModConfigSpec.DoubleValue equipmentDropRate;
		public final ModConfigSpec.BooleanValue enableHostilityOrbDrop;

		public final ModConfigSpec.BooleanValue enableCurioCheckFilter;
		public final ModConfigSpec.IntValue removeTraitCheckInterval;
		public final ModConfigSpec.IntValue auraEffectApplicationInterval;
		public final ModConfigSpec.IntValue selfEffectApplicationInterval;

		public final Map<String, ModConfigSpec.BooleanValue> map = new TreeMap<>();
		public final Map<String, ModConfigSpec.IntValue> range = new TreeMap<>();

		Server(Builder builder) {
			markL2();
			enableEntitySpecificDatapack = builder.text("Allow entity specific difficulty configs to load")
					.define("enableEntitySpecificDatapack", true);
			enableStructureSpecificDatapack = builder.text("Allow structure specific difficulty configs to load")
					.define("enableStructureSpecificDatapack", true);
			builder.push("scaling", "Mob Scaling");
			{
				healthFactor = builder.text("Health factor per level")
						.defineInRange("healthFactor", 0.03, 0, 1000);
				exponentialHealth = builder.text("Use exponential health")
						.define("exponentialHealth", false);
				damageFactor = builder.text("Damage factor per level")
						.defineInRange("damageFactor", 0.02, 0, 1000);
				exponentialDamage = builder.text("Use exponential damage")
						.define("exponentialDamage", false);
				expDropFactor = builder.text("Experience drop factor per level")
						.defineInRange("expDropFactor", 0.05, 0, 1000);
				drownedTridentChancePerLevel = builder.text("Chance per level for drowned to hold trident")
						.defineInRange("drownedTridentChancePerLevel", 0.005d, 0, 1000);
				enchantmentFactor = builder.text("Enchantment bonus per level.").comment(
								"Note: use it only when Apotheosis is installed",
								"Otherwise too high enchantment level will yield no enchantment")
						.defineInRange("enchantmentFactor", 0d, 0, 1000);
				dimensionFactor = builder.text("Difficulty bonus per level visited")
						.defineInRange("dimensionFactor", 10, 0, 1000);
				distanceFactor = builder.text("Difficulty bonus per block from origin")
						.defineInRange("distanceFactor", 0.003, 0, 1000);
				globalApplyChance = builder.text("Chance for health/damage bonus and trait to apply")
						.comment("Not applicable to mobs with minimum level.")
						.defineInRange("globalApplyChance", 1d, 0, 1);
				globalTraitChance = builder.text("Chance for trait to apply")
						.comment("Not applicable to mobs with minimum level.")
						.defineInRange("globalTraitChance", 1d, 0, 1);
				globalTraitSuppression = builder.text("Chance to stop adding traits after adding a trait")
						.comment("Not applicable to mobs with minimum level.")
						.defineInRange("globalTraitSuppression", 0.1d, 0, 1);
				allowLegendary = builder.text("Allow legendary traits")
						.define("allowLegendary", true);
				allowSectionDifficulty = builder.text("Allow chunk section to accumulate difficulty")
						.define("allowSectionDifficulty", true);
				allowBypassMinimum = builder.text("Allow difficulty clearing bypass mob minimum level")
						.define("allowBypassMinimum", true);
				allowExtraEnchantments = builder.text("Allow level-related extra enchantment spawning")
						.define("allowExtraEnchantments", true);
				defaultLevelBase = builder.text("Default dimension base difficulty for mod dimensions")
						.defineInRange("defaultLevelBase", 20, 0, 1000);
				defaultLevelVar = builder.text("Default dimension difficulty variation for mod dimensions")
						.defineInRange("defaultLevelVar", 16d, 0, 1000);
				defaultLevelScale = builder.text("Default dimension difficulty scale for mod dimensions")
						.defineInRange("defaultLevelScale", 1.5, 0, 10);
				initialTraitChanceSlope = builder.text("Mobs at Lv.N will have N x k% chance to have trait")
						.comment("Default k% = 0.01, so Lv.N mobs with have N% chance to have trait")
						.comment("Mobs with entity config and trait chance of 1 will not be affected")
						.defineInRange("initialTraitChanceSlope", 0.01, 0, 1);
				splitDropRateFactor = builder.text("Slimes hostility loot drop rate decay per split")
						.defineInRange("splitDropRateFactor", 0.25d, 0, 1);
				allowNoAI = builder.text("Allow mobs without AI to have levels")
						.define("allowNoAI", false);
				allowPlayerAllies = builder.text("Allow mobs allied to player to have levels")
						.define("allowPlayerAllies", false);
				allowTraitOnOwnable = builder.text("Keep traits on mobs tamed by player")
						.define("allowTraitOnOwnable", false);
				dropRateFromSpawner = builder.text("Drop rate of hostility loot from mobs from spawner")
						.defineInRange("dropRateFromSpawner", 0.5d, 0, 1);
				equipmentDropRate = builder.text("Drop rate of equipments spawned via hostility")
						.defineInRange("equipmentDropRate", 0.085, 0, 1);

			}
			builder.pop();

			builder.push("difficulty", "Difficulty Settings");
			{
				maxPlayerLevel = builder.text("Max player adaptive level")
						.defineInRange("maxPlayerLevel", 2000, 100, 100000);
				maxMobLevel = builder.text("Max mob level")
						.defineInRange("maxMobLevel", 3000, 100, 100000);
				killsPerLevel = builder.text("Difficulty increment takes this many kills of same level mob")
						.defineInRange("killsPerLevel", 30, 1, 100000);
				playerDeathDecay = builder.text("Decay in player difficulty on death")
						.defineInRange("playerDeathDecay", 0.8, 0, 2);
				keepInventoryRuleKeepDifficulty = builder.text("Allow KeepInventory to keep difficulty as well")
						.define("keepInventoryRuleKeepDifficulty", false);
				deathDecayDimension = builder.text("On player death, clear dimension penalty")
						.define("deathDecayDimension", true);
				deathDecayTraitCap = builder.text("On player death, reduce max trait spawned by 1")
						.define("deathDecayTraitCap", true);
				newPlayerProtectRange = builder.text("Mobs spawned within this range will use lowest player level in range instead of nearest player's level to determine mob level")
						.defineInRange("newPlayerProtectRange", 48, 0, 128);
			}
			builder.pop();

			builder.push("orb_and_spawner", "Hostility Orb and Hostility Spawner");
			{
				allowHostilityOrb = builder.text("Allow to use hostility orb")
						.define("allowHostilityOrb", true);
				enableHostilityOrbDrop = builder.text("Give player hostility orbs when upleveling difficulty")
						.define("enableHostilityOrbDrop", true);
				orbRadius = builder.text("Radius for Hostility Orb to take effect.")
						.comment("0 means 1x1x1 section, 1 means 3x3x3 sections, 2 means 5x5x5 sections")
						.defineInRange("orbRadius", 2, 0, 10);
				allowHostilitySpawner = builder.text("Allow to use hostility spawner")
						.define("allowHostilitySpawner", true);
				hostilitySpawnCount = builder.text("Number of mobs to spawn in Hostility Spawner")
						.defineInRange("hostilitySpawnCount", 16, 1, 64);
				hostilitySpawnLevelFactor = builder.text("Level bonus factor for mobs to spawn in Hostility Spawner")
						.defineInRange("hostilitySpawnLevelFactor", 2, 1, 10000);
			}
			builder.pop();

			builder.push("items", "Items");
			{
				banBottles = builder.text("Ban drinking bottle of curse and sanity")
						.define("banBottles", false);
				disableHostilityLootCurioRequirement = builder.text("Disable curio requirement for hostility loot")
						.define("disableHostilityLootCurioRequirement", false);
				bottleOfCurseLevel = builder.text("Number of level to add when using bottle of curse")
						.defineInRange("bottleOfCurseLevel", 50, 0, 1000);

				witchChargeMinDuration = builder.text("Minimum duration for witch charge to be effective, in ticks")
						.defineInRange("witchChargeMinDuration", 200, 20, 10000);

				ringOfLifeMaxDamage = builder.text("Max percentage of max health a damage can hurt wearer of Ring of Life")
						.defineInRange("ringOfLifeMaxDamage", 0.9, 0, 1);

				flameThornTime = builder.text("Time in ticks of Soul Flame to inflict")
						.defineInRange("flameThornTime", 100, 1, 10000);

				ringOfReflectionRadius = builder.text("Radius in blocks for Ring of Reflection to work")
						.defineInRange("ringOfReflectionRadius", 16, 1, 256);

				witchWandFactor = builder.text("Factor of effect duration for witch wand, to make up for splash decay")
						.defineInRange("witchWandFactor", 4, 1, 100);

				ringOfCorrosionFactor = builder.text("Factor of maximum durability to cost for ring of corrosion")
						.defineInRange("ringOfCorrosionFactor", 0.2, 0, 1);

				ringOfCorrosionPenalty = builder.text("Penalty of maximum durability to cost for ring of corrosion")
						.defineInRange("ringOfCorrosionPenalty", 0.1, 0, 1);

				ringOfHealingRate = builder.text("Percentage of health to heal every second")
						.defineInRange("ringOfHealingRate", 0.05, 0, 1);

				// curse
				{
					envyExtraLevel = builder.text("Number of level to add when using Curse of Envy")
							.defineInRange("envyExtraLevel", 50, 0, 1000);
					greedExtraLevel = builder.text("Number of level to add when using Curse of Greed")
							.defineInRange("greedExtraLevel", 50, 0, 1000);
					lustExtraLevel = builder.text("Number of level to add when using Curse of Lust")
							.defineInRange("lustExtraLevel", 50, 0, 1000);
					wrathExtraLevel = builder.text("Number of level to add when using Curse of Wrath")
							.defineInRange("wrathExtraLevel", 50, 0, 1000);

					greedDropFactor = builder.text("Hostility loot drop factor when using Curse of Greed")
							.defineInRange("greedDropFactor", 2d, 1, 10);
					envyDropRate = builder.text("Trait item drop rate per rank when using Curse of Envy")
							.defineInRange("envyDropRate", 0.02, 0, 1);
					gluttonyBottleDropRate = builder.text("Bottle of Curse drop rate per level when using Curse of Gluttony")
							.defineInRange("gluttonyBottleDropRate", 0.02, 0, 1);
					wrathDamageBonus = builder.text("Damage bonus per level difference when using Curse of Wrath")
							.defineInRange("wrathDamageBonus", 0.05, 0, 1);
					prideDamageBonus = builder.text("Damage bonus per level when using Curse of Pride")
							.defineInRange("prideDamageBonus", 0.02, 0, 1);
					prideHealthBonus = builder.text("Health boost per level in percentage when using Curse of Pride")
							.defineInRange("prideHealthBonus", 0.02, 0, 1);
					prideTraitFactor = builder.text("Trait cost multiplier when using Curse of Pride")
							.defineInRange("prideTraitFactor", 0.5, 0.01, 1);
				}


				abrahadabraExtraLevel = builder.text("Number of level to add when using Abrahadabra")
						.defineInRange("abrahadabraExtraLevel", 100, 0, 1000);
				nidhoggurExtraLevel = builder.text("Number of level to add when using Greed of Nidhoggur")
						.defineInRange("nidhoggurExtraLevel", 100, 0, 1000);
				nidhoggurDropFactor = builder.text("All loot drop factor when using Greed of Nidhoggur")
						.defineInRange("nidhoggurDropFactor", 0.01, 0, 10);
				nidhoggurCapAtItemMaxStack = builder.text("Cap drop at item max stack size")
						.define("nidhoggurCapAtItemMaxStack", false);
				bookOfReprintSpread = builder.text("When using book of reprint to copy books, drop extra on player and does not allow overstacking")
						.define("bookOfReprintSpread", false);

				insulatorFactor = builder.text("Insulator Enchantment factor for reducing pushing")
						.defineInRange("insulatorFactor", 0.8, 0, 1);
			}
			builder.pop();

			builder.push("performance", "Performance");
			{
				enableCurioCheckFilter = builder.text("Enable curios checks whitelist for items such as ring of ocean, on only selected mobs to reduce lag")
						.define("enableCurioCheckFilter", true);
				removeTraitCheckInterval = builder.text("Interval for which traits check if they are banned and to be removed")
						.defineInRange("removeTraitCheckInterval", 10, 1, 1000);
				auraEffectApplicationInterval = builder.text("Interval for aura effect traits to apply")
						.defineInRange("auraEffectApplicationInterval", 5, 1, 1000);
				selfEffectApplicationInterval = builder.text("Interval for self effect traits to apply")
						.defineInRange("selfEffectApplicationInterval", 200, 1, 1000);
			}
			builder.pop();

			LHTraits.register();

			builder.push("traits", "Trait Settings");
			{
				tankHealth = builder.text("Health bonus for Tank trait per level")
						.defineInRange("tankHealth", 0.5, 0, 1000);
				tankArmor = builder.text("Armor bonus for Tank trait per level")
						.defineInRange("tankArmor", 10d, 0, 1000);
				tankTough = builder.text("Toughness bonus for Tank trait per level")
						.defineInRange("tankTough", 4d, 0, 1000);
				speedy = builder.text("Speed bonus for Speedy trait per level")
						.defineInRange("speedy", 0.2, 0, 1000);
				regen = builder.text("Regen rate for Regeneration trait per second per level")
						.defineInRange("regen", 0.02, 0, 1000);
				adaptFactor = builder.text("Damage factor for Adaptive. Higher means less reduction")
						.defineInRange("adaptFactor", 0.5, 0, 1000);
				reflectFactor = builder.text("Reflect factor per level for Reflect. 0.5 means +50% extra damage")
						.defineInRange("reflectFactor", 0.3, 0, 1000);
				dispellTime = builder.text("Duration in ticks for enchantments to be disabled per level for Dispell")
						.defineInRange("dispellTime", 200, 1, 60000);
				fieryTime = builder.text("Duration in seconds to set target on fire by Fiery")
						.defineInRange("fieryTime", 5, 0, 3000);
				weakTime = builder.text("Duration in ticks for Weakness")
						.defineInRange("weakTime", 200, 0, 3000);
				slowTime = builder.text("Duration in ticks for Slowness")
						.defineInRange("slowTime", 200, 0, 3000);
				poisonTime = builder.text("Duration in ticks for Poison")
						.defineInRange("poisonTime", 200, 0, 3000);
				witherTime = builder.text("Duration in ticks for Wither")
						.defineInRange("witherTime", 200, 0, 3000);
				levitationTime = builder.text("Duration in ticks for Levitation")
						.defineInRange("levitationTime", 200, 0, 3000);
				blindTime = builder.text("Duration in ticks for Blindness")
						.defineInRange("blindTime", 200, 0, 3000);
				confusionTime = builder.text("Duration in ticks for Nausea")
						.defineInRange("confusionTime", 200, 0, 3000);
				soulBurnerTime = builder.text("Duration in ticks for Soul Burner")
						.defineInRange("soulBurnerTime", 60, 0, 3000);
				freezingTime = builder.text("Duration in ticks for Freezing")
						.defineInRange("freezingTime", 200, 0, 3000);
				curseTime = builder.text("Duration in ticks for Cursed")
						.defineInRange("curseTime", 200, 0, 3000);
				teleportDuration = builder.text("Interval in ticks for Teleport")
						.defineInRange("teleportDuration", 100, 0, 3000);
				teleportRange = builder.text("Range in blocks for Teleport")
						.defineInRange("teleportRange", 16, 0, 64);
				repellRange = builder.text("Range in blocks for Repell")
						.defineInRange("repellRange", 10, 0, 64);
				repellStrength = builder.text("Repell force strength, default is 0.2")
						.defineInRange("repellStrength", 0.2, 0, 1);

				corrosionDurability = builder.text("Fraction of remaining durability to corrode, per trait rank")
						.defineInRange("corrosionDurability", 0.3, 0, 1);
				corrosionDamage = builder.text("Damage bonus when nothing to corrode")
						.defineInRange("corrosionDamage", 0.25, 0, 1);
				erosionDurability = builder.text("Fraction of lost durability to erode, per trait rank")
						.defineInRange("erosionDurability", 0.1, 0, 1);
				erosionDamage = builder.text("Damage bonus when nothing to erode")
						.defineInRange("erosionDamage", 0.25, 0, 1);
				ragnarokTime = builder.text("Seal time per level for Ragnarok")
						.defineInRange("ragnarokTime", 20, 1, 1000);
				ragnarokSealBackpack = builder.text("Allow Ragnarok to seal items with Backpack in its id")
						.define("ragnarokSealBackpack", false);
				ragnarokSealSlotAdder = builder.text("Allow Ragnarok to seal curios items that adds curios slot")
						.define("ragnarokSealSlotAdder", false);
				killerAuraDamage = builder.text("Damage for killer aura")
						.defineInRange("killerAuraDamage", 10, 1, 10000);
				killerAuraRange = builder.text("Range for for killer aura")
						.defineInRange("killerAuraRange", 6, 1, 32);
				killerAuraInterval = builder.text("Interval for for killer aura")
						.defineInRange("killerAuraInterval", 120, 1, 10000);

				shulkerInterval = builder.text("Interval for for shulker")
						.defineInRange("shulkerInterval", 40, 1, 10000);
				grenadeInterval = builder.text("Interval for for explode shulker")
						.defineInRange("explodeShulkerInterval", 60, 1, 10000);
				drainDamage = builder.text("Damage bonus for each negative effects")
						.defineInRange("drainDamage", 0.1, 0, 100);
				drainDuration = builder.text("Duration boost for negative effects")
						.defineInRange("drainDuration", 0.50, 0, 100);
				drainDurationMax = builder.text("Max duration boost for negative effects")
						.defineInRange("drainDurationMax", 1200, 0, 10000);

				counterStrikeDuration = builder.text("Interval in ticks for Counter Strike")
						.defineInRange("counterStrikeDuration", 100, 0, 3000);
				counterStrikeRange = builder.text("Range in blocks for Counter Strike")
						.defineInRange("counterStrikeRange", 6, 0, 64);

				pullingRange = builder.text("Range in blocks for Pulling")
						.defineInRange("pullingRange", 10, 0, 64);
				pullingStrength = builder.text("Pulling force strength, default is 0.2")
						.defineInRange("pullingStrength", 0.2, 0, 10);
				reprintDamage = builder.text("Reprint damage factor per enchantment point")
						.defineInRange("reprintDamage", 0.02, 0, 1);
				reprintBypass = builder.text("Reprint will gain Void Touch 20 and Vanishing Curse when it hits a mob with max Enchantment level of X or higher")
						.defineInRange("reprintBypass", 10, 0, 10000);

				effectAura(builder, "gravity", 10);
				effectAura(builder, "moonwalk", 10);
				effectAura(builder, "arena", 24);


			}
			builder.pop();

			builder.push("toggle", " Trait Toggles");
			for (var e : L2Hostility.REGISTRATE.getList()) {
				map.put(e, builder.define("allow_" + e, true));
			}
			builder.pop();
		}

		private void effectAura(Builder builder, String str, int def) {
			range.put(str, builder.text("Effect range for trait " + str)
					.defineInRange(str + "Range", def, 0, 100));
		}

	}

	public static final Client CLIENT = L2Hostility.REGISTRATE.registerClient(Client::new);
	public static final Server SERVER = L2Hostility.REGISTRATE.registerSynced(Server::new);

	public static void init() {
	}

}
