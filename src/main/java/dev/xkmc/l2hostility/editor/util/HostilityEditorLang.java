package dev.xkmc.l2hostility.editor.util;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;

public enum HostilityEditorLang {
	DIFFICULTY("editor.difficulty", "Difficulty", 0, null),
	DIFFICULTY_FILE("editor.difficulty_file", "Difficulty file", 0, null),
	TRAIT("editor.trait", "Trait", 0, null),
	TRAIT_FILE("editor.trait_file", "Trait file", 0, null),
	WEAPON("editor.weapon", "Weapon", 0, null),
	WEAPON_FILE("editor.weapon_file", "Weapon file", 0, null),
	ENTITY("editor.entity", "Entity", 0, null),
	ENTITY_FILE("editor.entity_file", "Entity file", 0, null),
	TAGS("editor.tags", "Tags", 0, null),
	TAG_FILE("editor.tag_file", "Tag file", 0, null),
	CONFIG("editor.config", "Config", 0, null),
	CONFIG_EMPTY("editor.config_empty", "No config sections.", 0, ChatFormatting.GRAY),
	TRAIT_TOGGLE("editor.trait_toggle", "Trait toggle", 0, null),
	TRAIT_CONFIG("editor.trait_config", "Trait config", 0, null),
	CONFIG_ENABLED("editor.enabled", "Enabled", 0, null),
	CONFIG_DISABLED("editor.disabled", "Disabled", 0, ChatFormatting.RED),
	TAG_HASH_HINT("editor.tag_hash_hint", "Must start with '#'", 0, ChatFormatting.RED),
	EMPTY_NO_FILES("editor.empty_no_files", "No entries. Click New to create a file.", 0, ChatFormatting.GRAY),
	DIFFICULTY_EMPTY("editor.difficulty_empty", "No difficulty files loaded. Open a world first.", 0, ChatFormatting.GRAY),
	TRAIT_EMPTY("editor.trait_empty", "No traits registered.", 0, ChatFormatting.GRAY),
	WEAPON_EMPTY("editor.weapon_empty", "No weapon files loaded. Open a world first.", 0, ChatFormatting.GRAY),
	ENTITY_EMPTY("editor.entity_empty", "No entity files loaded. Open a world first.", 0, ChatFormatting.GRAY),
	TAG_EMPTY("editor.tag_empty", "No managed tags.", 0, ChatFormatting.GRAY),

	DIMENSIONS("editor.dimensions", "Dimensions", 0, null),
	BIOMES("editor.biomes", "Biomes", 0, null),
	LEVEL_DEFAULT_TRAITS("editor.level_default_traits", "Level default traits", 0, null),
	STRUCTURE_DEFAULT_TRAITS("editor.structure_default_traits", "Structure default traits", 0, null),
	MELEE_WEAPONS("editor.melee_weapons", "Melee weapons", 0, null),
	RANGED_WEAPONS("editor.ranged_weapons", "Ranged weapons", 0, null),
	ARMORS("editor.armors", "Armors", 0, null),
	SPECIAL_WEAPONS("editor.special_weapons", "Special weapons", 0, null),
	WEAPON_ENCHANTMENTS("editor.weapon_enchantments", "Weapon enchantments", 0, null),
	ARMOR_ENCHANTMENTS("editor.armor_enchantments", "Armor enchantments", 0, null),

	ENTITIES("editor.entities", "Entities", 0, null),
	DIFFICULTY_ROW("editor.difficulty_row", "Difficulty", 0, null),
	TRAITS("editor.traits", "Traits", 0, null),
	TRAIT_BLACKLIST("editor.trait_blacklist", "Trait blacklist", 0, null),
	ITEMS("editor.items", "Items", 0, null),
	VALUES("editor.values", "Values", 0, null),
	MASTER("editor.master", "Master", 0, null),

