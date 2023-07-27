package dev.xkmc.l2complements.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class LHConfig {

	public static class Client {

		Client(ForgeConfigSpec.Builder builder) {
		}

	}

	public static class Common {

		public ForgeConfigSpec.IntValue killPerLevel;
		public ForgeConfigSpec.IntValue modifierCapPerLevel;
		public ForgeConfigSpec.DoubleValue deathDecay;

		public ForgeConfigSpec.DoubleValue healthFactor;
		public ForgeConfigSpec.DoubleValue damageFactor;


		Common(ForgeConfigSpec.Builder builder) {
			builder.push("difficulty");
			killPerLevel = builder.comment("Difficulty increment takes this many kills of same level mob")
					.defineInRange("killPerLevel", 10, 1, 10000);
			modifierCapPerLevel = builder.comment("Mob modifier cap per difficulty level. This is not the only factor")
					.defineInRange("modifierCapPerLevel", 20, 1, 10000);
			deathDecay = builder.comment("Decay in player difficulty on death")
					.defineInRange("deathDecay", 0.9, 0, 2);
			builder.pop();

			builder.push("attributes");
			healthFactor = builder.comment("Health factor per level")
					.defineInRange("healthFactor", 0.03, 0, 1000);
			damageFactor = builder.comment("Damage factor per level")
					.defineInRange("damageFactor", 0.02, 0, 1000);
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
