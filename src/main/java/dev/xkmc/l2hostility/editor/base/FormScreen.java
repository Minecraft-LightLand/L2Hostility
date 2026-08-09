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
	                        boolean bool, @Nullable List<Component> tooltip) {

		public static FormField text(Component label, String initial, @Nullable Function<String, Component> validate) {
			return new FormField(label, initial, validate, false, null);
		}

		public static FormField text(Component label, String initial, @Nullable Function<String, Component> validate,
		                             Component... tooltip) {
			return new FormField(label, initial, validate, false, tooltip.length == 0 ? null : List.of(tooltip));
		}

		public static FormField bool(Component label, boolean initial) {
			return new FormField(label, "" + initial, null, true, null);
		}

		public static FormField bool(Component label, boolean initial, Component... tooltip) {
			return new FormField(label, "" + initial, null, true, tooltip.length == 0 ? null : List.of(tooltip));
		}

	}

	private static final int ROW_H = 26;
	private static final int BOX_W = 120;
	private static final int CONTENT_TOP = 24;

	private final FormSpec<T> spec;
	private final Consumer<T> onDone;
	private final Screen parent;
	private final boolean saveOnClose;

	private final boolean[] boolValues;
	private final List<EditBox> boxes = new ArrayList<>();
	private final List<Button> boolBtns = new ArrayList<>();
	private final List<Integer> boxToField = new ArrayList<>();
	private final List<Integer> boolToField = new ArrayList<>();
	private int scroll;
	@Nullable
	private Component error;

	public FormScreen(Component title, FormSpec<T> spec, Consumer<T> onDone, Screen parent) {
		this(title, spec, onDone, parent, false);
	}

	public FormScreen(Component title, FormSpec<T> spec, Consumer<T> onDone, Screen parent, boolean saveOnClose) {
		super(title);
		this.spec = spec;
		this.onDone = onDone;
		this.parent = parent;
		this.saveOnClose = saveOnClose;
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

	/**
	 * Max width of a field label before it would overlap the edit box. Translated labels can be
	 * longer than the English option names.
	 */
	private int maxLabelWidth() {
		return boxX() - labelX() - 6;
	}

	/**
	 * The label to draw: truncated with an ellipsis when it does not fit next to the edit box.
	 */
	private Component fitLabel(FormField field) {
		Component label = field.label();
		if (font.width(label) <= maxLabelWidth()) return label;
		String cut = font.plainSubstrByWidth(label.getString(), Math.max(0, maxLabelWidth() - 3));
		return Component.literal(cut.isEmpty() ? "..." : cut + "...").withStyle(label.getStyle());
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
		int top = CONTENT_TOP;
		int bottom = buttonY() - 10;
		for (int i = 0; i < boxes.size(); i++) {
			EditBox box = boxes.get(i);
			box.setY(fieldY(boxToField.get(i)) + 2);
			box.setVisible(inBand(boxToField.get(i), top, bottom) || box.isFocused());
		}
		for (int i = 0; i < boolBtns.size(); i++) {
			boolBtns.get(i).setY(fieldY(boolToField.get(i)) + 2);
			boolBtns.get(i).visible = inBand(boolToField.get(i), top, bottom);
		}
	}

	/**
	 * Whether the row of the given field is (partially) inside the content band, i.e. not scrolled
	 * far enough to overlap the title or the bottom buttons.
	 */
	private boolean inBand(int fieldIdx, int top, int bottom) {
		int y = fieldY(fieldIdx);
		return y + ROW_H >= top && y <= bottom;
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
		int top = CONTENT_TOP;
		int bottom = buttonY() - 10;
		for (int i = 0; i < spec.fields().size(); i++) {
			FormField field = spec.fields().get(i);
			int y = fieldY(i);
			if (y + ROW_H < top || y > bottom) continue;
			g.drawString(font, fitLabel(field), labelX(), y + 5, 0xAAAAAA);
		}
		List<Component> tip = hoveredTip(mx, my);
		if (tip != null && !tip.isEmpty()) {
			g.renderComponentTooltip(font, tip, mx, my);
		}
		if (error != null) {
			g.drawCenteredString(font, error, width / 2, buttonY() - 12, 0xFF5555);
		}
	}

	/**
	 * Tooltip of the field row under the mouse, if any. Only the label text (not the editbox/bool
	 * button) triggers the tooltip.
	 */
	@Nullable
	private List<Component> hoveredTip(int mx, int my) {
		int top = CONTENT_TOP;
		int bottom = buttonY() - 10;
		for (int i = 0; i < spec.fields().size(); i++) {
			FormField field = spec.fields().get(i);
			if (field.tooltip() == null) continue;
			int y = fieldY(i);
			if (y + ROW_H < top || y > bottom) continue;
			int right = labelX() + font.width(fitLabel(field));
			if (my >= y && my < y + ROW_H && mx >= labelX() && mx <= right) {
				return field.tooltip();
			}
		}
		return null;
	}

	/**
	 * Whether any field value differs from its initial value.
	 */
	private boolean changed() {
		int bi = 0;
		for (int i = 0; i < spec.fields().size(); i++) {
			FormField field = spec.fields().get(i);
			if (field.bool()) {
				if (boolValues[bi++] != Boolean.parseBoolean(field.initial())) return true;
			} else if (!boxes.get(boxToField.indexOf(i)).getValue().equals(field.initial())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClose() {
		if (saveOnClose && changed()) {
			submit();
		} else {
			Minecraft.getInstance().setScreen(parent);
		}
	}

}
