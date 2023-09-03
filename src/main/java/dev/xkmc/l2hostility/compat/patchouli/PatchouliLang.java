package dev.xkmc.l2hostility.compat.patchouli;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2hostility.init.L2Hostility;

public enum PatchouliLang {
	TITLE("title", "L2Hostility Guide"),
	LANDING("landing", "Find out the mechanics and mob traits to know what to prepare for");

	private final String key, def;

	PatchouliLang(String key, String def) {
		this.key = "patchouli." + L2Hostility.MODID + "." + key;
		this.def = def;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (PatchouliLang lang : PatchouliLang.values()) {
			pvd.add(lang.key, lang.def);
		}
	}

}
