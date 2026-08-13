package dev.xkmc.l2hostility.editor.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Grid editor for a directed relation map {@code Map<T, Map<T, Double>>}: rows and columns are
 * traits, the cell (row, col) holds the factor of "row relates to col" (0/absent = no relation).
 *
 * <p>Generic and mod-independent (base layer; {@code dev.xkmc.l2hostility.*} must not be used).
 * The editor mutates the passed {@code map} and {@code members} lists in place and flags the
 * shared {@link EditorSession} whenever a real value changes; adding a candidate is transient and
 * does not mark the session dirty.</p>
 */
public class ExclusionGridScreen<T> extends EditorScreen {

	public interface Handler<T> {

		Component label(T t);

		@Nullable
		ItemStack icon(T t);

		/**
		 * Title of the value prompt for a cell: "A relates B".
		 */
		default Component cellLabel(T a, T b) {
			return label(a).copy().append(" -> ").append(label(b));
		}

		/**
		 * Called when the user picks a trait to add. May mutate {@code map} (e.g. merge in a whole
		 * group) and must return the resulting member order.
		 */
		List<T> onAdd(T picked);

	}

	private static final int SIDE = 8;
	private static final int HEADER_SIZE = 28;
	private static final int GAP = 2;
	private static final int CELL = 22;
	private static final int TOP = 40;

	private final Component hint;
	private final Component clearHint;
	private final Map<T, Map<T, Double>> map;
	private final List<T> members;
	private final List<T> candidates;
	private final Handler<T> handler;
	private final Screen parent;
	private final EditorSession session;

	private Button removeBtn;
	private double scroll;
	@Nullable
	private T selected;

	public ExclusionGridScreen(Component title, Component hint, Component clearHint,
	                           Map<T, Map<T, Double>> map, List<T> members, List<T> candidates,
	                           Handler<T> handler, Screen parent, EditorSession session) {
		super(title);
		this.hint = hint;
		this.clearHint = clearHint;
		this.map = map;
		this.members = members;
		this.candidates = candidates;
		this.handler = handler;
		this.parent = parent;
		this.session = session;
	}

	@Override
	protected void init() {
		List<Button> row = new ArrayList<>();
		row.add(Button.builder(EditorText.ADD.get(), b -> addValue()).bounds(0, 0, 60, 20).build());
		removeBtn = Button.builder(EditorText.REMOVE.get(), b -> removeValue()).bounds(0, 0, 60, 20).build();
		row.add(removeBtn);
		row.add(Button.builder(EditorText.BACK.get(), b -> onClose()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		removeBtn.active = false;
	}

	private int colX() {
		return SIDE + HEADER_SIZE + GAP;
	}

	private int rowY() {
		return TOP + HEADER_SIZE + GAP;
	}

	private int gridBottom() {
		return height - 50;
	}

	private double maxScroll() {
		int content = members.size() * CELL;
		int visible = gridBottom() - rowY() - (members.isEmpty() ? 0 : CELL);
		return Math.max(0, content - Math.max(0, visible));
	}

	private int rowAt(double my) {
		int idx = (int) ((my - rowY() + scroll) / CELL);
		return idx < 0 || idx >= members.size() ? -1 : idx;
	}

	private int colAt(double mx) {
		int idx = (int) ((mx - colX()) / CELL);
		return idx < 0 || idx >= members.size() ? -1 : idx;
	}

	@Nullable
	private Double value(T a, T b) {
		Map<T, Double> sub = map.get(a);
		return sub == null ? null : sub.get(b);
	}

	private void addValue() {
		Minecraft.getInstance().setScreen(new PickListScreen<>(EditorText.PICK_TARGET.get(), candidates,
				new PickHandler(), this));
	}

	private void removeValue() {
		if (selected == null) return;
		map.remove(selected);
		for (T m : members) {
			Map<T, Double> sub = map.get(m);
			if (sub != null) sub.remove(selected);
		}
		members.remove(selected);
		selected = null;
		session.dirty = true;
	}

	private void editCell(T a, T b) {
		Double cur = value(a, b);
		Minecraft.getInstance().setScreen(new PromptScreen(handler.cellLabel(a, b), clearHint,
				cur == null ? "" : DoubleMapScreen.format(cur), s -> {
					String t = s.trim();
					if (t.isEmpty()) return null;
					try {
						Double.parseDouble(t);
						return null;
					} catch (NumberFormatException e) {
						return EditorText.INVALID_NUMBER.get(t);
					}
				}, s -> {
					Map<T, Double> sub = map.computeIfAbsent(a, k -> new LinkedHashMap<>());
					String t = s.trim();
					if (t.isEmpty()) {
						sub.remove(b);
					} else {
						double v = Double.parseDouble(t);
						if (v <= 0) {
							sub.remove(b);
						} else {
							sub.put(b, v);
						}
					}
					session.dirty = true;
					Minecraft.getInstance().setScreen(ExclusionGridScreen.this);
				}, this));
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0) {
			if (mx >= SIDE && mx < colX() && my >= rowY() && my <= gridBottom()) {
				int r = rowAt(my);
				if (r >= 0) {
					selected = members.get(r);
					removeBtn.active = true;
					return true;
				}
			}
			if (mx >= colX() && my >= TOP && my < rowY()) {
				int c = colAt(mx);
				if (c >= 0) {
					selected = members.get(c);
					removeBtn.active = true;
					return true;
				}
			}
			if (mx >= colX() && my >= rowY() && my <= gridBottom()) {
				int r = rowAt(my);
				int c = colAt(mx);
				if (r >= 0 && c >= 0) {
					if (r != c) {
						editCell(members.get(r), members.get(c));
					}
					return true;
				}
			}
		}
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double delta) {
		if (delta != 0 && (mx >= SIDE || my >= TOP)) {
			scroll -= delta * CELL;
			scroll = Math.max(0, Math.min(scroll, maxScroll()));
			return true;
		}
		return super.mouseScrolled(mx, my, delta);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, 4, 0xFFFFFF);
		g.drawCenteredString(font, hint, width / 2, 18, 0xAAAAAA);
		renderGrid(g);
		T hovered = hoveredHeader(mx, my);
		if (hovered != null) {
			g.renderComponentTooltip(font, List.of(handler.label(hovered)), mx, my);
		}
	}

