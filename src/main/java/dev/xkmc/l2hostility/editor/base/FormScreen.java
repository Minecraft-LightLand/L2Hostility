package dev.xkmc.l2hostility.editor.base;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FormScreen<T> extends EditorScreen {

	public record FormSpec<T>(List<FormField> fields, Function<List<String>, T> build) {

	}

	public record FormField(Component label, String initial, @Nullable Function<String, Component> validate,
	                        boolean bool) {

		public static FormField text(Component label, String initial, @Nullable Function<String, Component> validate) {
			return new FormField(label, initial, validate, false);
		}

		public static FormField bool(Component label, boolean initial) {
			return new FormField(label, "" + initial, null, true);
		}

	}

	private static final int ROW_H = 26;
	private static final int BOX_W = 120;
	private static final int CONTENT_TOP = 24;

	private final FormSpec<T> spec;
	private final Consumer<T> onDone;
	private final Screen parent;

	private final boolean[] boolValues;
	private final List<EditBox> boxes = new ArrayList<>();
	private final List<Button> boolBtns = new ArrayList<>();
	private final List<Integer> boxToField = new ArrayList<>();
	private final List<Integer> boolToField = new ArrayList<>();
	private int scroll;
	@Nullable
	private Component error;

	public FormScreen(Component title, FormSpec<T> spec, Consumer<T> onDone, Screen parent) {
		super(title);
		this.spec = spec;
		this.onDone = onDone;
		this.parent = parent;
		this.boolValues = new boolean[spec.fields().stream().mapToInt(e -> e.bool() ? 1 : 0).sum()];
	}

	@Override
	protected void init() {
		int bi = 0;
		for (int i = 0; i < spec.fields().size(); i++) {
			FormField field = spec.fields().get(i);
			if (field.bool()) {
				int idx = bi++;
				boolToField.add(i);
				boolValues[idx] = field.initial().equals("true");
				Button btn = Button.builder(boolLabel(idx), b -> {
					boolValues[idx] = !boolValues[idx];
					b.setMessage(boolLabel(idx));
					error = null;
				}).bounds(boxX(), 0, BOX_W, 20).build();
				boolBtns.add(btn);
				addRenderableWidget(btn);
			} else {
				EditBox box = new EditBox(this.font, boxX(), 0, BOX_W, 20, field.label());
				box.setMaxLength(64);
				box.setValue(field.initial());
				box.setResponder(s -> error = null);
				boxes.add(box);
				boxToField.add(i);
				addRenderableWidget(box);
			}
		}
		addRenderableWidget(Button.builder(EditorText.CANCEL.get(), b -> Minecraft.getInstance().setScreen(parent))
				.bounds(width / 2 - 110, buttonY(), 100, 20).build());
		addRenderableWidget(Button.builder(EditorText.CONFIRM.get(), b -> submit())
				.bounds(width / 2 + 10, buttonY(), 100, 20).build());
		layout();
		if (!boxes.isEmpty()) setInitialFocus(boxes.get(0));
	}

	private int labelX() {
		return width / 2 - 160;
	}

	private int boxX() {
		return width / 2 + 40;
	}

	private int fieldY(int i) {
		return CONTENT_TOP + i * ROW_H - scroll;
	}

	private int buttonY() {
		return height - 30;
	}

	private int maxScroll() {
		return Math.max(0, spec.fields().size() * ROW_H - (buttonY() - 10 - CONTENT_TOP));
	}

	private void layout() {
		for (int i = 0; i < boxes.size(); i++) {
			boxes.get(i).setY(fieldY(boxToField.get(i)) + 2);
		}
		for (int i = 0; i < boolBtns.size(); i++) {
			boolBtns.get(i).setY(fieldY(boolToField.get(i)) + 2);
		}
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double delta) {
		int max = maxScroll();
		if (max <= 0) return false;
		scroll = Mth.clamp(scroll - (int) (delta * 20), 0, max);
		layout();
		return true;
	}

	private Component boolLabel(int idx) {
		boolean v = boolValues[idx];
		return Component.literal(Boolean.toString(v)).withStyle(v ? ChatFormatting.GREEN : ChatFormatting.RED);
	}

	private void submit() {
		List<String> values = new ArrayList<>();
		int bi = 0;
		for (int i = 0; i < spec.fields().size(); i++) {
			FormField field = spec.fields().get(i);
			if (field.bool()) {
				values.add("" + boolValues[bi++]);
			} else {
				String val = boxes.get(boxToField.indexOf(i)).getValue();
				if (field.validate() != null) {
					Component err = field.validate().apply(val);
					if (err != null) {
						error = err;
						return;
					}
				}
				values.add(val);
			}
		}
		onDone.accept(spec.build().apply(values));
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
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, 8, 0xFFFFFF);
		for (int i = 0; i < spec.fields().size(); i++) {
			FormField field = spec.fields().get(i);
			g.drawString(font, field.label(), labelX(), fieldY(i) + 5, 0xAAAAAA);
		}
		if (error != null) {
			g.drawCenteredString(font, error, width / 2, buttonY() - 12, 0xFF5555);
		}
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(parent);
	}

}
