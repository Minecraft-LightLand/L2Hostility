package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityFileScreen extends HostilityFileScreen {

	private final EntityConfig config;
	private final List<EntityConfig.Config> configs;
	private Button addBtn;
	private Button editBtn;
	private Button removeBtn;

	public EntityFileScreen(ResourceLocation id, Screen parent) {
		super(HostilityEditorLang.ENTITY_FILE.get(), id, parent);
		EntityConfig base = L2Hostility.ENTITY.getEntry(id);
		EntityConfig copy = base == null ? null : EditorUtil.copy(L2Hostility.ENTITY, base);
		this.config = copy == null ? HostilityEditorUtil.newEntity() : copy;
		this.configs = config.list;
	}

	@Override
	protected List<Button> extraButtons() {
		addBtn = EditorTip.tip(Button.builder(EditorText.ADD.get(), b -> addConfig()).bounds(0, 0, 60, 20).build(),
				HostilityEditorLang.ADD_ENTITY_CONFIG_TIP.get());
		editBtn = EditorTip.tip(Button.builder(EditorText.EDIT.get(), b -> editConfig()).bounds(0, 0, 60, 20).build(),
				HostilityEditorLang.EDIT_ENTITY_CONFIG_TIP.get());
		removeBtn = EditorTip.tip(Button.builder(EditorText.REMOVE.get(), b -> removeConfig()).bounds(0, 0, 60, 20).build(),
				HostilityEditorLang.REMOVE_ENTITY_CONFIG_TIP.get());
		editBtn.active = false;
		removeBtn.active = false;
		list.setOnSelect(() -> {
			editBtn.active = selected() != null;
			removeBtn.active = selected() != null;
		});
		list.setOnDoubleClick(this::editConfig);
		return List.of(addBtn, editBtn, removeBtn);
	}

	@Override
	protected void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		if (configs.isEmpty()) {
			entries.add(new EditorList.Entry(EditorText.EMPTY_FILE.get(), null, null));
		}
		for (EntityConfig.Config c : configs) {
			entries.add(new EditorList.Entry(HostilityEditorForms.configSummary(c), null, null));
		}
		list.setData(entries);
	}

	@Nullable
	private EntityConfig.Config selected() {
		EditorList.Entry sel = list.getSelected();
		if (sel == null) return null;
		int i = list.children().indexOf(sel);
		if (i < 0 || i >= configs.size()) return null;
		return configs.get(i);
	}

	private void addConfig() {
		Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_ENTITY.get(),
				HostilityEditorUtil.listEntityTypes(), EditorHandler.Pick.of(HostilityEditorHandlers.ENTITY_TYPE, t -> {
			EntityConfig.Config c = new EntityConfig.Config();
			c.entities = HolderSet.direct(EntityType::builtInRegistryHolder, List.of(t));
			openEntryEditor(c, configs::add);
		}), this));
	}

	private void editConfig() {
		EntityConfig.Config cur = selected();
		if (cur == null) return;
		int idx = configs.indexOf(cur);
		openEntryEditor(cur, c -> configs.set(idx, c));
	}

	private void openEntryEditor(EntityConfig.Config cur, Consumer<EntityConfig.Config> onDone) {
		Minecraft.getInstance().setScreen(new EntityConfigEntryScreen(HostilityEditorLang.ENTITY_CONFIG.get(), cur, c -> {
			onDone.accept(c);
			session.dirty = true;
			rebuild();
			Minecraft.getInstance().setScreen(EntityFileScreen.this);
		}, this));
	}

	private void removeConfig() {
		EntityConfig.Config cur = selected();
		if (cur == null) return;
		configs.remove(cur);
		session.dirty = true;
		rebuild();
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
