package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHConfig.Common;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Read/write access to the Forge config for the editor. Lives in the config package rather than
 * base on purpose (the base layer is a mod-independent copy; this is l2hostility-specific).
 */
public final class LHConfigEdit {

	private LHConfigEdit() {
	}

	public enum Kind {BOOL, INT, DOUBLE}

	/**
	 * One config value together with the widgets to edit it.
	 */
	public record FieldDef(Component label, Kind kind, ForgeConfigSpec.ConfigValue<?> value,
	                       @Nullable List<Component> fixed) {

		public FieldDef(Component label, Kind kind, ForgeConfigSpec.ConfigValue<?> value) {
			this(label, kind, value, null);
		}

		public String getString() {
			return switch (kind) {
				case BOOL -> String.valueOf(((ForgeConfigSpec.BooleanValue) value).get());
				case INT -> String.valueOf(((ForgeConfigSpec.IntValue) value).get());
				case DOUBLE -> String.valueOf(((ForgeConfigSpec.DoubleValue) value).get());
			};
		}

		public void set(String s) {
			switch (kind) {
				case BOOL -> ((ForgeConfigSpec.BooleanValue) value).set(Boolean.parseBoolean(s));
				case INT -> ((ForgeConfigSpec.IntValue) value).set(Integer.parseInt(s));
				case DOUBLE -> ((ForgeConfigSpec.DoubleValue) value).set(Double.parseDouble(s));
			}
		}

		/**
		 * Tooltip lines for this value: an explicit text when given, otherwise the comment that
		 * was declared with the Forge config value.
		 */
		@Nullable
		public List<Component> tooltip() {
			if (fixed != null) return fixed;
			Object vs = LHConfig.COMMON_SPEC.getSpec().get(value.getPath());
			if (vs instanceof ForgeConfigSpec.ValueSpec vs2) {
				String comment = vs2.getComment();
				if (comment == null || comment.isBlank()) return null;
				List<Component> ans = new ArrayList<>();
				for (String line : comment.split("\n")) {
					String trimmed = line.trim();
					if (!trimmed.isEmpty()) ans.add(Component.literal(trimmed));
				}
				return ans;
			}
			return null;
		}

		public FormScreen.FormField toFormField() {
			Component[] tip = tooltip() == null ? new Component[0] : tooltip().toArray(new Component[0]);
			return switch (kind) {
				case BOOL -> FormScreen.FormField.bool(label, (Boolean) value.get(), tip);
				case INT -> FormScreen.FormField.text(label, String.valueOf(((ForgeConfigSpec.IntValue) value).get()),
						HostilityEditorForms::intValidate, tip);
				case DOUBLE ->
						FormScreen.FormField.text(label, String.valueOf(((ForgeConfigSpec.DoubleValue) value).get()),
								HostilityEditorForms::doubleValidate, tip);
			};
		}
	}

	/**
	 * A named group of config fields (a config section).
	 */
	public record Section(Component title, List<FieldDef> fields) {

	}

	private static final ResourceLocation CONFIG_ID = new ResourceLocation("l2hostility", "config");

	/**
	 * Writes the common config to disk. The values are already applied in memory via {@code set}.
	 */
	public static void saveConfig() {
		for (ModConfig c : ConfigTracker.INSTANCE.configSets().getOrDefault(ModConfig.Type.COMMON, Set.of())) {
			if (c.getSpec() == LHConfig.COMMON_SPEC) {
				c.save();
				return;
			}
		}
	}

