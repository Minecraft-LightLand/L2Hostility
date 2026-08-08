package dev.xkmc.l2hostility.editor.tag;

import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TagEditScreen extends EditorScreen {

	private final ResourceLocation tagId;
	private final Screen parent;
	private final EditorSession session = new EditorSession();
	private final List<TagValue> values = new ArrayList<>();

	private EditorList list;
	private final List<Integer> order = new ArrayList<>();
	private Button addEntBtn;
	private Button addTagBtn;
	private Button editBtn;
	private Button removeBtn;
	private Button saveBtn;

	public TagEditScreen(ResourceLocation tagId, Screen parent) {
		super(HostilityEditorLang.TAG_FILE.get());
		this.tagId = tagId;
		this.parent = parent;
		values.addAll(HostilityTagUtil.load(tagId));
	}

	@Override
	protected void init() {
		list = new EditorList(minecraft, width, height - 70, 30, height - 40);
		addRenderableWidget(list);
		List<Button> row = new ArrayList<>();
		addEntBtn = Button.builder(HostilityEditorLang.ADD_ENTITY.get(), b -> addEntity()).bounds(0, 0, 50, 20).build();
		row.add(addEntBtn);
		addTagBtn = Button.builder(HostilityEditorLang.ADD_TAG.get(), b -> addTag()).bounds(0, 0, 50, 20).build();
		row.add(addTagBtn);
		editBtn = Button.builder(EditorText.EDIT.get(), b -> toggleRequired()).bounds(0, 0, 50, 20).build();
		row.add(editBtn);
		removeBtn = Button.builder(EditorText.REMOVE.get(), b -> removeValue()).bounds(0, 0, 50, 20).build();
		row.add(removeBtn);
		saveBtn = Button.builder(EditorText.SAVE.get(), b -> save()).bounds(0, 0, 50, 20).build();
		row.add(saveBtn);
		row.add(Button.builder(EditorText.BACK.get(), b -> exit()).bounds(0, 0, 50, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		editBtn.active = false;
		removeBtn.active = false;
		saveBtn.active = session.dirty;
		list.setOnSelect(() -> {
			editBtn.active = selected() != null;
			removeBtn.active = selected() != null;
		});
		list.setOnDoubleClick(this::toggleRequired);
		rebuild();
	}

	private void rebuild() {
		order.clear();
		List<EditorList.Entry> entries = new ArrayList<>();
		for (int i = 0; i < values.size(); i++) {
			order.add(i);
			entries.add(new EditorList.Entry(values.get(i).toComponent(), null, null));
		}
		if (entries.isEmpty()) {
			entries.add(new EditorList.Entry(EditorText.EMPTY_FILE.get(), null, null));
		}
		list.setData(entries);
	}

	@Nullable
	private TagValue selected() {
		EditorList.Entry sel = list.getSelected();
		if (sel == null) return null;
		int i = list.children().indexOf(sel);
		if (i < 0 || i >= order.size()) return null;
		return values.get(order.get(i));
	}

	private List<EntityType<?>> remainingEntities() {
		List<EntityType<?>> ans = new ArrayList<>();
		for (EntityType<?> type : HostilityEditorUtil.listEntityTypes()) {
			ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(type);
			if (rl == null) continue;
			boolean has = values.stream().anyMatch(v -> !v.isTag() && v.id().equals(rl.toString()));
			if (!has) ans.add(type);
		}
		return ans;
	}

	private void addEntity() {
		List<EntityType<?>> remaining = remainingEntities();
		if (remaining.isEmpty()) {
			EditorToast.show(EditorText.ADD.get(), EditorText.NO_FILE.get());
			return;
		}
		Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_ENTITY.get(), remaining,
				EditorHandler.Pick.of(HostilityEditorHandlers.ENTITY_TYPE, t -> {
					ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(t);
					if (rl == null) return;
					values.add(new TagValue(rl.toString(), true, false));
					session.dirty = true;
					rebuild();
					Minecraft.getInstance().setScreen(TagEditScreen.this);
				}), this));
	}

	private void addTag() {
		Minecraft.getInstance().setScreen(new PromptScreen(HostilityEditorLang.ADD_TAG.get(),
				EditorText.PICK_TAG.get(), "#l2hostility:", this::validateTag, s -> {
			ResourceLocation rl = EditorFile.parseId(s.substring(1));
			if (rl == null) return;
			values.add(new TagValue(rl.toString(), true, true));
			session.dirty = true;
			rebuild();
			Minecraft.getInstance().setScreen(TagEditScreen.this);
		}, this));
	}

	@Nullable
	private Component validateTag(String s) {
		if (!s.startsWith("#")) return HostilityEditorLang.TAG_HASH_HINT.get();
		ResourceLocation rl = EditorFile.parseId(s.substring(1));
		return rl == null ? EditorText.INVALID_ID.get(s) : null;
	}

	private void toggleRequired() {
		int i = list.children().indexOf(list.getSelected());
		if (i < 0 || i >= order.size()) return;
		int idx = order.get(i);
		TagValue v = values.get(idx);
		values.set(idx, new TagValue(v.id(), !v.required(), v.isTag()));
		session.dirty = true;
		rebuild();
	}

	private void removeValue() {
		int i = list.children().indexOf(list.getSelected());
		if (i < 0 || i >= order.size()) return;
		values.remove((int) order.get(i));
		session.dirty = true;
		rebuild();
	}

	private void save() {
		try {
			HostilityTagUtil.save(tagId, values);
			EditorSaveState.savedFlag = true;
			session.dirty = false;
			EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_DONE.get(tagId));
			EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_NOTE.get());
			saveBtn.active = false;
		} catch (Exception e) {
			EditorToast.show(EditorText.SAVE_FAIL.get(e.getMessage()), EditorText.NOT_IN_WORLD.get());
		}
	}

	private void exit() {
		if (session.dirty) {
			Minecraft.getInstance().setScreen(new ExitConfirmScreen(this, () -> {
				save();
				if (!session.dirty) Minecraft.getInstance().setScreen(parent);
			}, () -> Minecraft.getInstance().setScreen(parent)));
		} else {
			Minecraft.getInstance().setScreen(parent);
		}
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, EditorText.FILE.get(tagId), width / 2, 10, 0xFFFFFF);
		g.drawCenteredString(font, HostilityEditorLang.TAG_NOTE.get(), width / 2, 20, 0xAAAAAA);
	}

	@Override
	public void onClose() {
		exit();
	}

}
