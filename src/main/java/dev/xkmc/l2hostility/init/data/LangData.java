package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2hostility.compat.patchouli.PatchouliLang;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public enum LangData {
	TOOLTIP_DISABLE("tooltip.disable", "%s enchantments disabled for %ss. Any new enchantment will vanish.", 2),
	TOOLTIP_SELF_EFFECT("tooltip.self_effect", "Mob gains continuous effect: ", 0),
	TOOLTIP_TARGET_EFFECT("tooltip.target_effect", "Mob inflict effect on hit: ", 0),
	TOOLTIP_LEGENDARY("tooltip.legendary", "Legendary", 0),

	ITEM_WAND_AI("item.wand.ai", "Right click to remove or restore mob AI.", 0),
	ITEM_WAND_EQUIPMENT("item.wand.equipment", "Right click to open mob equipment menu.", 0),
	ITEM_WAND_TARGET("item.wand.target", "Right click 2 mobs to make them fight each other.", 0),
	ITEM_WAND_ADDER("item.wand.adder", "Right click blocks to select trait. Right click mobs to select trait rank. Press shift to select in opposite direction.", 0),

	ITEM_GLASSES("item.equipment.glasses", "Allow you to see invisible mobs, and see mobs when you have blindness or darkness effects", 0),
	ITEM_SPAWNER("item.spawner", "Summon strong mobs. Kill them all to make a chunk section safe", 0),
	ITEM_ORB("item.consumable.orb", "Make a chunk section safe. Useful when building your base. Use wisely.", 0),

	MSG_AI("msg.ai", "Configure %s: Set NoAI to %s.", 2),
	MSG_SET_TARGET("msg.set_target", "Set %s and %s to fight", 2),
	MSG_TARGET_FAIL("msg.target_fail", "%s and %s cannot fight", 2),
	MSG_TARGET_RECORD("msg.target_record", "Recorded %s", 1),
	MSG_SET_TRAIT("msg.set_trait", "Set trait %1$s on entity %2$s to level %3$s", 3),
	MSG_SELECT_TRAIT("msg.select_trait", "Selected trait: %s", 1),
	MSG_ERR_MAX("msg.err_max", "Trait level already reached max value", 0),
	MSG_ERR_DISALLOW("msg.err_disallow", "Trait not applicable on this entity", 0),

	INFO_PLAYER_LEVEL("info.player_level", "Player difficulty level: %s", 1),
	INFO_PLAYER_EXP("info.player_exp", "Difficulty progress: %s%%", 1),
	INFO_PLAYER_CAP("info.player_cap", "Mob trait rank limit: %s", 1),
	INFO_CHUNK_LEVEL("info.chunk_level", "Chunk base difficulty: %s", 1),
	INFO_CHUNK_SCALE("info.chunk_scale", "Chunk difficulty scale: %s", 1),
	INFO_CHUNK_CLEAR("info.clear", "Chunk Section difficulty cleared", 0),
	INFO_TAB_TITLE("info.title", "Difficulty Information", 0),
	INFO_REWARD("info.reward", "Obtained %s rewards", 1),

	BOSS_EVENT("boss_event", "Hostility Clearing Progress: %s/%s", 2),

	LOOT_TITLE("jei.loot_title", "Trait Loot", 0),
	LOOT_CHANCE("jei.loot_chance", "%s%% chance to drop for %s rank %s", 3),
	LOOT_MIN_LEVEL("jei.min_level", "Drops on mobs with level %s or higher", 1),
	LOOT_OTHER_TRAIT("jei.other_trait", "Requires mobs to also have %s at rank %s", 2),

	COMMAND_PLAYER_SUCCEED("command.player.success", "Performed actions on %s players", 1),
	COMMAND_PLAYER_FAIL("command.player.fail", "Command has no target or no effect", 0),
	COMMAND_PLAYER_GET_BASE("command.player.get_base", "%s has base difficulty level %s", 2),
	COMMAND_PLAYER_GET_TOTAL("command.player.get_total", "%s has overall difficulty level %s", 2),
	COMMAND_PLAYER_GET_DIM("command.player.get_dim", "%s has visited %s dimensions", 2),
	COMMAND_PLAYER_GET_TRAIT_CAP("command.player.trait_cap", "The max rank %s has killed is rank %s", 2),

	PATCHOULI_TITLE("patchouli.title", "L2Hostility Guide", 0),
	PATCHOULI_LANDING("patchouli.landing", "Welcome to Champion-like difficulty scaling mod", 0),
	;

	final String id, def;
	final int count;

	LangData(String id, String def, int count) {
		this.id = id;
		this.def = def;
		this.count = count;
	}

	public MutableComponent get(Object... objs) {
		if (objs.length != count)
			throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + objs.length);
		return translate(L2Hostility.MODID + "." + id, objs);
	}


	public static void addTranslations(RegistrateLangProvider pvd) {
		for (LangData id : LangData.values()) {
			String[] strs = id.id.split("\\.");
			String str = strs[strs.length - 1];
			pvd.add(L2Hostility.MODID + "." + id.id, id.def);
		}
		pvd.add("config.jade.plugin_l2hostility.mob", "L2Hostility");
		PatchouliLang.genLang(pvd);
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent translate(String key, Object... objs) {
		return Component.translatable(key, objs);
	}

}
