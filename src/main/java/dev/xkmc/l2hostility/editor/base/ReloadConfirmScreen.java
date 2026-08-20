package dev.xkmc.l2hostility.editor.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

public class ReloadConfirmScreen extends EditorScreen {

	private final Screen parent;
	private final Runnable onNow;
	private final Runnable onLater;

	public ReloadConfirmScreen(Screen parent, Runnable onNow, Runnable onLater) {
		super(EditorText.RELOAD_TITLE.get());
		this.parent = parent;
		this.onNow = onNow;
		this.onLater = onLater;
	}

	@Override
	protected void init() {
		int c = width / 2;
		int gap = 10;
		int w = Math.max(120, Math.max(font.width(EditorText.RELOAD_NOW.get()),
				font.width(EditorText.LATER.get())) + 24);
		addRenderableWidget(Button.builder(EditorText.RELOAD_NOW.get(), b -> onNow.run())
				.bounds(c - w - gap / 2, height / 2 + 20, w, 20).build());
		addRenderableWidget(Button.builder(EditorText.LATER.get(), b -> onLater.run())
				.bounds(c + gap / 2, height / 2 + 20, w, 20).build());
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, height / 2 - 24, 0xFFFFFF);
		g.drawCenteredString(font, EditorText.RELOAD_NOTE.get(), width / 2, height / 2 - 6, 0xAAAAAA);
	}

	@Override
	public void onClose() {
		onLater.run();
	}

}
