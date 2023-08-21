package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public enum LangData {
	TOOLTIP_DISABLE("tooltip.disable", "%s enchantments disabled for %ss. Any new enchantment will vanish.", 2),
	TOOLTIP_SELF_EFFECT("tooltip.self_effect", "Mob gains continuous effect: ", 0),
	TOOLTIP_TARGET_EFFECT("tooltip.target_effect", "Mob inflict effect on hit: ", 0),
	LEGENDARY("tooltip.legendary", "Legendary", 0),

	MSG_AI("msg.ai", "Configure %s: Set NoAI to %s.", 2),
	MSG_SET_TARGET("msg.set_target", "Set %s and %s to fight", 2),
	MSG_TARGET_FAIL("msg.target_fail", "%s and %s cannot fight", 2),
	MSG_TARGET_RECORD("msg.target_record", "Recorded %s", 1),
	MSG_SET_TRAIT("msg.set_trait", "Set trait %1$s on entity %2$s to level %3$s", 3),
	MSG_SELECT_TRAIT("msg.select_trait", "Selected trait: %s", 1),
	MSG_ERR_MAX("msg.err_max", "Trait level already reached max value", 0),
	MSG_ERR_DISALLOW("msg.err_disallow", "Trait not applicable on this entity", 0),

	INFO_PLAYER_LEVEL("info.player_level", "Player difficulty level: %s", 1),
	INFO_PLAYER_EXP("info.player_exp", "Difficulty progress: %s/%s", 2),
	INFO_PLAYER_CAP("info.player_cap", "Mob trait rank limit: %s", 1),
	INFO_CHUNK_LEVEL("info.chunk_level", "Chunk base difficulty: %s", 1),
	INFO_CHUNK_SCALE("info.chunk_scale", "Chunk difficulty scale: %s", 1),
	TAB_TITLE("info.title", "Difficulty Information", 0),

	LOOT_TITLE("jei.loot_title", "Trait Loot", 0),
	CHANCE("jei.loot_chance", "%s%% chance to drop at Rank %s", 2);

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
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent translate(String key, Object... objs) {
		return Component.translatable(key, objs);
	}

}
