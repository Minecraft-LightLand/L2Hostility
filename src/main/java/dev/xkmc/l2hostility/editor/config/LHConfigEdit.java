package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.editor.base.ConfigEdit;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHConfig.Client;
import dev.xkmc.l2hostility.init.data.LHConfig.Server;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * L2Hostility-specific config access for the editor: supplies the config specs, lang keys and
 * sections of this mod. Generic logic lives in {@link ConfigEdit}.
 */
public final class LHConfigEdit extends ConfigEdit {

	/**
	 * The single config editor instance used by this mod.
	 */
	public static final LHConfigEdit INSTANCE = new LHConfigEdit();

	private static final String LANG_PREFIX = L2Hostility.MODID + ".configuration.";

	private static final ResourceLocation CONFIG_ID = ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "config");

	private LHConfigEdit() {
	}

	@Override
	protected ModConfigSpec commonSpec() {
		return (ModConfigSpec) LHConfig.SERVER.getSpec();
	}

	@Override
	protected ModConfigSpec clientSpec() {
		return (ModConfigSpec) LHConfig.CLIENT.getSpec();
	}

	@Override
	protected String configLangPrefix() {
		return LANG_PREFIX;
	}

	@Override
	protected ResourceLocation configId() {
		return CONFIG_ID;
	}

	@Override
	@Nullable
	protected Component validateInt(String s) {
		return HostilityEditorForms.intValidate(s);
	}

	@Override
	@Nullable
	protected Component validateDouble(String s) {
		return HostilityEditorForms.doubleValidate(s);
	}

	@Override
	protected List<Section> homeSections() {
		List<Section> ans = new ArrayList<>(clientSections());
		ans.addAll(generalSections());
		return ans;
	}

	/**
	 * The allow toggle for a trait, or null if the trait has no toggle.
	 */
	@Nullable
	public static FieldDef traitToggle(String traitPath) {
		ModConfigSpec.BooleanValue toggle = LHConfig.SERVER.map.get(traitPath);
		if (toggle == null) return null;
		return new FieldDef(allowLabel(traitPath), Kind.BOOL, toggle,
				List.of(HostilityEditorLang.TRAIT_TOGGLE_TIP.get()));
	}

	/**
	 * Display name of the allow toggle for a trait: a translatable "Allow: " prefix followed by the
	 * translated trait name.
	 */
	private static Component allowLabel(String traitPath) {
		MobTrait trait = LHTraits.TRAITS.get().get(ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, traitPath));
		Component name = trait == null ? Component.literal(traitPath) : HostilityEditorUtil.traitName(trait);
		return HostilityEditorLang.ALLOW.get().append(name);
	}

	/**
	 * Config values a trait reads from the Forge config, excluding its toggle.
	 */
	public static List<FieldDef> traitConfigFields(String traitPath) {
		List<FieldDef> ans = new ArrayList<>();
		Server c = LHConfig.SERVER;
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
				ModConfigSpec.IntValue range = c.range.get(traitPath);
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

	private static FieldDef b(ModConfigSpec.BooleanValue value, String name) {
		return new FieldDef(optionName(name), Kind.BOOL, value);
	}

	private static FieldDef i(ModConfigSpec.IntValue value, String name) {
		return new FieldDef(optionName(name), Kind.INT, value);
	}

	private static FieldDef d(ModConfigSpec.DoubleValue value, String name) {
		return new FieldDef(optionName(name), Kind.DOUBLE, value);
	}

	private static FieldDef s(ModConfigSpec.ConfigValue<String> value, String name) {
		return new FieldDef(optionName(name), Kind.STRING, value);
	}

	/**
	 * Display name of a config option: its translation when one is registered, otherwise the raw
	 * option name.
	 */
	private static Component optionName(String name) {
		if (I18n.exists(LANG_PREFIX + name))
			return Component.translatable(LANG_PREFIX + name);
		return Component.literal(name);
	}

	/**
	 * Display name of a config section: its translation when one is registered, otherwise the
	 * given English fallback.
	 */
	private static Component sectionName(String key, String fallback) {
		if (I18n.exists(LANG_PREFIX + key))
			return Component.translatable(LANG_PREFIX + key);
		return Component.literal(fallback);
	}

	/**
	 * Config sections for the config tab, excluding trait-related configs (edited in the trait
	 * editor instead).
	 */
	public static List<Section> generalSections() {
		Server c = LHConfig.SERVER;
		return List.of(
				new Section(sectionName("datapack", "Datapack"), List.of(
						b(c.enableEntitySpecificDatapack, "enableEntitySpecificDatapack"),
						b(c.enableStructureSpecificDatapack, "enableStructureSpecificDatapack"),
						b(c.enableEquipmentDatapack, "enableEquipmentDatapack"))),
				new Section(sectionName("scaling", "Scaling"), List.of(
						d(c.healthFactor, "healthFactor"), b(c.exponentialHealth, "exponentialHealth"),
						d(c.damageFactor, "damageFactor"), b(c.exponentialDamage, "exponentialDamage"),
						d(c.expDropFactor, "expDropFactor"), d(c.drownedTridentChancePerLevel, "drownedTridentChancePerLevel"),
						i(c.dimensionFactor, "dimensionFactor"),
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
				new Section(sectionName("difficulty", "Difficulty"), List.of(
						i(c.maxPlayerLevel, "maxPlayerLevel"), i(c.maxMobLevel, "maxMobLevel"),
						i(c.killsPerLevel, "killsPerLevel"), d(c.playerDeathDecay, "playerDeathDecay"),
						b(c.keepInventoryRuleKeepDifficulty, "keepInventoryRuleKeepDifficulty"),
						b(c.deathDecayDimension, "deathDecayDimension"), b(c.deathDecayTraitCap, "deathDecayTraitCap"),
						i(c.newPlayerProtectRange, "newPlayerProtectRange"))),
				new Section(sectionName("orb_and_spawner", "Orb & Spawner"), List.of(
						b(c.allowHostilityOrb, "allowHostilityOrb"), b(c.enableHostilityOrbDrop, "enableHostilityOrbDrop"),
						i(c.orbRadius, "orbRadius"), b(c.allowHostilitySpawner, "allowHostilitySpawner"),
						i(c.hostilitySpawnCount, "hostilitySpawnCount"), i(c.hostilitySpawnLevelFactor, "hostilitySpawnLevelFactor"))),
				new Section(sectionName("items", "Items"), List.of(
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
				new Section(sectionName("performance", "Performance"), List.of(
						b(c.enableCurioCheckFilter, "enableCurioCheckFilter"), i(c.removeTraitCheckInterval, "removeTraitCheckInterval"),
						i(c.auraEffectApplicationInterval, "auraEffectApplicationInterval"), i(c.selfEffectApplicationInterval, "selfEffectApplicationInterval"))));
	}

	/**
	 * Config sections of the client config, including the editor save path.
	 */
	public static List<Section> clientSections() {
		Client c = LHConfig.CLIENT;
		return List.of(
				new Section(sectionName("editor", "Editor"), List.of(
						s(c.editorSavePath, "editorSavePath"))),
				new Section(sectionName("overhead", "Overhead display"), List.of(
						b(c.showTraitOverHead, "showTraitOverHead"), b(c.showLevelOverHead, "showLevelOverHead"),
						i(c.overHeadRenderDistance, "overHeadRenderDistance"), d(c.overHeadRenderOffset, "overHeadRenderOffset"),
						b(c.overHeadRenderFullBright, "overHeadRenderFullBright"), i(c.overHeadLevelColor, "overHeadLevelColor"),
						i(c.overHeadLevelColorAbyss, "overHeadLevelColorAbyss"), b(c.showOnlyWhenHovered, "showOnlyWhenHovered"))),
				new Section(sectionName("glasses", "Detector Glasses"), List.of(
						i(c.glowingRangeHidden, "glowingRangeHidden"), i(c.glowingRangeNear, "glowingRangeNear"))),
				new Section(sectionName("misc", "Misc"), List.of(
						b(c.showUndyingParticles, "showUndyingParticles"), b(c.killerAuraSoundEffect, "killerAuraSoundEffect"))));
	}

}
