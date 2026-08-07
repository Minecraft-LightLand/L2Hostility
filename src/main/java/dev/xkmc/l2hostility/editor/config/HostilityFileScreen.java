package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.editor.base.EditorFile;
import dev.xkmc.l2hostility.editor.base.EditorLayout;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.EditorSaveState;
import dev.xkmc.l2hostility.editor.base.EditorScreen;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.ExitConfirmScreen;
import dev.xkmc.l2hostility.editor.base.PromptScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class HostilityFileScreen extends EditorScreen {

	protected final Screen parent;
	protected final EditorSession session = new EditorSession();
	protected ResourceLocation fileId;
	protected EditorList list;

	protected HostilityFileScreen(Component title, ResourceLocation fileId, Screen parent) {
		super(title);
		this.fileId = fileId;
		this.parent = parent;
	}

	@Override
	protected void init() {
		list = new EditorList(minecraft, width, height - 70, 30, height - 40);
		addRenderableWidget(list);
		List<Button> row = new ArrayList<>(extraButtons());
		Button saveBtn = Button.builder(EditorText.SAVE.get(), b -> save()).bounds(0, 0, 60, 20).build();
		saveBtn.active = session.dirty;
		row.add(saveBtn);
		row.add(Button.builder(EditorText.BACK.get(), b -> exitFile()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		onInitDone();
		rebuild();
	}

	/**
	 * Extra buttons placed before Save/Back on the bottom row.
	 */
	protected List<Button> extraButtons() {
		return new ArrayList<>();
	}

	protected void onInitDone() {
	}

	protected abstract void rebuild();

	protected abstract boolean doSave();

	protected void save() {
		Minecraft.getInstance().setScreen(new PromptScreen(EditorText.SAVE.get(), EditorText.FILE_ID.get(),
				fileId.toString(), HostilityEditorUtil::validateFileId, s -> {
					ResourceLocation id = EditorFile.parseId(s);
					if (id == null) return;
					fileId = id;
					if (doSave()) {
						Minecraft.getInstance().setScreen(HostilityFileScreen.this);
					}
				}, this));
	}

	protected void saveDone(ResourceLocation id) {
		EditorSaveState.savedFlag = true;
		session.dirty = false;
		EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_DONE.get(id));
		EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_NOTE.get());
	}

	protected void exitFile() {
		if (session.dirty) {
			Minecraft.getInstance().setScreen(new ExitConfirmScreen(this, () -> {
				if (doSave()) {
					Minecraft.getInstance().setScreen(parent);
				}
			}, () -> Minecraft.getInstance().setScreen(parent)));
		} else {
			Minecraft.getInstance().setScreen(parent);
		}
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, EditorText.FILE.get(fileId), width / 2, 10, 0xFFFFFF);
	}

	@Override
	public void onClose() {
		exitFile();
	}

}
