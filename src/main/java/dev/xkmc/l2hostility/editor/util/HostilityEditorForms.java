package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.editor.base.DoubleMapScreen;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public final class HostilityEditorForms {

	private HostilityEditorForms() {
	}

	@Nullable
	public static Component intValidate(String s) {
		try {
			Integer.parseInt(s.trim());
			return null;
		} catch (NumberFormatException e) {
			return HostilityEditorLang.INVALID_INTEGER.get(s);
		}
	}

	@Nullable
	public static Component doubleValidate(String s) {
		try {
			Double.parseDouble(s.trim());
			return null;
		} catch (NumberFormatException e) {
			return HostilityEditorLang.INVALID_DOUBLE.get(s);
		}
	}

	@Nullable
	public static Component optionalRlValidate(String s) {
		if (s.isBlank()) return null;
		ResourceLocation id = dev.xkmc.l2hostility.editor.base.EditorFile.parseId(s);
		return id == null ? EditorText.INVALID_ID.get(s) : null;
	}

	public static FormScreen.FormSpec<WorldDifficultyConfig.DifficultyConfig> difficultyConfig(WorldDifficultyConfig.DifficultyConfig cur) {
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		Function<String, Component> doubles = HostilityEditorForms::doubleValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + cur.min(), ints),
				FormScreen.FormField.text(HostilityEditorLang.BASE.get(), "" + cur.base(), ints),
				FormScreen.FormField.text(HostilityEditorLang.VARIATION.get(), DoubleMapScreen.format(cur.variation()), doubles),
				FormScreen.FormField.text(HostilityEditorLang.SCALE.get(), DoubleMapScreen.format(cur.scale()), doubles),
				FormScreen.FormField.text(HostilityEditorLang.APPLY_CHANCE.get(), DoubleMapScreen.format(cur.apply_chance()), doubles),
				FormScreen.FormField.text(HostilityEditorLang.TRAIT_CHANCE.get(), DoubleMapScreen.format(cur.trait_chance()), doubles),
				FormScreen.FormField.text(HostilityEditorLang.SUPPRESSION.get(), DoubleMapScreen.format(cur.suppression()), doubles)
		), v -> new WorldDifficultyConfig.DifficultyConfig(
				Integer.parseInt(v.get(0).trim()),
				Integer.parseInt(v.get(1).trim()),
				Double.parseDouble(v.get(2).trim()),
				Double.parseDouble(v.get(3).trim()),
				Double.parseDouble(v.get(4).trim()),
				Double.parseDouble(v.get(5).trim()),
				Double.parseDouble(v.get(6).trim())));
	}

	public static FormScreen.FormSpec<TraitConfig> traitConfig(TraitConfig cur) {
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + cur.min_level, ints),
				FormScreen.FormField.text(HostilityEditorLang.COST.get(), "" + cur.cost, ints),
				FormScreen.FormField.text(HostilityEditorLang.MAX_RANK.get(), "" + cur.max_rank, ints),
				FormScreen.FormField.text(HostilityEditorLang.WEIGHT.get(), "" + cur.weight, ints)
		), v -> {
			TraitConfig ans = new TraitConfig();
			ans.min_level = Integer.parseInt(v.get(0).trim());
			ans.cost = Integer.parseInt(v.get(1).trim());
			ans.max_rank = Integer.parseInt(v.get(2).trim());
			ans.weight = Integer.parseInt(v.get(3).trim());
			return ans;
		});
	}

	public static FormScreen.FormSpec<EntityConfig.TraitBase> traitBase(MobTrait trait, @Nullable EntityConfig.TraitBase cur) {
		EntityConfig.TraitBase c = cur == null ? new EntityConfig.TraitBase(trait, 0, 0, false, null) : cur;
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		Function<String, Component> doubles = HostilityEditorForms::doubleValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.FREE.get(), "" + c.free(), ints),
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + c.min(), ints),
				FormScreen.FormField.bool(HostilityEditorLang.CAP.get(), c.cap()),
				FormScreen.FormField.text(HostilityEditorLang.LV.get(), c.condition() == null ? "" : "" + c.condition().lv(), HostilityEditorForms::optionalInt),
				FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), c.condition() == null ? "" : DoubleMapScreen.format(c.condition().chance()), HostilityEditorForms::optionalDouble),
				FormScreen.FormField.text(HostilityEditorLang.ADVANCEMENT_ID.get(), c.condition() == null || c.condition().id() == null ? "" : c.condition().id().toString(), HostilityEditorForms::optionalRlValidate)
		), v -> {
			boolean cap = v.get(2).equals("true");
			String lv = v.get(3).trim();
			String chance = v.get(4).trim();
			String id = v.get(5).trim();
			if (lv.isEmpty()) {
				return new EntityConfig.TraitBase(c.trait(), Integer.parseInt(v.get(0).trim()),
						Integer.parseInt(v.get(1).trim()), cap, null);
			}
			ResourceLocation rl = id.isEmpty() ? null : new ResourceLocation(id);
			return new EntityConfig.TraitBase(c.trait(), Integer.parseInt(v.get(0).trim()),
					Integer.parseInt(v.get(1).trim()), cap,
					new EntityConfig.TraitCondition(Integer.parseInt(lv), (float) Double.parseDouble(chance), rl));
		});
	}

	public static FormScreen.FormSpec<EntityConfig.ItemPool> itemPool() {
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		Function<String, Component> doubles = HostilityEditorForms::doubleValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "0", ints),
				FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), "1", doubles),
				FormScreen.FormField.text(HostilityEditorLang.SLOT.get(), "mainhand", null)
		), v -> new EntityConfig.ItemPool(Integer.parseInt(v.get(0).trim()),
				(float) Double.parseDouble(v.get(1).trim()), v.get(2).trim(), new java.util.ArrayList<>()));
	}

	public static FormScreen.FormSpec<EntityConfig.MasterConfig> masterConfig(EntityConfig.MasterConfig cur) {
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MAX_TOTAL_COUNT.get(), "" + cur.maxTotalCount(), ints),
				FormScreen.FormField.text(HostilityEditorLang.SPAWN_INTERVAL.get(), "" + cur.spawnInterval(), ints)
		), v -> new EntityConfig.MasterConfig(Integer.parseInt(v.get(0).trim()),
				Integer.parseInt(v.get(1).trim()), cur.minions()));
	}

	public static FormScreen.FormSpec<EntityConfig.Minion> minion(EntityType<?> type, @Nullable EntityConfig.Minion cur) {
		EntityConfig.Minion c = cur == null ? new EntityConfig.Minion(type, 1, 0, 1, 16, 200,
				false, false, 8, true, false, null) : cur;
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		Function<String, Component> doubles = HostilityEditorForms::doubleValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MAX_COUNT.get(), "" + c.maxCount(), ints),
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + c.minLevel(), ints),
				FormScreen.FormField.text(HostilityEditorLang.HEALTH_SCALE.get(), DoubleMapScreen.format(c.maxHealthPercentage()), doubles),
				FormScreen.FormField.text(HostilityEditorLang.SPAWN_RANGE.get(), "" + c.spawnRange(), ints),
				FormScreen.FormField.text(HostilityEditorLang.COOLDOWN.get(), "" + c.cooldown(), ints),
				FormScreen.FormField.bool(HostilityEditorLang.COPY_LEVEL.get(), c.copyLevel()),
				FormScreen.FormField.bool(HostilityEditorLang.COPY_TRAIT.get(), c.copyTrait()),
				FormScreen.FormField.text(HostilityEditorLang.SCALE.get(), DoubleMapScreen.format(c.linkDistance()), doubles),
				FormScreen.FormField.bool(HostilityEditorLang.PROTECT_MASTER.get(), c.protectMaster()),
				FormScreen.FormField.bool(HostilityEditorLang.DISCARD_ON_UNLINK.get(), c.discardOnUnlink())
		), v -> new EntityConfig.Minion(type,
				Integer.parseInt(v.get(0).trim()), Integer.parseInt(v.get(1).trim()),
				Double.parseDouble(v.get(2).trim()), Integer.parseInt(v.get(3).trim()),
				Integer.parseInt(v.get(4).trim()), v.get(5).equals("true"), v.get(6).equals("true"),
				Double.parseDouble(v.get(7).trim()), v.get(8).equals("true"), v.get(9).equals("true"),
				c.traits()));
	}

	public static FormScreen.FormSpec<EntityConfig.Config> entityValues(EntityConfig.Config cur) {
		Function<String, Component> ints = HostilityEditorForms::intValidate;
		Function<String, Component> doubles = HostilityEditorForms::doubleValidate;
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + cur.minSpawnLevel, ints),
				FormScreen.FormField.text(HostilityEditorLang.MAX_LEVEL.get(), "" + cur.maxLevel, ints),
				FormScreen.FormField.text(HostilityEditorLang.MAX_TRAIT_COUNT.get(), "" + cur.maxTraitCount, ints),
				FormScreen.FormField.text(HostilityEditorLang.HEALTH_SCALE.get(), DoubleMapScreen.format(cur.healthScale), doubles),
				FormScreen.FormField.text(HostilityEditorLang.ATTACK_SCALE.get(), DoubleMapScreen.format(cur.attackScale), doubles),
				FormScreen.FormField.bool(HostilityEditorLang.PRESET_TRAITS_ONLY.get(), cur.presetTraitsOnly)
		), v -> {
			cur.minSpawnLevel = Integer.parseInt(v.get(0).trim());
			cur.maxLevel = Integer.parseInt(v.get(1).trim());
			cur.maxTraitCount = Integer.parseInt(v.get(2).trim());
			cur.healthScale = Double.parseDouble(v.get(3).trim());
			cur.attackScale = Double.parseDouble(v.get(4).trim());
			cur.presetTraitsOnly = v.get(5).equals("true");
			return cur;
		});
	}

	@Nullable
	private static Component optionalInt(String s) {
		if (s.isBlank()) return null;
		try {
			Integer.parseInt(s.trim());
			return null;
		} catch (NumberFormatException e) {
			return HostilityEditorLang.INVALID_INTEGER.get(s);
		}
	}

	@Nullable
	private static Component optionalDouble(String s) {
		if (s.isBlank()) return null;
		try {
			Double.parseDouble(s.trim());
			return null;
		} catch (NumberFormatException e) {
			return HostilityEditorLang.INVALID_DOUBLE.get(s);
		}
	}

	public static Component difficultySummary(WorldDifficultyConfig.DifficultyConfig c) {
		if (defaultDifficulty(c)) return HostilityEditorLang.SUMMARY_DEFAULT.get();
		java.util.ArrayList<Component> parts = new java.util.ArrayList<>();
		if (c.min() != 0) parts.add(HostilityEditorLang.SUMMARY_MIN_LV.get(c.min()));
		if (c.base() != 0) parts.add(HostilityEditorLang.SUMMARY_BASE.get(c.base()));
		if (c.variation() != 0) parts.add(HostilityEditorLang.SUMMARY_VAR.get(DoubleMapScreen.format(c.variation())));
		if (c.scale() != 0) parts.add(HostilityEditorLang.SUMMARY_SCALE.get(DoubleMapScreen.format(c.scale() * 100) + "%"));
		if (c.apply_chance() != 1) parts.add(HostilityEditorLang.SUMMARY_APPLY.get(DoubleMapScreen.format(c.apply_chance() * 100) + "%"));
		if (c.trait_chance() != 1) parts.add(HostilityEditorLang.SUMMARY_TRAIT_CHANCE.get(DoubleMapScreen.format(c.trait_chance() * 100) + "%"));
		if (c.suppression() != 0) parts.add(HostilityEditorLang.SUMMARY_SUPPRESS.get(DoubleMapScreen.format(c.suppression() * 100) + "%"));
		return summaryGray(parts.toArray(new Component[0]));
	}

	public static Component itemConfigSummary(WeaponConfig.ItemConfig c) {
		return summary(
				HostilityEditorLang.SUMMARY_LV.get(c.level()),
				HostilityEditorLang.SUMMARY_W.get(c.weight()),
				HostilityEditorLang.SUMMARY_ITEMS.get(c.stack().size()));
	}

	public static Component enchConfigSummary(WeaponConfig.EnchConfig c) {
		return summary(
				HostilityEditorLang.SUMMARY_ENCHANTS.get(c.enchantments().size()),
				HostilityEditorLang.SUMMARY_LV.get(c.level()),
				HostilityEditorLang.SUMMARY_PCT.get(DoubleMapScreen.format(c.chance() * 100)));
	}

	public static Component configSummary(EntityConfig.Config c) {
		return entry(entityListName(c.entities),
				HostilityEditorLang.SUMMARY_TRAITS.get(c.traits().size()),
				difficultySummary(c.difficulty()));
	}

	/**
	 * First entity name plus the total count, or a single "All entities" label when empty.
	 */
	public static Component entityListName(List<EntityType<?>> entities) {
		if (entities.isEmpty()) return HostilityEditorLang.ALL_ENTITIES.get();
		Component name = HostilityEditorUtil.entityName(entities.get(0));
		if (entities.size() == 1) return name;
		return name.copy().append(Component.literal(" ... (" + entities.size() + ")").withStyle(ChatFormatting.WHITE));
	}

	public static Component traitBaseSummary(EntityConfig.TraitBase t) {
		Component name = t.trait() == null ? Component.literal("?") : t.trait().getDesc();
		MutableComponent ans = entry(name, summary(
				HostilityEditorLang.SUMMARY_FREE.get(t.free()),
				HostilityEditorLang.SUMMARY_MIN.get(t.min())));
		if (t.cap()) ans.append(Component.literal("  ")).append(HostilityEditorLang.SUMMARY_CAP.get().withStyle(ChatFormatting.GRAY));
		return ans;
	}

	public static Component itemPoolSummary(EntityConfig.ItemPool p) {
		return entry(Component.literal("[" + p.slot() + "]"),
				summary(HostilityEditorLang.SUMMARY_LV.get(p.level()),
						HostilityEditorLang.SUMMARY_PCT.get(DoubleMapScreen.format(p.chance() * 100)),
						HostilityEditorLang.SUMMARY_ENTRIES.get(p.entries().size())));
	}

	public static Component itemEntrySummary(EntityConfig.ItemEntry e) {
		return HostilityEditorLang.SUMMARY_W.get(e.weight());
	}

	public static Component minionSummary(EntityConfig.Minion m) {
		return entry(HostilityEditorUtil.entityName(m.type()),
				HostilityEditorLang.SUMMARY_HP.get(DoubleMapScreen.format(m.maxHealthPercentage() * 100) + "%"));
	}

	public static Component traitFieldsSummary(TraitConfig c) {
		return summaryGray(
				HostilityEditorLang.SUMMARY_MIN.get(c.min_level),
				HostilityEditorLang.SUMMARY_COST.get(c.cost),
				HostilityEditorLang.SUMMARY_RANK.get(c.max_rank),
				HostilityEditorLang.SUMMARY_W.get(c.weight));
	}

	public static Component entityValuesSummary(EntityConfig.Config c) {
		if (defaultValues(c)) return HostilityEditorLang.SUMMARY_DEFAULT.get();
		java.util.ArrayList<Component> parts = new java.util.ArrayList<>();
		if (c.minSpawnLevel != 0) parts.add(HostilityEditorLang.SUMMARY_MIN_SPAWN.get(c.minSpawnLevel));
		if (c.maxLevel != 0) parts.add(HostilityEditorLang.SUMMARY_MAX_LV.get(c.maxLevel));
		if (c.maxTraitCount != -1) parts.add(HostilityEditorLang.SUMMARY_MAX_TRAIT.get(c.maxTraitCount));
		if (c.healthScale != 1) parts.add(HostilityEditorLang.SUMMARY_HP_SCALE.get(DoubleMapScreen.format(c.healthScale * 100) + "%"));
		if (c.attackScale != 1) parts.add(HostilityEditorLang.SUMMARY_ATK_SCALE.get(DoubleMapScreen.format(c.attackScale * 100) + "%"));
		if (c.presetTraitsOnly) parts.add(HostilityEditorLang.SUMMARY_PRESET.get());
		return summaryGray(parts.toArray(new Component[0]));
	}

	public static Component masterSummary(EntityConfig.MasterConfig m) {
		return summaryGray(
				HostilityEditorLang.SUMMARY_MAX_TOTAL.get(m.maxTotalCount()),
				HostilityEditorLang.SUMMARY_INTERVAL.get(m.spawnInterval()),
				HostilityEditorLang.SUMMARY_MINIONS.get(m.minions().size()));
	}

	public static boolean defaultDifficulty(WorldDifficultyConfig.DifficultyConfig c) {
		return c.min() == 0 && c.base() == 0 && c.variation() == 0 && c.scale() == 0
				&& c.apply_chance() == 1 && c.trait_chance() == 1 && c.suppression() == 0;
	}

	public static boolean defaultValues(EntityConfig.Config c) {
		return c.minSpawnLevel == 0 && c.maxLevel == 0 && c.maxTraitCount == -1
				&& c.healthScale == 1 && c.attackScale == 1 && !c.presetTraitsOnly;
	}

	public static boolean hasMaster(EntityConfig.Config c) {
		return c.asMaster != null
				|| c.traits().stream().anyMatch(t -> t.trait() == LHTraits.MASTER.get());
	}

	/**
	 * First part in default (white) color, remaining parts in gray.
	 */
	public static MutableComponent entry(Component name, Component... details) {
		MutableComponent ans = name.copy();
		for (Component d : details) {
			ans.append(Component.literal("  ")).append(d.copy().withStyle(ChatFormatting.GRAY));
		}
		return ans;
	}

	public static Component counted(Component base, int n) {
		return base.copy().append(Component.literal("  (" + n + ")").withStyle(ChatFormatting.WHITE));
	}

	private static MutableComponent summary(Component... parts) {
		MutableComponent ans = Component.empty();
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) ans.append(Component.literal("  "));
			ans.append(parts[i]);
		}
		return ans;
	}

	private static MutableComponent summaryGray(Component... parts) {
		MutableComponent ans = Component.empty();
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) ans.append(Component.literal("  "));
			ans.append(parts[i].copy().withStyle(ChatFormatting.GRAY));
		}
		return ans;
	}

}
