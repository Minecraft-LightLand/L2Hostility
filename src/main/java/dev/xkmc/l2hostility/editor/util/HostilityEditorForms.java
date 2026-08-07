package dev.xkmc.l2hostility.editor.util;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.editor.base.DoubleMapScreen;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public final class HostilityEditorForms {

	private HostilityEditorForms() {
	}

	public static Function<String, Component> intValidate() {
		return s -> {
			try {
				Integer.parseInt(s.trim());
				return null;
			} catch (NumberFormatException e) {
				return HostilityEditorLang.INVALID_INTEGER.get(s);
			}
		};
	}

	public static Function<String, Component> doubleValidate() {
		return s -> {
			try {
				Double.parseDouble(s.trim());
				return null;
			} catch (NumberFormatException e) {
				return HostilityEditorLang.INVALID_DOUBLE.get(s);
			}
		};
	}

	@Nullable
	public static Function<String, Component> optionalRlValidate() {
		return s -> {
			if (s.isBlank()) return null;
			ResourceLocation id = dev.xkmc.l2hostility.editor.base.EditorFile.parseId(s);
			return id == null ? EditorText.INVALID_ID.get(s) : null;
		};
	}

	public static FormScreen.FormSpec<WorldDifficultyConfig.DifficultyConfig> difficultyConfig(WorldDifficultyConfig.DifficultyConfig cur) {
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
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
		Function<String, Component> ints = intValidate();
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

	public static FormScreen.FormSpec<WeaponConfig.ItemConfig> itemConfig() {
		Function<String, Component> ints = intValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "0", ints),
				FormScreen.FormField.text(HostilityEditorLang.WEIGHT.get(), "100", ints)
		), v -> new WeaponConfig.ItemConfig(new java.util.ArrayList<>(),
				Integer.parseInt(v.get(0).trim()), Integer.parseInt(v.get(1).trim())));
	}

	public static FormScreen.FormSpec<WeaponConfig.ItemConfig> itemConfigForm(int level, int weight) {
		Function<String, Component> ints = intValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "" + level, ints),
				FormScreen.FormField.text(HostilityEditorLang.WEIGHT.get(), "" + weight, ints)
		), v -> new WeaponConfig.ItemConfig(new java.util.ArrayList<>(),
				Integer.parseInt(v.get(0).trim()), Integer.parseInt(v.get(1).trim())));
	}

	public static FormScreen.FormSpec<WeaponConfig.EnchConfig> enchConfig() {
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "0", ints),
				FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), "0", doubles)
		), v -> new WeaponConfig.EnchConfig(new java.util.ArrayList<>(),
				Integer.parseInt(v.get(0).trim()), (float) Double.parseDouble(v.get(1).trim())));
	}

	public static FormScreen.FormSpec<WeaponConfig.EnchConfig> enchConfigForm(int level, float chance) {
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "" + level, ints),
				FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), "" + chance, doubles)
		), v -> new WeaponConfig.EnchConfig(new java.util.ArrayList<>(),
				Integer.parseInt(v.get(0).trim()), (float) Double.parseDouble(v.get(1).trim())));
	}

	public static FormScreen.FormSpec<EntityConfig.TraitBase> traitBase(MobTrait trait, @Nullable EntityConfig.TraitBase cur) {
		EntityConfig.TraitBase c = cur == null ? new EntityConfig.TraitBase(trait, 0, 0, false, null) : cur;
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.FREE.get(), "" + c.free(), ints),
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + c.min(), ints),
				FormScreen.FormField.bool(HostilityEditorLang.CAP.get(), c.cap()),
				FormScreen.FormField.text(HostilityEditorLang.LV.get(), c.condition() == null ? "" : "" + c.condition().lv(), optionalInt()),
				FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), c.condition() == null ? "" : DoubleMapScreen.format(c.condition().chance()), optionalDouble()),
				FormScreen.FormField.text(HostilityEditorLang.ADVANCEMENT_ID.get(), c.condition() == null || c.condition().id() == null ? "" : c.condition().id().toString(), optionalRlValidate())
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
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "0", ints),
				FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), "1", doubles),
				FormScreen.FormField.text(HostilityEditorLang.SLOT.get(), "mainhand", null)
		), v -> new EntityConfig.ItemPool(Integer.parseInt(v.get(0).trim()),
				(float) Double.parseDouble(v.get(1).trim()), v.get(2).trim(), new java.util.ArrayList<>()));
	}

	public static FormScreen.FormSpec<EntityConfig.MasterConfig> masterConfig(EntityConfig.MasterConfig cur) {
		Function<String, Component> ints = intValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MAX_TOTAL_COUNT.get(), "" + cur.maxTotalCount(), ints),
				FormScreen.FormField.text(HostilityEditorLang.SPAWN_INTERVAL.get(), "" + cur.spawnInterval(), ints)
		), v -> new EntityConfig.MasterConfig(Integer.parseInt(v.get(0).trim()),
				Integer.parseInt(v.get(1).trim()), cur.minions()));
	}

	public static FormScreen.FormSpec<EntityConfig.Minion> minion(EntityType<?> type, @Nullable EntityConfig.Minion cur) {
		EntityConfig.Minion c = cur == null ? new EntityConfig.Minion(type, 1, 0, 1, 16, 200,
				false, false, 8, true, false, null) : cur;
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
		return new FormScreen.FormSpec<>(List.of(
				FormScreen.FormField.text(HostilityEditorLang.MAX_LEVEL.get(), "" + c.maxCount(), ints),
				FormScreen.FormField.text(HostilityEditorLang.MIN_LEVEL.get(), "" + c.minLevel(), ints),
				FormScreen.FormField.text(HostilityEditorLang.HEALTH_SCALE.get(), DoubleMapScreen.format(c.maxHealthPercentage()), doubles),
				FormScreen.FormField.text(HostilityEditorLang.SPAWN_INTERVAL.get(), "" + c.spawnRange(), ints),
				FormScreen.FormField.text(HostilityEditorLang.SUPPRESSION.get(), "" + c.cooldown(), ints),
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
		Function<String, Component> ints = intValidate();
		Function<String, Component> doubles = doubleValidate();
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

	private static Function<String, Component> optionalInt() {
		return s -> {
			if (s.isBlank()) return null;
			try {
				Integer.parseInt(s.trim());
				return null;
			} catch (NumberFormatException e) {
				return HostilityEditorLang.INVALID_INTEGER.get(s);
			}
		};
	}

	private static Function<String, Component> optionalDouble() {
		return s -> {
			if (s.isBlank()) return null;
			try {
				Double.parseDouble(s.trim());
				return null;
			} catch (NumberFormatException e) {
				return HostilityEditorLang.INVALID_DOUBLE.get(s);
			}
		};
	}

	public static Component difficultySummary(WorldDifficultyConfig.DifficultyConfig c) {
		return Component.literal("lv " + c.min() + "  base " + c.base()
				+ "  var " + DoubleMapScreen.format(c.variation())
				+ "  scale " + DoubleMapScreen.format(c.scale()));
	}

	public static Component itemConfigSummary(WeaponConfig.ItemConfig c) {
		return Component.literal("lv " + c.level() + "  w " + c.weight()
				+ "  (" + c.stack().size() + " items)");
	}

	public static Component enchConfigSummary(WeaponConfig.EnchConfig c) {
		return Component.literal(c.enchantments().size() + " enchants  lv " + c.level()
				+ "  " + DoubleMapScreen.format(c.chance() * 100) + "%");
	}

	public static Component configSummary(EntityConfig.Config c) {
		String ents = c.entities.isEmpty() ? "all" : "" + c.entities.size();
		return Component.literal(ents + " entities  " + c.traits().size() + " traits  "
				+ difficultySummary(c.difficulty()));
	}

	public static Component traitBaseSummary(EntityConfig.TraitBase t) {
		Component name = t.trait() == null ? Component.literal("?") : t.trait().getDesc();
		return name.copy().append(Component.literal("  free " + t.free()
				+ "  min " + t.min() + (t.cap() ? "  cap" : "")));
	}

	public static Component itemPoolSummary(EntityConfig.ItemPool p) {
		return Component.literal("[" + p.slot() + "]  lv " + p.level()
				+ "  " + DoubleMapScreen.format(p.chance() * 100) + "%  ("
				+ p.entries().size() + " entries)");
	}

	public static Component itemEntrySummary(EntityConfig.ItemEntry e) {
		return Component.literal("w " + e.weight());
	}

	public static Component minionSummary(EntityConfig.Minion m) {
		return HostilityEditorUtil.entityName(m.type()).copy().append(Component.literal("  x" + m.maxCount()));
	}

}
