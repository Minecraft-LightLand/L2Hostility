package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.tag.HostilityTagUtil;
import dev.xkmc.l2hostility.editor.tag.TagEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class TagsHomeScreen extends HostilityHomeScreen {

	public TagsHomeScreen(Screen parent) {
		super(HostilityEditorLang.TAGS.get(), 4, parent);
	}

	@Override
	protected boolean canCreate() {
		return false;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return HostilityEditorUtil.listManagedTags();
	}

	@Override
	protected Component fileLabel(ResourceLocation id) {
		return switch (id.getPath()) {
			case "blacklist" -> HostilityEditorLang.TAG_NAME_BLACKLIST.get();
			case "whitelist" -> HostilityEditorLang.TAG_NAME_WHITELIST.get();
			case "default_blacklist" -> HostilityEditorLang.TAG_NAME_DEFAULT_BLACKLIST.get();
			case "default_whitelist" -> HostilityEditorLang.TAG_NAME_DEFAULT_WHITELIST.get();
			case "no_scaling" -> HostilityEditorLang.TAG_NAME_NO_SCALING.get();
			case "no_trait" -> HostilityEditorLang.TAG_NAME_NO_TRAIT.get();
			case "semiboss" -> HostilityEditorLang.TAG_NAME_SEMIBOSS.get();
			case "effect_immune" -> HostilityEditorLang.TAG_NAME_EFFECT_IMMUNE.get();
			case "no_drop" -> HostilityEditorLang.TAG_NAME_NO_DROP.get();
			case "hide_traits" -> HostilityEditorLang.TAG_NAME_HIDE_TRAITS.get();
			case "hide_level" -> HostilityEditorLang.TAG_NAME_HIDE_LEVEL.get();
			case "hide_title" -> HostilityEditorLang.TAG_NAME_HIDE_TITLE.get();
			case "armor_target" -> HostilityEditorLang.TAG_NAME_ARMOR_TARGET.get();
			case "melee_weapon_target" -> HostilityEditorLang.TAG_NAME_MELEE_WEAPON_TARGET.get();
			case "ranged_weapon_target" -> HostilityEditorLang.TAG_NAME_RANGED_WEAPON_TARGET.get();
			case "hostility_spawner_blacklist" -> HostilityEditorLang.TAG_NAME_SPAWNER_BLACKLIST.get();
			default -> super.fileLabel(id);
		};
	}

	@Override
	@Nullable
	protected Component fileTooltip(ResourceLocation id) {
		return switch (id.getPath()) {
			case "blacklist" -> HostilityEditorLang.TAG_INFO_BLACKLIST.get();
			case "whitelist" -> HostilityEditorLang.TAG_INFO_WHITELIST.get();
			case "default_blacklist" -> HostilityEditorLang.TAG_INFO_DEFAULT_BLACKLIST.get();
			case "default_whitelist" -> HostilityEditorLang.TAG_INFO_DEFAULT_WHITELIST.get();
			case "no_scaling" -> HostilityEditorLang.TAG_INFO_NO_SCALING.get();
			case "no_trait" -> HostilityEditorLang.TAG_INFO_NO_TRAIT.get();
			case "semiboss" -> HostilityEditorLang.TAG_INFO_SEMIBOSS.get();
			case "effect_immune" -> HostilityEditorLang.TAG_INFO_EFFECT_IMMUNE.get();
			case "no_drop" -> HostilityEditorLang.TAG_INFO_NO_DROP.get();
			case "hide_traits" -> HostilityEditorLang.TAG_INFO_HIDE_TRAITS.get();
			case "hide_level" -> HostilityEditorLang.TAG_INFO_HIDE_LEVEL.get();
			case "hide_title" -> HostilityEditorLang.TAG_INFO_HIDE_TITLE.get();
			case "armor_target" -> HostilityEditorLang.TAG_INFO_ARMOR_TARGET.get();
			case "melee_weapon_target" -> HostilityEditorLang.TAG_INFO_MELEE_WEAPON_TARGET.get();
			case "ranged_weapon_target" -> HostilityEditorLang.TAG_INFO_RANGED_WEAPON_TARGET.get();
			case "hostility_spawner_blacklist" -> HostilityEditorLang.TAG_INFO_SPAWNER_BLACKLIST.get();
			default -> null;
		};
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		return HostilityTagUtil.load(id).size();
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.TAG_EMPTY.get();
	}

	@Override
	protected String newFileDefault() {
		return "l2hostility:new";
	}

	@Override
	protected void openNew(ResourceLocation id) {
		EditorToast.show(EditorText.NEW.get(), EditorText.NO_FILE.get());
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new TagEditScreen(id, this));
	}

}