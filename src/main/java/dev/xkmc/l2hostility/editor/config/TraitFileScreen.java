package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.tag.HostilityTagUtil;
import dev.xkmc.l2hostility.editor.tag.TagEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TraitFileScreen extends HostilityFileScreen {

	private final TraitConfig config;

	public TraitFileScreen(ResourceLocation id, Screen parent) {
		super(HostilityEditorLang.TRAIT_FILE.get(), id, parent);
		TraitConfig base = L2Hostility.TRAIT.getEntry(id);
		TraitConfig copy = base == null ? null : EditorUtil.copy(L2Hostility.TRAIT, base);
		if (copy != null) copy.setId(id);
		this.config = copy == null ? new TraitConfig(id, 10, 100, 1, 10) : copy;
	}

	@Override
	protected void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorForms.entry(HostilityEditorLang.TRAIT_FIELDS.get(),
				HostilityEditorForms.traitFieldsSummary(config)), null,
				() -> Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.TRAIT_FIELDS.get(),
						HostilityEditorForms.traitConfig(config), c -> {
					config.min_level = c.min_level;
					config.cost = c.cost;
					config.max_rank = c.max_rank;
					config.weight = c.weight;
					session.dirty = true;
				}, TraitFileScreen.this))));
		LHConfigEdit.FieldDef toggle = LHConfigEdit.traitToggle(fileId.getPath());
		if (toggle != null) {
			Component status = toggle.getString().equals("true")
					? HostilityEditorLang.CONFIG_ENABLED.get()
					: HostilityEditorLang.CONFIG_DISABLED.get();
			entries.add(new EditorList.Entry(HostilityEditorForms.entry(HostilityEditorLang.TRAIT_TOGGLE.get(), status), null,
					() -> LHConfigEdit.openSectionForm(HostilityEditorLang.TRAIT_TOGGLE.get(), List.of(toggle), TraitFileScreen.this)));
		}
		List<LHConfigEdit.FieldDef> configFields = LHConfigEdit.traitConfigFields(fileId.getPath());
		if (!configFields.isEmpty()) {
			entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.TRAIT_CONFIG.get(), configFields.size()), null,
					() -> LHConfigEdit.openSectionForm(HostilityEditorLang.TRAIT_CONFIG.get(), configFields, TraitFileScreen.this)));
		}
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.BLACKLIST_TAG.get(),
				HostilityTagUtil.load(config.getBlacklistTag().location()).size()), null,
				() -> Minecraft.getInstance().setScreen(new TagEditScreen(config.getBlacklistTag().location(), TraitFileScreen.this))));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.WHITELIST_TAG.get(),
				HostilityTagUtil.load(config.getWhitelistTag().location()).size()), null,
				() -> Minecraft.getInstance().setScreen(new TagEditScreen(config.getWhitelistTag().location(), TraitFileScreen.this))));
		list.setData(entries);
	}

	@Override
	protected boolean doSave() {
		try {
			HostilityEditorUtil.saveTrait(fileId, config);
			saveDone(fileId);
			return true;
		} catch (Exception e) {
			EditorToast.show(EditorText.SAVE_FAIL.get(e.getMessage()), EditorText.NOT_IN_WORLD.get());
			return false;
		}
	}

}
