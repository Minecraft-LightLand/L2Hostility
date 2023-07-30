package dev.xkmc.l2hostility.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class LHConfig {

	public static class Client {

		public final ForgeConfigSpec.BooleanValue showAsNamePlate;

		Client(ForgeConfigSpec.Builder builder) {
			showAsNamePlate = builder.comment("Render Traits in name plate form")
					.define("showAsNamePlate", true);
		}

	}

	public static class Common {

		public final ForgeConfigSpec.IntValue killPerLevel;
		public final ForgeConfigSpec.IntValue traitCapPerLevel;
		public final ForgeConfigSpec.DoubleValue deathDecay;

		public final ForgeConfigSpec.DoubleValue healthFactor;
		public final ForgeConfigSpec.DoubleValue damageFactor;
		public final ForgeConfigSpec.IntValue armorFactor;
		public final ForgeConfigSpec.DoubleValue enchantmentFactor;
		public final ForgeConfigSpec.BooleanValue addLevelToName;

		public final ForgeConfigSpec.DoubleValue tankHealth;
		public final ForgeConfigSpec.DoubleValue tankArmor;
		public final ForgeConfigSpec.DoubleValue tankTough;
		public final ForgeConfigSpec.DoubleValue speedy;
		public final ForgeConfigSpec.DoubleValue regen;
		public final ForgeConfigSpec.DoubleValue adaptFactor;
		public final ForgeConfigSpec.DoubleValue reflect;
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

		Common(ForgeConfigSpec.Builder builder) {
			builder.push("difficulty");
			{
				killPerLevel = builder.comment("Difficulty increment takes this many kills of same level mob")
						.defineInRange("killPerLevel", 10, 1, 10000);
				traitCapPerLevel = builder.comment("Mob trait cap per difficulty level. This is not the only factor")
						.defineInRange("traitCapPerLevel", 20, 1, 10000);
				deathDecay = builder.comment("Decay in player difficulty on death")
						.defineInRange("deathDecay", 0.9, 0, 2);
			}
			builder.pop();
			builder.push("misc");
			addLevelToName = builder.comment("Add mob level to name")
					.define("addLevelToName", true);
			builder.pop();

			builder.push("scaling");
			{
				healthFactor = builder.comment("Health factor per level")
						.defineInRange("healthFactor", 0.03, 0, 1000);
				damageFactor = builder.comment("Damage factor per level")
						.defineInRange("damageFactor", 0.02, 0, 1000);
				armorFactor = builder.comment("Armor rank per n level")
						.defineInRange("armorFactor", 10, 0, 1000);
				enchantmentFactor = builder.comment("Enchantment bonus per level.",
								"Note: use it only when Apotheosis is installed",
								"Otherwise too high enchantment level will yield no enchantment")
						.defineInRange("enchantmentFactor", 0d, 0, 1000);
			}
			builder.pop();

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
				reflect = builder.comment("Reflect factor per level for Reflect. 0.5 means +50% extra damage")
						.defineInRange("reflect", 0.5, 0, 1000);
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

			}
			builder.pop();
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
