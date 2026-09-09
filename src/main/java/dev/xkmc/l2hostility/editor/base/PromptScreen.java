package dev.xkmc.l2hostility.editor.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class PromptScreen extends EditorScreen {

	private final Component label;
	@Nullable
	private final String initial;
	private final Function<String, Component> validate;
	private final Consumer<String> callback;
	private final Screen parent;

	private EditBox box;
	private Button confirmBtn;
	private Button resetBtn;
	@Nullable
	private Component error;

	public PromptScreen(Component title, Component label, @Nullable String initial,
	                    Function<String, Component> validate, Consumer<String> callback, Screen parent) {
		super(title);
		this.label = label;
		this.initial = initial;
		this.validate = validate;
		this.callback = callback;
		this.parent = parent;
	}

	@Override
	protected void init() {
		box = new EditBox(this.font, width / 2 - 100, height / 2 - 10, 200, 20, label);
		box.setValue(initial == null ? "" : initial);
		box.setMaxLength(256);
		box.setResponder(s -> {
			error = null;
			updateConfirmButton();
		});
		addRenderableWidget(box);
		int c = width / 2;
		int gap = 10;
		int w = Math.max(90, Math.max(font.width(EditorText.CANCEL.get()),
				Math.max(font.width(EditorText.RESET.get()), font.width(EditorText.CONFIRM.get()))) + 20);
		int x = c - (3 * w + 2 * gap) / 2;
		addRenderableWidget(Button.builder(EditorText.CANCEL.get(), b -> Minecraft.getInstance().setScreen(parent))
				.bounds(x, height / 2 + 18, w, 20).build());
		resetBtn = Button.builder(EditorText.RESET.get(), b -> resetValue())
				.bounds(x + w + gap, height / 2 + 18, w, 20).build();
		addRenderableWidget(resetBtn);
		confirmBtn = Button.builder(EditorText.CONFIRM.get(), b -> submit())
				.bounds(x + 2 * (w + gap), height / 2 + 18, w, 20).build();
		addRenderableWidget(confirmBtn);
		setInitialFocus(box);
		updateConfirmButton();
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(parent);
	}

	private boolean changed() {
		return initial == null ? !box.getValue().isEmpty() : !box.getValue().equals(initial);
	}

	private void updateConfirmButton() {
		boolean b = changed();
		confirmBtn.active = b;
		resetBtn.active = b;
	}

	private void resetValue() {
		box.setValue(initial == null ? "" : initial);
		error = null;
		updateConfirmButton();
	}

	private void submit() {
		Component err = validate.apply(box.getValue());
		if (err != null) {
			error = err;
		} else {
			callback.accept(box.getValue());
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 257 || keyCode == 335) {
			submit();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g, mx, my, pTick);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, height / 2 - 40, 0xFFFFFF);
		g.drawString(font, label, width / 2 - 220, height / 2 - 5, 0xAAAAAA);
		if (error != null) {
			g.drawCenteredString(font, error, width / 2, height / 2 + 42, 0xFF5555);
		}
	}

}