	SELECT_ENTITY("editor.select_entity", "Select entity type", 0, null),
	SELECT_TRAIT("editor.select_trait", "Select trait", 0, null),
	SELECT_ENCHANTMENT("editor.select_enchantment", "Select enchantment", 0, null),
	SELECT_BIOME("editor.select_biome", "Select biome", 0, null),
	SELECT_STRUCTURE("editor.select_structure", "Select structure", 0, null),
	SELECT_ITEM("editor.select_item", "Select item", 0, null),
	ADD_ENTITY("editor.add_entity", "Add entity", 0, null),
	ADD_TAG("editor.add_tag", "Add tag", 0, null),
	ADD_TRAIT("editor.add_trait", "Add trait", 0, null),
	ADD_ENCHANTMENT("editor.add_enchantment", "Add enchantment", 0, null),
	ADD_MASTER("editor.add_master", "Add master", 0, null),
	ENTRY("editor.entry", "Entry", 0, null),
	WEIGHT("editor.weight", "Weight", 0, null),
	LEVEL("editor.level", "Level", 0, null),
	CHANCE("editor.chance", "Chance", 0, null),
	SLOT("editor.slot", "Slot", 0, null),
	MIN_LEVEL("editor.min_level", "Minimum level", 0, null),
	MAX_LEVEL("editor.max_level", "Maximum level", 0, null),
	MAX_COUNT("editor.max_count", "Max count", 0, null),
	BASE("editor.base", "Base", 0, null),
	VARIATION("editor.variation", "Variation", 0, null),
	SCALE("editor.scale", "Scale", 0, null),
	APPLY_CHANCE("editor.apply_chance", "Apply chance", 0, null),
	TRAIT_CHANCE("editor.trait_chance", "Trait chance", 0, null),
	SUPPRESSION("editor.suppression", "Suppression", 0, null),
	COST("editor.cost", "Cost", 0, null),
	MAX_RANK("editor.max_rank", "Max rank", 0, null),
	MAX_TRAIT_COUNT("editor.max_trait_count", "Max trait count", 0, null),
	HEALTH_SCALE("editor.health_scale", "Health scale", 0, null),
	ATTACK_SCALE("editor.attack_scale", "Attack scale", 0, null),
	PRESET_TRAITS_ONLY("editor.preset_traits_only", "Preset traits only", 0, null),
	FREE("editor.free", "Free", 0, null),
	CAP("editor.cap", "Cap", 0, null),
	LV("editor.lv", "Trait level", 0, null),
	ADVANCEMENT_ID("editor.advancement_id", "Advancement id (optional)", 0, null),
	SPAWN_INTERVAL("editor.spawn_interval", "Spawn interval", 0, null),
	SPAWN_RANGE("editor.spawn_range", "Spawn range", 0, null),
	COOLDOWN("editor.cooldown", "Cooldown", 0, null),
	MAX_TOTAL_COUNT("editor.max_total_count", "Max total count", 0, null),
	MINIONS("editor.minions", "Minions", 0, null),
	REQUIRED("editor.required", "Required (false = optional)", 0, null),
	ADD_VALUE("editor.add_value", "Add value", 0, null),
	ADD_ITEM("editor.add_item", "Add item", 0, null),
	MINION_FIELDS("editor.minion_fields", "Minion fields", 0, null),
	COPY_LEVEL("editor.copy_level", "Copy master level", 0, null),
	COPY_TRAIT("editor.copy_trait", "Copy master traits", 0, null),
	PROTECT_MASTER("editor.protect_master", "Protect master", 0, null),
	DISCARD_ON_UNLINK("editor.discard_on_unlink", "Discard on unlink", 0, null),
	CONDITION("editor.condition", "Condition", 0, null),
	MASTER_FIELDS("editor.master_fields", "Master fields", 0, null),
	ITEM_POOL("editor.item_pool", "Item pool", 0, null),
	CONFIG_LIST("editor.config_list", "Configs", 0, null),
	ITEM_CONFIG("editor.item_config", "Item config", 0, null),
	ENCH_CONFIG("editor.ench_config", "Enchantment config", 0, null),
	TRAIT_FIELDS("editor.trait_fields", "Trait fields", 0, null),
	BLACKLIST_TAG("editor.blacklist_tag", "Blacklist", 0, null),
	WHITELIST_TAG("editor.whitelist_tag", "Whitelist", 0, null),
	ENTITY_CONFIG("editor.entity_config", "Entity config", 0, null),
	ENTITY_LIST("editor.entity_list", "Entities", 0, null),
	DIFFICULTY_EDIT("editor.difficulty_edit", "Difficulty", 0, null),
	TRAIT_BASE_LIST("editor.trait_base_list", "Traits", 0, null),
	ITEM_POOL_LIST("editor.item_pool_list", "Item pools", 0, null),
	ITEM_ENTRY_LIST("editor.item_entry_list", "Item entries", 0, null),
	MASTER_CONFIG("editor.master_config", "Master config", 0, null),
	MINION_LIST("editor.minion_list", "Minions", 0, null),
	REMOVE_MASTER("editor.remove_master", "Remove master", 0, null),
	ALL_ENTITIES("editor.all_entities", "All entities", 0, null),
	APPLIES_TO("editor.applies_to", "Applies to:", 0, null),
	WORLD("editor.world", "World", 0, null),
	VALUES_EDIT("editor.values_edit", "Values", 0, null),
	TAG_NOTE("editor.tag_note", "Edited tags are written with replace:true and override mod datapack values.", 0, ChatFormatting.GRAY),
	INVALID_DOUBLE("editor.invalid_double", "Not a valid number: %s", 1, ChatFormatting.RED),
	INVALID_INTEGER("editor.invalid_integer", "Not a valid integer: %s", 1, ChatFormatting.RED),
	EMPTY_OPTIONAL("editor.empty_optional", "Leave blank to keep as default", 0, ChatFormatting.GRAY),

