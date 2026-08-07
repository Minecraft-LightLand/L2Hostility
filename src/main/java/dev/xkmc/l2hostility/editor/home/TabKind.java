package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import net.minecraft.network.chat.Component;

public enum TabKind {
	DIFFICULTY, TRAIT, WEAPON, ENTITY, TAGS;

	public Component title() {
		return switch (this) {
			case DIFFICULTY -> HostilityEditorLang.DIFFICULTY.get();
			case TRAIT -> HostilityEditorLang.TRAIT.get();
			case WEAPON -> HostilityEditorLang.WEAPON.get();
			case ENTITY -> HostilityEditorLang.ENTITY.get();
			case TAGS -> HostilityEditorLang.TAGS.get();
		};
	}

}