	/**
	 * Tooltip for the hovered header trait, or null.
	 */
	@Nullable
	private T hoveredHeader(int mx, int my) {
		if (mx >= SIDE && mx < colX() && my >= rowY() && my <= gridBottom()) {
			int r = rowAt(my);
			return r >= 0 ? members.get(r) : null;
		}
		if (mx >= colX() && my >= TOP && my < rowY()) {
			int c = colAt(mx);
			return c >= 0 ? members.get(c) : null;
		}
		return null;
	}

	private void renderGrid(GuiGraphics g) {
		int cols = members.size();
		if (cols == 0) return;
		double scroll = this.scroll;
		int bottom = gridBottom();
		for (int r = 0; r < cols; r++) {
			int y0 = (int) (rowY() + r * CELL - scroll);
			if (y0 + CELL < TOP || y0 > bottom) continue;
			T row = members.get(r);
			g.fill(SIDE, y0, SIDE + HEADER_SIZE, y0 + CELL,
					row.equals(selected) ? 0x40FFFFFF : 0x20AAAAAA);
			ItemStack ic = handler.icon(row);
			if (ic != null && !ic.isEmpty()) {
				g.renderItem(ic, SIDE + (HEADER_SIZE - 16) / 2, y0 + (CELL - 16) / 2);
			}
			int x = colX();
			for (int c = 0; c < cols; c++) {
				int x0 = x + c * CELL;
				if (x0 + CELL > width - SIDE) break;
				if (y0 + CELL < TOP || y0 > bottom) continue;
				T col = members.get(c);
				boolean selectedCol = col.equals(this.selected);
				if (r == c) {
					g.fill(x0, y0, x0 + CELL, y0 + CELL, 0x10000000);
				} else {
					Double v = value(row, col);
					if (v == null) {
						g.fill(x0, y0, x0 + CELL, y0 + CELL, selectedCol ? 0x201D1D6A : 0x2D111111);
					} else {
						g.fill(x0, y0, x0 + CELL, y0 + CELL, selectedCol ? 0x4064A64C : 0x30333311);
						g.drawCenteredString(font, DoubleMapScreen.format(v), x0 + CELL / 2, y0 + 6, 0xFFFFFF);
					}
					g.fill(x0, y0, x0 + 1, y0 + CELL, 0xFF3C3C3C);
					g.fill(x0, y0 + CELL - 1, x0 + CELL, y0 + CELL, 0xFF3C3C3C);
				}
			}
			g.fill(SIDE, y0 + CELL - 1, SIDE + HEADER_SIZE, y0 + CELL, 0xFF3C3C3C);
		}
		for (int c = 0; c < cols; c++) {
			int x0 = colX() + c * CELL;
			if (x0 + CELL > width - SIDE) break;
			int yTop = TOP;
			int yMid = TOP + HEADER_SIZE;
			T col = members.get(c);
			g.fill(x0, yTop, x0 + CELL, yMid, col.equals(selected) ? 0x40FFFFFF : 0x20AAAAAA);
			ItemStack ic = handler.icon(col);
			if (ic != null && !ic.isEmpty()) {
				g.renderItem(ic, x0 + (CELL - 16) / 2, yTop + (HEADER_SIZE - 16) / 2);
			}
			g.fill(x0, yMid - 1, x0 + CELL, yMid, 0xFF3C3C3C);
		}
	}

	private class PickHandler implements PickListScreen.Handler<T> {

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
		public String searchKey(T t) {
			return handler.label(t).getString();
		}

		@Override
		public void onSelect(T t) {
			List<T> merged = handler.onAdd(t);
			members.clear();
			members.addAll(merged);
			Minecraft.getInstance().setScreen(ExclusionGridScreen.this);
		}

	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(parent);
	}

}