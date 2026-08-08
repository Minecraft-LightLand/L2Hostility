package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.editor.base.EditorLayout;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.EditorScreen;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ItemListScreen;
import dev.xkmc.l2hostility.editor.base.PickListScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Value page for an entry that is a set of picks plus scalar fields. The picked set is shown
 * and edited inline (Add opens the single-select picker, Remove deletes the selection); scalar
 * fields are edited inline. The picker returns to this screen on close.
 */
public class SetValueEditScreen<T, R> extends EditorScreen {

	private static final int ROW_H = 36;

	private final Screen parent;
	private final Consumer<R> onDone;
	private final Set<T> picked;
	private final List<T> candidates;
	private final ItemListScreen.Handler<T> handler;
	private final Component pickTitle;
	private final List<FormScreen.FormField> fields;
	private final BiFunction<List<T>, List<String>, R> build;

	private EditorList list;
	private final List<T> order = new ArrayList<>();
	private final List<EditBox> boxes = new ArrayList<>();
	private final List<Integer> boxToField = new ArrayList<>();
	private Button removeBtn;
	private final String[] values;
	@Nullable
	private Component error;

	public SetValueEditScreen(Component title, Screen parent, Consumer<R> onDone,
							  Set<T> picked, List<T> candidates, ItemListScreen.Handler<T> handler,
							  Component pickTitle,
							  List<FormScreen.FormField> fields, BiFunction<List<T>, List<String>, R> build) {
		super(title);
		this.parent = parent;
		this.onDone = onDone;
		this.picked = picked;
		this.candidates = candidates;
		this.handler = handler;
		this.pickTitle = pickTitle;
		this.fields = fields;
		this.build = build;
		this.values = new String[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			values[i] = fields.get(i).initial();
		}
	}

	@Override
	protected void init() {
		int top = 26 + fields.size() * ROW_H + 10;
		list = new EditorList(minecraft, width, height - 40 - top, top, height - 40);
		addRenderableWidget(list);
		for (int i = 0; i < fields.size(); i++) {
			FormScreen.FormField field = fields.get(i);
			EditBox box = new EditBox(this.font, width / 2 + 40, 26 + i * ROW_H + 14, 120, 20, field.label());
			box.setMaxLength(64);
			box.setValue(values[i]);
			int idx = i;
			box.setResponder(s -> {
				values[idx] = s;
				error = null;
			});
			boxes.add(box);
			boxToField.add(i);
			addRenderableWidget(box);
		}
		List<Button> row = new ArrayList<>();
		row.add(Button.builder(EditorText.ADD.get(), b -> addPick()).bounds(0, 0, 60, 20).build());
		removeBtn = Button.builder(EditorText.REMOVE.get(), b -> removePick()).bounds(0, 0, 60, 20).build();
		removeBtn.active = false;
		row.add(removeBtn);
		row.add(Button.builder(EditorText.CANCEL.get(), b -> Minecraft.getInstance().setScreen(parent)).bounds(0, 0, 60, 20).build());
		row.add(Button.builder(EditorText.CONFIRM.get(), b -> submit()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		list.setOnSelect(() -> removeBtn.active = selected() != null);
		rebuild();
	}

	private void rebuild() {
		order.clear();
		List<EditorList.Entry> entries = new ArrayList<>();
		List<T> keys = new ArrayList<>(picked);
		keys.sort((a, b) -> handler.label(a).getString().compareToIgnoreCase(handler.label(b).getString()));
		for (T k : keys) {
			order.add(k);
			entries.add(new EditorList.Entry(handler.label(k), handler.icon(k), null));
		}
		if (entries.isEmpty()) {
			entries.add(new EditorList.Entry(EditorText.EMPTY_FILE.get(), null, null));
		}
		list.setData(entries);
	}

	@Nullable
	private T selected() {
		EditorList.Entry sel = list.getSelected();
		if (sel == null) return null;
		int i = list.children().indexOf(sel);
		if (i < 0 || i >= order.size()) return null;
		return order.get(i);
	}

	private void addPick() {
		List<T> remaining = new ArrayList<>();
		for (T t : candidates) {
			if (!picked.contains(t)) remaining.add(t);
		}
		if (remaining.isEmpty()) {
			EditorToast.show(EditorText.ADD.get(), EditorText.NO_FILE.get());
			return;
		}
		Minecraft.getInstance().setScreen(new PickListScreen<>(pickTitle, remaining,
				new PickHandler<>(this, handler), this));
	}

	private void removePick() {
		T item = selected();
		if (item == null) return;
		picked.remove(item);
		rebuild();
	}

	private record PickHandler<T>(SetValueEditScreen<T, ?> screen, ItemListScreen.Handler<T> handler)
			implements PickListScreen.Handler<T> {

		@Override
		public Component label(T t) {
			return handler.label(t);
		}

		@Override
		@Nullable
		public ItemStack icon(T t) {
			return handler.icon(t);
		}

		@Override
		public void onSelect(T t) {
			screen.picked.add(t);
			Minecraft.getInstance().setScreen(screen);
		}

	}

	private void submit() {
		List<String> vals = new ArrayList<>();
		for (int i = 0; i < fields.size(); i++) {
			FormScreen.FormField field = fields.get(i);
			String val = boxes.get(boxToField.indexOf(i)).getValue().trim();
			if (field.validate() != null) {
				Component err = field.validate().apply(val);
				if (err != null) {
					error = err;
					return;
				}
			}
			vals.add(val);
		}
		onDone.accept(build.apply(new ArrayList<>(picked), vals));
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
		for (int i = 0; i < fields.size(); i++) {
			FormScreen.FormField field = fields.get(i);
			g.drawString(font, field.label(), width / 2 - 160, 26 + i * ROW_H + 19, 0xAAAAAA);
		}
		if (error != null) {
			g.drawCenteredString(font, error, width / 2, height - 52, 0xFF5555);
		}
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(parent);
	}

}
