package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorTip;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.config.LHConfigEdit;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConfigHomeScreen extends HostilityHomeScreen {

	private static final String CLIENT = "client";
	private static final String COMMON = "common";

	private static final List<String> CLIENT_KEYS = List.of("editor", "overhead", "glasses", "misc");
	private static final List<String> COMMON_KEYS = List.of("datapack", "scaling", "difficulty",
			"orb_and_spawner", "items", "performance");

	private static final List<ResourceLocation> CONFIG_IDS = new ArrayList<>();

	static {
		for (String key : CLIENT_KEYS) CONFIG_IDS.add(ResourceLocation.fromNamespaceAndPath(CLIENT, key));
		for (String key : COMMON_KEYS) CONFIG_IDS.add(ResourceLocation.fromNamespaceAndPath(COMMON, key));
	}

	public ConfigHomeScreen(Screen parent) {
		super(HostilityEditorLang.CONFIG.get(), 5, parent);
	}

	@Nullable
	private static LHConfigEdit.Section configSection(ResourceLocation id) {
		List<String> keys = id.getNamespace().equals(CLIENT) ? CLIENT_KEYS : COMMON_KEYS;
		List<LHConfigEdit.Section> sections = id.getNamespace().equals(CLIENT)
				? LHConfigEdit.clientSections() : LHConfigEdit.generalSections();
		int idx = keys.indexOf(id.getPath());
		return idx >= 0 && idx < sections.size() ? sections.get(idx) : null;
	}

	@Override
	protected boolean canCreate() {
		return false;
	}

	@Override
	protected boolean hasNew() {
		return false;
	}

	@Override
	protected boolean hasReload() {
		return false;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return new ArrayList<>(CONFIG_IDS);
	}

	@Override
	protected String groupName(String ns) {
		if (ns.equals(CLIENT)) return I18n.get(HostilityEditorLang.CLIENT.key());
		if (ns.equals(COMMON)) return I18n.get(HostilityEditorLang.COMMON.key());
		return super.groupName(ns);
	}

	@Override
	protected Component fileLabel(ResourceLocation id) {
		LHConfigEdit.Section section = configSection(id);
		return section != null ? section.title() : super.fileLabel(id);
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		LHConfigEdit.Section section = configSection(id);
		return section == null ? 0 : section.fields().size();
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.CONFIG_EMPTY.get();
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
		LHConfigEdit.Section section = configSection(id);
		if (section != null) {
			LHConfigEdit.INSTANCE.openSectionForm(section.title(), section.fields(), this);
		}
	}

	@Override
	protected List<Button> extraButtons() {
		Button reset = EditorTip.tip(Button.builder(EditorText.RESET.get(), b -> {
			LHConfigEdit.INSTANCE.resetToDefault();
			EditorToast.show(EditorText.RESET.get(), EditorText.RESET_DONE.get());
			rebuildWidgets();
		}).bounds(0, 0, 60, 20).build(), HostilityEditorLang.RESET_TIP.get());
		return List.of(reset);
	}

}