	SUMMARY_LV("editor.summary_lv", "lv %s", 1, null),
	SUMMARY_MIN_LV("editor.summary_min_lv", "minLv %s", 1, null),
	SUMMARY_MIN("editor.summary_min", "min %s", 1, null),
	SUMMARY_BASE("editor.summary_base", "base %s", 1, null),
	SUMMARY_VAR("editor.summary_var", "var %s", 1, null),
	SUMMARY_SCALE("editor.summary_scale", "scale %s", 1, null),
	SUMMARY_COST("editor.summary_cost", "cost %s", 1, null),
	SUMMARY_RANK("editor.summary_rank", "rank %s", 1, null),
	SUMMARY_W("editor.summary_w", "w %s", 1, null),
	SUMMARY_ITEMS("editor.summary_items", "%s items", 1, null),
	SUMMARY_ENCHANTS("editor.summary_enchants", "%s enchants", 1, null),
	SUMMARY_ENTRIES("editor.summary_entries", "%s entries", 1, null),
	SUMMARY_TRAITS("editor.summary_traits", "%s traits", 1, null),
	SUMMARY_FREE("editor.summary_free", "free %s", 1, null),
	SUMMARY_CAP("editor.summary_cap", "cap", 0, null),
	SUMMARY_CONFIGS("editor.summary_configs", "%s configs", 1, null),
	SUMMARY_MIN_SPAWN("editor.summary_min_spawn", "minSpawn %s", 1, null),
	SUMMARY_MAX_LV("editor.summary_max_lv", "maxLv %s", 1, null),
	SUMMARY_MAX_TRAIT("editor.summary_max_trait", "maxTrait %s", 1, null),
	SUMMARY_MAX_TOTAL("editor.summary_max_total", "maxTotal %s", 1, null),
	SUMMARY_INTERVAL("editor.summary_interval", "interval %s", 1, null),
	SUMMARY_MINIONS("editor.summary_minions", "%s minions", 1, null),
	SUMMARY_HP("editor.summary_hp", "hp %s", 1, null),
	SUMMARY_ATK("editor.summary_atk", "atk %s", 1, null),
	SUMMARY_HP_SCALE("editor.summary_hp_scale", "hpScale %s", 1, null),
	SUMMARY_ATK_SCALE("editor.summary_atk_scale", "atkScale %s", 1, null),
	SUMMARY_PCT("editor.summary_pct", "%s%%", 1, null),
	STRUCTURE_SERVER_HINT("editor.structure_server_hint", " (singleplayer only)", 0, ChatFormatting.GRAY),
	SUMMARY_PRESET("editor.summary_preset", "preset", 0, null),
	SUMMARY_DEFAULT("editor.summary_default", "(default)", 0, null),
	SUMMARY_APPLY("editor.summary_apply", "apply %s", 1, null),
	SUMMARY_TRAIT_CHANCE("editor.summary_trait_chance", "traitChance %s", 1, null),
	SUMMARY_SUPPRESS("editor.summary_suppress", "suppress %s", 1, null);

	private final String key, def;
	private final int arg;
	@Nullable
	private final ChatFormatting format;

	HostilityEditorLang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = L2Hostility.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public String key() {
		return key;
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			ans = ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (HostilityEditorLang lang : HostilityEditorLang.values()) {
			pvd.add(lang.key, lang.def);
		}
	}

}
