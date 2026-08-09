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
	SUMMARY_SUPPRESS("editor.summary_suppress", "suppress %s", 1, null),

	// Field tooltips (shown when hovering a form row)
	DIFF_MIN_TIP("editor.tip_diff_min", "Lowest level mobs can have. Mobs below it are brought up to this level.", 0, ChatFormatting.GRAY),
	DIFF_BASE_TIP("editor.tip_diff_base", "Base level added to the difficulty of this entry. Combined with the world difficulty it decides the mob level.", 0, ChatFormatting.GRAY),
	DIFF_VAR_TIP("editor.tip_diff_var", "Random variation of the difficulty around the base level. Higher values make mob levels more spread out.", 0, ChatFormatting.GRAY),
	DIFF_SCALE_TIP("editor.tip_diff_scale", "Multiplier on the accumulated difficulty. Mob level = base + difficulty * scale.", 0, ChatFormatting.GRAY),
	DIFF_APPLY_TIP("editor.tip_diff_apply", "Chance that health/damage scaling and traits actually apply. 1 applies to every mob, values below 1 skip some.", 0, ChatFormatting.GRAY),
	DIFF_TRAIT_CHANCE_TIP("editor.tip_diff_trait_chance", "Multiplied chance that a trait is rolled for a mob, on top of the global trait chance.", 0, ChatFormatting.GRAY),
	DIFF_SUPPRESS_TIP("editor.tip_diff_suppress", "Chance to stop after one trait is added. 0 (with a minimum level set) guarantees the trait roll to always apply.", 0, ChatFormatting.GRAY),

	TRAIT_MIN_TIP("editor.tip_trait_min", "Mobs below this level cannot spawn with this trait.", 0, ChatFormatting.GRAY),
	TRAIT_COST_TIP("editor.tip_trait_cost", "Difficulty level points spent for one rank of this trait when a mob is rolled.", 0, ChatFormatting.GRAY),
	TRAIT_MAX_RANK_TIP("editor.tip_trait_max_rank", "Highest rank this trait can reach on a mob.", 0, ChatFormatting.GRAY),
	TRAIT_WEIGHT_TIP("editor.tip_trait_weight", "How likely this trait is picked when the mob has multiple valid traits. Higher = more common.", 0, ChatFormatting.GRAY),
	TRAIT_TOGGLE_TIP("editor.tip_trait_toggle", "When off, this trait never spawns on any mob.", 0, ChatFormatting.GRAY),

	FREE_TIP("editor.tip_free", "Ranks of this trait given for free before any level is spent.", 0, ChatFormatting.GRAY),
	RANK_MIN_TIP("editor.tip_rank_min", "Ranks this preset trait is scaled up to. If the mob cannot afford the cost, only the affordable ranks are applied instead.", 0, ChatFormatting.GRAY),
	CAP_TIP("editor.tip_cap", "When on, this trait is not rolled randomly - only its preset rank is applied.", 0, ChatFormatting.GRAY),
	LV_TIP("editor.tip_lv", "Condition: mob level must be at least this value or the trait is skipped.", 0, ChatFormatting.GRAY),
	CHANCE_TIP("editor.tip_chance", "Condition: chance for the extra preset trait to be applied.", 0, ChatFormatting.GRAY),
	ADVANCEMENT_TIP("editor.tip_advancement", "Condition: player closest must have this advancement to get the trait. Leave blank for none.", 0, ChatFormatting.GRAY),

	SLOT_TIP("editor.tip_slot", "Slot the item is equipped in: an equipment slot (e.g. equipment/mainhand, equipment/head) or a curio slot (e.g. curios/ring).", 0, ChatFormatting.GRAY),
	ITEM_LEVEL_TIP("editor.tip_item_level", "Mob level required for this pool of items to be equipped on the mob.", 0, ChatFormatting.GRAY),
	ITEM_CHANCE_TIP("editor.tip_item_chance", "Chance that one item from this pool is equipped on the mob.", 0, ChatFormatting.GRAY),

	ENTITY_MIN_TIP("editor.tip_entity_min", "Mobs assigned a level lower than this value are prevented from spawning.", 0, ChatFormatting.GRAY),
	ENTITY_MAX_TIP("editor.tip_entity_max", "Maximum level a mob of this type can have.", 0, ChatFormatting.GRAY),
	ENTITY_MAX_TRAIT_TIP("editor.tip_entity_max_trait", "Max number of random traits a mob of this type can have. -1 uses the global value.", 0, ChatFormatting.GRAY),
	HEALTH_SCALE_TIP("editor.tip_health_scale", "Extra health multiplier for mobs in this entity config, multiplies the global health scaling.", 0, ChatFormatting.GRAY),
	ATTACK_SCALE_TIP("editor.tip_attack_scale", "Extra damage multiplier for mobs in this entity config, multiplies the global damage scaling.", 0, ChatFormatting.GRAY),
	PRESET_ONLY_TIP("editor.tip_preset_only", "When on, the traits below are the only traits the mob can get - no random traits are rolled.", 0, ChatFormatting.GRAY),

	MAX_COUNT_TIP("editor.master.max_count", "Maximum number of this minion type a master can keep alive at the same time.", 0, ChatFormatting.GRAY),
	MINION_MIN_TIP("editor.master.minion_min", "Minimum level the master must have to spawn this minion type.", 0, ChatFormatting.GRAY),
	MINION_HP_TIP("editor.master.minion_hp", "Minions of this type spawn only while the master's health is at or below this percentage of its max health.", 0, ChatFormatting.GRAY),
	SPAWN_RANGE_TIP("editor.master.spawn_range", "Radius around the master (in blocks) where minions are spawned.", 0, ChatFormatting.GRAY),
	COOLDOWN_TIP("editor.master.cooldown", "Time, in ticks, between two spawns of this minion type.", 0, ChatFormatting.GRAY),
	COPY_LEVEL_TIP("editor.master.copy_level", "Minions spawn at the same level as the master when on.", 0, ChatFormatting.GRAY),
	COPY_TRAIT_TIP("editor.master.copy_trait", "Minions copy the master's traits when on.", 0, ChatFormatting.GRAY),
	LINK_DISTANCE_TIP("editor.master.link_distance", "Max distance from the master (in blocks) before the minion is disconnected and discarded.", 0, ChatFormatting.GRAY),
	PROTECT_MASTER_TIP("editor.master.protect_master", "The master is immune to all damage while this minion is alive and connected.", 0, ChatFormatting.GRAY),
	DISCARD_UNLINK_TIP("editor.master.discard_unlink", "Minions vanish as soon as the link distance is exceeded.", 0, ChatFormatting.GRAY),
	MASTER_MAX_TOTAL_TIP("editor.master.max_total", "Highest total number of minions this master keeps alive.", 0, ChatFormatting.GRAY),
	MASTER_SPAWN_INTERVAL_TIP("editor.master.spawn_interval", "Delay, in ticks, between minion spawn attempts of this master.", 0, ChatFormatting.GRAY),

	// Button / row tooltips
	ADD_ENTITY_TIP("editor.tip_add_entity", "Add an entity type to this tag.", 0, ChatFormatting.GRAY),
	ADD_TAG_TIP("editor.tip_add_tag", "Add a nested tag reference (must start with '#').", 0, ChatFormatting.GRAY),
	EDIT_TAG_TIP("editor.tip_edit_tag", "Toggle whether this entry is required or optional.", 0, ChatFormatting.GRAY),
	REMOVE_MASTER_TIP("editor.tip_remove_master", "Remove the master behaviour from this entity entirely.", 0, ChatFormatting.GRAY),
	ADD_MASTER_TIP("editor.tip_add_master", "Make this entity a master that spawns and commands minions.", 0, ChatFormatting.GRAY),
	ADD_ENTITY_CONFIG_TIP("editor.tip_add_entity_config", "Create a new configuration entry for a mob type.", 0, ChatFormatting.GRAY),
	EDIT_ENTITY_CONFIG_TIP("editor.tip_edit_entity_config", "Edit the selected configuration entry.", 0, ChatFormatting.GRAY),
	REMOVE_ENTITY_CONFIG_TIP("editor.tip_remove_entity_config", "Remove the selected configuration entry.", 0, ChatFormatting.GRAY),

	ROW_DIMENSIONS_TIP("editor.tip_row_dimensions", "Per-dimension difficulty values for mobs spawning in that dimension.", 0, ChatFormatting.GRAY),
	ROW_BIOMES_TIP("editor.tip_row_biomes", "Per-biome difficulty values for mobs spawning in that biome.", 0, ChatFormatting.GRAY),
	ROW_LEVEL_DEF_TRAITS_TIP("editor.tip_row_level_traits", "Preset traits given to mobs spawning in this dimension.", 0, ChatFormatting.GRAY),
	ROW_STRUCTURE_DEF_TRAITS_TIP("editor.tip_row_structure_traits", "Preset traits given to mobs spawning inside these structures.", 0, ChatFormatting.GRAY),
	ROW_TRAIT_FIELDS_TIP("editor.tip_row_trait_fields", "Level, cost and roll weight of this trait.", 0, ChatFormatting.GRAY),
	ROW_TRAIT_CONFIG_TIP("editor.tip_row_trait_config", "Global config values this trait reads (allow toggle, duration, damage...).", 0, ChatFormatting.GRAY),
	ROW_BLACKLIST_TIP("editor.tip_row_blacklist", "Entities that can never get this trait.", 0, ChatFormatting.GRAY),
	ROW_WHITELIST_TIP("editor.tip_row_whitelist", "Entities forced to get this trait (as a possible target).", 0, ChatFormatting.GRAY),
	ROW_MELEE_TIP("editor.tip_row_melee", "Melee weapons held by leveled mobs.", 0, ChatFormatting.GRAY),
	ROW_RANGED_TIP("editor.tip_row_ranged", "Bows/crossbows held by leveled mobs.", 0, ChatFormatting.GRAY),
	ROW_ARMOR_TIP("editor.tip_row_armor", "Armor pieces with which leveled mobs are equipped.", 0, ChatFormatting.GRAY),
	ROW_SPECIAL_TIP("editor.tip_row_special", "Special weapons (shield/trident/wand...) given to leveled mobs.", 0, ChatFormatting.GRAY),
	ROW_WEAPON_ENCH_TIP("editor.tip_row_weapon_ench", "Enchantments added to the weapons of leveled mobs.", 0, ChatFormatting.GRAY),
	ROW_ARMOR_ENCH_TIP("editor.tip_row_armor_ench", "Enchantments added to the armor of leveled mobs.", 0, ChatFormatting.GRAY),

	ROW_APPLIES_TO_TIP("editor.tip_row_applies", "Which entity types this config applies to. Empty = all entities.", 0, ChatFormatting.GRAY),
	ROW_DIFF_TIP("editor.tip_row_diff", "Per-entity difficulty overrides for these entity types.", 0, ChatFormatting.GRAY),
	ROW_TRAITS_TIP("editor.tip_row_traits", "Preset traits these entities receive. Conditions can limit when they apply.", 0, ChatFormatting.GRAY),
	ROW_BLACK_TIP("editor.tip_row_black", "Traits that are never rolled for these entities.", 0, ChatFormatting.GRAY),
	ROW_ITEMS_TIP("editor.tip_row_items", "Extra items these entities can drop, slot and level depending.", 0, ChatFormatting.GRAY),
	ROW_VALUES_TIP("editor.tip_row_values", "Level cap, health/damage scale and trait limits for these entities.", 0, ChatFormatting.GRAY),
	ROW_MASTER_TIP("editor.tip_row_master", "Turn these entities into masters that command minions.", 0, ChatFormatting.GRAY),

	TAB_WORLD_TIP("editor.tip_tab_world", "Edit per-dimension and per-biome difficulty levels, scaling and default traits.", 0, ChatFormatting.GRAY),
	TAB_TRAIT_TIP("editor.tip_tab_trait", "Configure traits: level cost, weight, max rank and entity whitelist/blacklists.", 0, ChatFormatting.GRAY),
	TAB_WEAPON_TIP("editor.tip_tab_weapon", "Configure the melee weapons, armors, special items and enchantments level mobs spawn with.", 0, ChatFormatting.GRAY),
	TAB_ENTITY_TIP("editor.tip_tab_entity", "Configure per-entity types: spawning levels, stats scaling, items and traits.", 0, ChatFormatting.GRAY),
	TAB_TAGS_TIP("editor.tip_tab_tags", "Edit entity/block tags used by loot tables and trait filters.", 0, ChatFormatting.GRAY),
	TAB_CONFIG_TIP("editor.tip_tab_config", "Edit global numeric values: scaling, difficulty, items and performance.", 0, ChatFormatting.GRAY);

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
