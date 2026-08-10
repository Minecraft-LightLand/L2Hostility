package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.tag.HostilityTagUtil;
import dev.xkmc.l2hostility.editor.tag.TagEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TraitFileScreen extends HostilityFileScreen {

	private TraitConfig config;

	public TraitFileScreen(ResourceLocation id, Screen parent) {
		super(HostilityEditorLang.TRAIT_FILE.get(), id, parent);
		TraitConfig cur = HostilityEditorUtil.currentTraitConfig(id);
		this.config = cur == null ? HostilityEditorUtil.newTrait() : cur;
	}

	@Override
	protected void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorForms.entry(HostilityEditorLang.TRAIT_FIELDS.get(),
				HostilityEditorForms.traitFieldsSummary(config)), null,
				() -> Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.TRAIT_FIELDS.get(),
						HostilityEditorForms.traitConfig(config), c -> {
					config = c;
					session.dirty = true;
					rebuild();
					Minecraft.getInstance().setScreen(TraitFileScreen.this);
				}, TraitFileScreen.this)), HostilityEditorLang.ROW_TRAIT_FIELDS_TIP.get()));
		List<LHConfigEdit.FieldDef> configFields = LHConfigEdit.traitFields(fileId.getPath());
		if (!configFields.isEmpty()) {
			MutableComponent configLabel = HostilityEditorForms.counted(HostilityEditorLang.TRAIT_CONFIG.get(), configFields.size()).copy();
			LHConfigEdit.FieldDef toggle = LHConfigEdit.traitToggle(fileId.getPath());
			if (toggle != null) {
				Component status = toggle.getString().equals("true")
						? HostilityEditorLang.CONFIG_ENABLED.get()
						: HostilityEditorLang.CONFIG_DISABLED.get();
				configLabel.append(Component.literal("  ")).append(status);
			}
			entries.add(new EditorList.Entry(configLabel, null,
					() -> LHConfigEdit.INSTANCE.openSectionForm(HostilityEditorLang.TRAIT_CONFIG.get(), configFields, TraitFileScreen.this),
					HostilityEditorLang.ROW_TRAIT_CONFIG_TIP.get()));
		}
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.BLACKLIST_TAG.get(),
				HostilityTagUtil.load(config.getBlacklistTag(fileId).location()).size()), null,
				() -> Minecraft.getInstance().setScreen(new TagEditScreen(config.getBlacklistTag(fileId).location(), TraitFileScreen.this)),
				HostilityEditorLang.ROW_BLACKLIST_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.WHITELIST_TAG.get(),
				HostilityTagUtil.load(config.getWhitelistTag(fileId).location()).size()), null,
				() -> Minecraft.getInstance().setScreen(new TagEditScreen(config.getWhitelistTag(fileId).location(), TraitFileScreen.this)),
				HostilityEditorLang.ROW_WHITELIST_TIP.get()));
		list.setData(entries);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.render(g, mx, my, pTick);
		list.renderRowTooltip(g);
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