	/**
	 * Opens a form over the given fields; on confirm the values are applied and saved, and the
	 * parent screen is reopened (re-reading the config).
	 */
	public static void openSectionForm(Component title, List<FieldDef> fields, Screen parent) {
		List<FormScreen.FormField> form = new ArrayList<>();
		for (FieldDef f : fields) form.add(f.toFormField());
		Minecraft.getInstance().setScreen(new FormScreen<>(title, new FormScreen.FormSpec<>(form, values -> {
			for (int i = 0; i < fields.size(); i++) fields.get(i).set(values.get(i));
			saveConfig();
			EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_DONE.get(CONFIG_ID));
			return null;
		}), t -> Minecraft.getInstance().setScreen(parent), parent, true));
	}

	/**
	 * The allow toggle for a trait, or null if the trait has no toggle.
	 */
	@Nullable
	public static FieldDef traitToggle(String traitPath) {
		ForgeConfigSpec.BooleanValue toggle = LHConfig.COMMON.map.get(traitPath);
		if (toggle == null) return null;
		return new FieldDef(Component.literal("allow_" + traitPath), Kind.BOOL, toggle,
				List.of(HostilityEditorLang.TRAIT_TOGGLE_TIP.get()));
	}

	/**
	 * Config values a trait reads from the Forge config, excluding its toggle.
	 */
	public static List<FieldDef> traitConfigFields(String traitPath) {
		List<FieldDef> ans = new ArrayList<>();
		Common c = LHConfig.COMMON;
		switch (traitPath) {
			case "tank" -> ans.addAll(List.of(
					d(c.tankHealth, "tankHealth"), d(c.tankArmor, "tankArmor"), d(c.tankTough, "tankTough")));
			case "speedy" -> ans.add(d(c.speedy, "speedy"));
			case "regenerate" -> ans.add(d(c.regen, "regen"));
			case "adaptive" -> ans.add(d(c.adaptFactor, "adaptFactor"));
			case "reflect" -> ans.add(d(c.reflectFactor, "reflectFactor"));
			case "dispell" -> ans.addAll(List.of(
					i(c.dispellTime, "dispellTime"), d(c.dispellDamageFactor, "dispellDamageFactor"),
					d(c.dispellDamageReductionBase, "dispellDamageReductionBase")));
			case "fiery" -> ans.add(i(c.fieryTime, "fieryTime"));
			case "weakness" -> ans.add(i(c.weakTime, "weakTime"));
			case "slowness" -> ans.add(i(c.slowTime, "slowTime"));
			case "poison" -> ans.add(i(c.poisonTime, "poisonTime"));
			case "wither" -> ans.add(i(c.witherTime, "witherTime"));
			case "levitation" -> ans.add(i(c.levitationTime, "levitationTime"));
			case "blindness" -> ans.add(i(c.blindTime, "blindTime"));
			case "nausea" -> ans.add(i(c.confusionTime, "confusionTime"));
			case "soul_burner" -> ans.add(i(c.soulBurnerTime, "soulBurnerTime"));
			case "freezing" -> ans.add(i(c.freezingTime, "freezingTime"));
			case "cursed" -> ans.add(i(c.curseTime, "curseTime"));
			case "teleport" ->
					ans.addAll(List.of(i(c.teleportDuration, "teleportDuration"), i(c.teleportRange, "teleportRange")));
			case "repelling" ->
					ans.addAll(List.of(i(c.repellRange, "repellRange"), d(c.repellStrength, "repellStrength")));
			case "pulling" ->
					ans.addAll(List.of(i(c.pullingRange, "pullingRange"), d(c.pullingStrength, "pullingStrength")));
			case "corrosion" -> ans.addAll(List.of(
					d(c.corrosionDurability, "corrosionDurability"), d(c.corrosionDamage, "corrosionDamage")));
			case "erosion" -> ans.addAll(List.of(
					d(c.erosionDurability, "erosionDurability"), d(c.erosionDamage, "erosionDamage")));
			case "ragnarok" -> ans.addAll(List.of(
					i(c.ragnarokTime, "ragnarokTime"), b(c.ragnarokSealBackpack, "ragnarokSealBackpack"),
					b(c.ragnarokSealSlotAdder, "ragnarokSealSlotAdder")));
			case "killer_aura" -> ans.addAll(List.of(
					i(c.killerAuraDamage, "killerAuraDamage"), i(c.killerAuraRange, "killerAuraRange"),
					i(c.killerAuraInterval, "killerAuraInterval"), d(c.killerAuraDamageFactor, "killerAuraDamageFactor")));
			case "shulker" -> ans.add(i(c.shulkerInterval, "shulkerInterval"));
			case "grenade" -> ans.addAll(List.of(
					i(c.grenadeInterval, "grenadeInterval"), d(c.grenadeDamageFactor, "grenadeDamageFactor")));
			case "drain" -> ans.addAll(List.of(
					d(c.drainDamage, "drainDamage"), d(c.drainDuration, "drainDuration"), i(c.drainDurationMax, "drainDurationMax")));
			case "counter_strike" -> ans.addAll(List.of(
					i(c.counterStrikeDuration, "counterStrikeDuration"), i(c.counterStrikeRange, "counterStrikeRange")));
			case "reprint" ->
					ans.addAll(List.of(d(c.reprintDamage, "reprintDamage"), i(c.reprintBypass, "reprintBypass")));
			case "dementor" -> ans.addAll(List.of(
					d(c.dementorDamageFactor, "dementorDamageFactor"), d(c.dementorDamageReductionBase, "dementorDamageReductionBase")));
			case "gravity", "moonwalk", "arena" -> {
				ForgeConfigSpec.IntValue range = c.range.get(traitPath);
				if (range != null) ans.add(i(range, traitPath + "Range"));
			}
		}
		return ans;
	}

	/**
	 * Config fields for a single trait (its toggle + the config values it reads). Empty for traits
	 * that have no dedicated config.
	 */
	public static List<FieldDef> traitFields(String traitPath) {
		List<FieldDef> ans = new ArrayList<>();
		FieldDef toggle = traitToggle(traitPath);
		if (toggle != null) ans.add(toggle);
		ans.addAll(traitConfigFields(traitPath));
		return ans;
	}

	private static FieldDef b(ForgeConfigSpec.BooleanValue value, String name) {
		return new FieldDef(Component.literal(name), Kind.BOOL, value);
	}

	private static FieldDef i(ForgeConfigSpec.IntValue value, String name) {
		return new FieldDef(Component.literal(name), Kind.INT, value);
	}

	private static FieldDef d(ForgeConfigSpec.DoubleValue value, String name) {
		return new FieldDef(Component.literal(name), Kind.DOUBLE, value);
	}

	/**
	 * Config sections for the config tab, excluding trait-related configs (edited in the trait
	 * editor instead).
	 */
	public static List<Section> generalSections() {
		Common c = LHConfig.COMMON;
		return List.of(
				new Section(Component.literal("Datapack"), List.of(
						b(c.enableEntitySpecificDatapack, "enableEntitySpecificDatapack"),
						b(c.enableStructureSpecificDatapack, "enableStructureSpecificDatapack"),
						b(c.enableEquipmentDatapack, "enableEquipmentDatapack"))),
				new Section(Component.literal("Scaling"), List.of(
						d(c.healthFactor, "healthFactor"), b(c.exponentialHealth, "exponentialHealth"),
						d(c.damageFactor, "damageFactor"), b(c.exponentialDamage, "exponentialDamage"),
						d(c.expDropFactor, "expDropFactor"), d(c.drownedTridentChancePerLevel, "drownedTridentChancePerLevel"),
						d(c.enchantmentFactor, "enchantmentFactor"), i(c.dimensionFactor, "dimensionFactor"),
						d(c.distanceFactor, "distanceFactor"), d(c.globalApplyChance, "globalApplyChance"),
						d(c.globalTraitChance, "globalTraitChance"), d(c.globalTraitSuppression, "globalTraitSuppression"),
						b(c.allowLegendary, "allowLegendary"), b(c.allowSectionDifficulty, "allowSectionDifficulty"),
						b(c.allowBypassMinimum, "allowBypassMinimum"), b(c.allowExtraEnchantments, "allowExtraEnchantments"),
						i(c.defaultLevelBase, "defaultLevelBase"), d(c.defaultLevelVar, "defaultLevelVar"),
						d(c.defaultLevelScale, "defaultLevelScale"), d(c.initialTraitChanceSlope, "initialTraitChanceSlope"),
						d(c.splitDropRateFactor, "splitDropRateFactor"), b(c.allowNoAI, "allowNoAI"),
						b(c.allowPlayerAllies, "allowPlayerAllies"), b(c.allowTraitOnOwnable, "allowTraitOnOwnable"),
						d(c.dropRateFromSpawner, "dropRateFromSpawner"), d(c.equipmentDropRate, "equipmentDropRate"),
						i(c.maxTraitCount, "maxTraitCount"), b(c.enableAdaptiveLeveling, "enableAdaptiveLeveling"))),
				new Section(Component.literal("Difficulty"), List.of(
						i(c.maxPlayerLevel, "maxPlayerLevel"), i(c.maxMobLevel, "maxMobLevel"),
						i(c.killsPerLevel, "killsPerLevel"), d(c.playerDeathDecay, "playerDeathDecay"),
						b(c.keepInventoryRuleKeepDifficulty, "keepInventoryRuleKeepDifficulty"),
						b(c.deathDecayDimension, "deathDecayDimension"), b(c.deathDecayTraitCap, "deathDecayTraitCap"),
						i(c.newPlayerProtectRange, "newPlayerProtectRange"))),
				new Section(Component.literal("Orb & Spawner"), List.of(
						b(c.allowHostilityOrb, "allowHostilityOrb"), b(c.enableHostilityOrbDrop, "enableHostilityOrbDrop"),
						i(c.orbRadius, "orbRadius"), b(c.allowHostilitySpawner, "allowHostilitySpawner"),
						i(c.hostilitySpawnCount, "hostilitySpawnCount"), i(c.hostilitySpawnLevelFactor, "hostilitySpawnLevelFactor"))),
				new Section(Component.literal("Items"), List.of(
						b(c.banBottles, "banBottles"),
						b(c.disableHostilityLootCurioRequirement, "disableHostilityLootCurioRequirement"),
						i(c.bottleOfCurseLevel, "bottleOfCurseLevel"), i(c.witchChargeMinDuration, "witchChargeMinDuration"),
						d(c.ringOfLifeMaxDamage, "ringOfLifeMaxDamage"), i(c.flameThornTime, "flameThornTime"),
						i(c.ringOfReflectionRadius, "ringOfReflectionRadius"), i(c.witchWandFactor, "witchWandFactor"),
						d(c.ringOfCorrosionFactor, "ringOfCorrosionFactor"), d(c.ringOfCorrosionPenalty, "ringOfCorrosionPenalty"),
						d(c.ringOfHealingRate, "ringOfHealingRate"), i(c.envyExtraLevel, "envyExtraLevel"),
						i(c.greedExtraLevel, "greedExtraLevel"), i(c.lustExtraLevel, "lustExtraLevel"),
						i(c.wrathExtraLevel, "wrathExtraLevel"), d(c.greedDropFactor, "greedDropFactor"),
						d(c.envyDropRate, "envyDropRate"), d(c.gluttonyBottleDropRate, "gluttonyBottleDropRate"),
						d(c.wrathDamageBonus, "wrathDamageBonus"), d(c.prideDamageBonus, "prideDamageBonus"),
						d(c.prideHealthBonus, "prideHealthBonus"), d(c.prideTraitFactor, "prideTraitFactor"),
						i(c.abrahadabraExtraLevel, "abrahadabraExtraLevel"), i(c.nidhoggurExtraLevel, "nidhoggurExtraLevel"),
						d(c.nidhoggurDropFactor, "nidhoggurDropFactor"), b(c.nidhoggurCapAtItemMaxStack, "nidhoggurCapAtItemMaxStack"),
						b(c.bookOfReprintSpread, "bookOfReprintSpread"), d(c.insulatorFactor, "insulatorFactor"))),
				new Section(Component.literal("Performance"), List.of(
						b(c.enableCurioCheckFilter, "enableCurioCheckFilter"), i(c.removeTraitCheckInterval, "removeTraitCheckInterval"),
						i(c.auraEffectApplicationInterval, "auraEffectApplicationInterval"), i(c.selfEffectApplicationInterval, "selfEffectApplicationInterval"))));
	}

}
