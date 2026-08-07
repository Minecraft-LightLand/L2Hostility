package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class EntityFileScreen extends HostilityFileScreen {

	private final EntityConfig config;

	public EntityFileScreen(ResourceLocation id, Screen parent) {
		super(HostilityEditorLang.ENTITY_FILE.get(), id, parent);
		EntityConfig base = L2Hostility.ENTITY.getEntry(id);
		EntityConfig copy = base == null ? null : dev.xkmc.l2hostility.editor.base.EditorUtil.copy(L2Hostility.ENTITY, base);
		this.config = copy == null ? HostilityEditorUtil.newEntity() : copy;
	}

	@Override
	protected void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorLang.CONFIG_LIST.get(), null,
				() -> Minecraft.getInstance().setScreen(new ConfigListScreen(
						HostilityEditorLang.CONFIG_LIST.get(), config.list, true, EntityFileScreen.this, session))));
		list.setData(entries);
	}

	@Override
	protected boolean doSave() {
		try {
			HostilityEditorUtil.saveEntity(fileId, config);
			saveDone(fileId);
			return true;
		} catch (Exception e) {
			EditorToast.show(EditorText.SAVE_FAIL.get(e.getMessage()), EditorText.NOT_IN_WORLD.get());
			return false;
		}
	}

}
