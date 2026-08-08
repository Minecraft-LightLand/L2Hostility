package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.config.LHConfigEdit;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConfigHomeScreen extends HostilityHomeScreen {

	public ConfigHomeScreen(Screen parent) {
		super(HostilityEditorLang.CONFIG.get(), 5, parent);
	}

	private static final List<ResourceLocation> CONFIG_IDS = List.of(
			new ResourceLocation("l2hostility", "datapack"),
			new ResourceLocation("l2hostility", "scaling"),
			new ResourceLocation("l2hostility", "difficulty"),
			new ResourceLocation("l2hostility", "orb_and_spawner"),
			new ResourceLocation("l2hostility", "items"),
			new ResourceLocation("l2hostility", "performance"));

	@Nullable
	private static LHConfigEdit.Section configSection(ResourceLocation id) {
		int idx = CONFIG_IDS.indexOf(id);
		if (idx < 0) return null;
		List<LHConfigEdit.Section> sections = LHConfigEdit.generalSections();
		return idx < sections.size() ? sections.get(idx) : null;
	}

	@Override
	protected boolean canCreate() {
		return false;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return new ArrayList<>(CONFIG_IDS);
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
			LHConfigEdit.openSectionForm(section.title(), section.fields(), this);
		}
	}

}