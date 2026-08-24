package dev.xkmc.l2hostility.editor.base;

import com.mojang.blaze3d.vertex.PoseStack;
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
 * Cells display the relation symmetrically: when both directions carry values their product is
 * shown. The grid scrolls vertically (mouse wheel) and horizontally (shift + mouse wheel), with
 * scroll bars for both axes.
 *
 * <p>Generic and mod-independent (base layer; {@code dev.xkmc.l2hostility.*} must not be used).
 * The editor mutates the passed {@code map} and {@code members} lists in place and flags the
 * shared {@link EditorSession} whenever a real value changes; adding a candidate is transient and
 * does not mark the session dirty. When a {@link Saver} is supplied, a Save button and an
 * unsaved-changes confirm on exit are added.</p>
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

	/**
	 * Optional save hook; when present, the screen shows a Save button and confirms unsaved
	 * changes on exit. Returns whether saving succeeded.
	 */
	public interface Saver {

		boolean save();

	}

	private static final int SIDE = 8;
	private static final int HEADER_SIZE = 28;
	private static final int GAP = 2;
	private static final int CELL = 22;
	private static final int TOP = 40;
	private static final int BAR = 6;
	private static final int BAR_GAP = 3;
	private static final int MIN_THUMB = 20;

	private final Component hint;
	private final Component clearHint;
	@Nullable
	private final Component subtitle;
	private final Map<T, Map<T, Double>> map;
	private final List<T> members;
	private final List<T> candidates;
	private final Handler<T> handler;
	@Nullable
	private final Saver saver;
	private final Screen parent;
	private final EditorSession session;

	private Button removeBtn;
	private double vScroll;
	private double hScroll;
	private int drag = -1;
	private double dragOffset;
	@Nullable
	private T selected;

	public ExclusionGridScreen(Component title, @Nullable Component subtitle, Component hint, Component clearHint,
	                           Map<T, Map<T, Double>> map, List<T> members, List<T> candidates,
	                           Handler<T> handler, @Nullable Saver saver, Screen parent, EditorSession session) {
		super(title);
		this.subtitle = subtitle;
		this.hint = hint;
		this.clearHint = clearHint;
		this.map = map;
		this.members = members;
		this.candidates = candidates;
		this.handler = handler;
		this.saver = saver;
		this.parent = parent;
		this.session = session;
	}

	@Override
	protected void init() {
		List<Button> row = new ArrayList<>();
		row.add(Button.builder(EditorText.ADD.get(), b -> addValue()).bounds(0, 0, 60, 20).build());
		removeBtn = Button.builder(EditorText.REMOVE.get(), b -> removeValue()).bounds(0, 0, 60, 20).build();
		row.add(removeBtn);
		if (saver != null) {
			Button saveBtn = Button.builder(EditorText.SAVE.get(), b -> saver.save()).bounds(0, 0, 60, 20).build();
			saveBtn.active = session.dirty;
			row.add(saveBtn);
		}
		row.add(Button.builder(EditorText.BACK.get(), b -> exit()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		removeBtn.active = false;
	}

	protected void exit() {
		if (saver != null && session.dirty) {
			Minecraft.getInstance().setScreen(new ExitConfirmScreen(this, () -> {
				if (saver.save()) {
					session.dirty = false;
					Minecraft.getInstance().setScreen(parent);
				}
			}, () -> Minecraft.getInstance().setScreen(parent)));
		} else {
			Minecraft.getInstance().setScreen(parent);
		}
	}

	private int colX() {
		return SIDE + HEADER_SIZE + GAP;
	}

	private int rowY() {
		return TOP + HEADER_SIZE + GAP;
	}

	private int gridRight() {
		return width - SIDE - BAR - BAR_GAP * 2;
	}

	private int gridBottom() {
		return height - 50;
	}

	private int contentSize() {
		return members.size() * CELL;
	}

	private double maxV() {
		return Math.max(0, contentSize() - Math.max(1, gridBottom() - rowY()));
	}

	private double maxH() {
		return Math.max(0, contentSize() - Math.max(1, gridRight() - colX()));
	}

	private void clamp() {
		vScroll = Math.max(0, Math.min(vScroll, maxV()));
		hScroll = Math.max(0, Math.min(hScroll, maxH()));
	}

	// vertical scroll bar geometry

	private int vBarX0() {
		return width - SIDE - BAR;
	}

	private int vThumbLen() {
		int track = gridBottom() - rowY();
		int total = contentSize();
		if (total <= track) return track;
		return Math.min(track, Math.max(MIN_THUMB, (int) ((long) track * track / total)));
	}

	private int vThumbPos() {
		int travel = gridBottom() - rowY() - vThumbLen();
		double max = maxV();
		if (travel <= 0 || max <= 0) return rowY();
		return (int) (rowY() + travel * (vScroll / max));
	}

	// horizontal scroll bar geometry

	private int hBarY0() {
		return gridBottom() + BAR_GAP;
	}

	private int hThumbLen() {
		int track = gridRight() - colX();
		int total = contentSize();
		if (total <= track) return track;
		return Math.min(track, Math.max(MIN_THUMB, (int) ((long) track * track / total)));
	}

	private int hThumbPos() {
		int travel = gridRight() - colX() - hThumbLen();
		double max = maxH();
		if (travel <= 0 || max <= 0) return colX();
		return (int) (colX() + travel * (hScroll / max));
	}

	private int rowAt(double my) {
		int idx = (int) ((my - rowY() + vScroll) / CELL);
		return idx < 0 || idx >= members.size() ? -1 : idx;
	}

	private int colAt(double mx) {
		int idx = (int) ((mx - colX() + hScroll) / CELL);
		return idx < 0 || idx >= members.size() ? -1 : idx;
	}

	@Nullable
	private Double value(T a, T b) {
		Map<T, Double> sub = map.get(a);
		return sub == null ? null : sub.get(b);
	}

	/**
	 * Effective relation from a to b: whichever direction carries a value, or the product of both.
	 */
	private double displayValue(T a, T b) {
		Double ab = value(a, b);
		Double ba = value(b, a);
		if (ab != null && ba != null) return ab * ba;
		if (ab != null) return ab;
		return ba == null ? 0 : ba;
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
		clamp();
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
					clamp();
					Minecraft.getInstance().setScreen(ExclusionGridScreen.this);
				}, this));
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0 && dragBar(mx, my)) {
			return true;
		}
		if (button == 0) {
			if (mx >= SIDE && mx < colX() && my >= rowY() && my <= gridBottom()) {
				int r = rowAt(my);
				if (r >= 0) {
					selected = members.get(r);
					removeBtn.active = true;
					return true;
				}
			}
			if (mx >= colX() && mx <= gridRight() && my >= TOP && my < rowY()) {
				int c = colAt(mx);
				if (c >= 0) {
					selected = members.get(c);
					removeBtn.active = true;
					return true;
				}
			}
			if (mx >= colX() && mx <= gridRight() && my >= rowY() && my <= gridBottom()) {
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

	/**
	 * Starts interacting with one of the scroll bars; returns whether the click was consumed.
	 */
	private boolean dragBar(double mx, double my) {
		if (mx >= vBarX0() && mx <= width - SIDE && my >= rowY() && my <= gridBottom() && maxV() > 0) {
			drag = 0;
			int thumb = vThumbPos();
			int len = vThumbLen();
			if (my >= thumb && my <= thumb + len) {
				dragOffset = my - thumb;
			} else {
				dragOffset = len / 2.0;
				jumpV(my);
			}
			return true;
		}
		if (mx >= colX() && mx <= gridRight() && my >= hBarY0() && my <= hBarY0() + BAR && maxH() > 0) {
			drag = 1;
			int thumb = hThumbPos();
			int len = hThumbLen();
			if (mx >= thumb && mx <= thumb + len) {
				dragOffset = mx - thumb;
			} else {
				dragOffset = len / 2.0;
				jumpH(mx);
			}
			return true;
		}
		return false;
	}

	private void jumpV(double my) {
		int travel = gridBottom() - rowY() - vThumbLen();
		if (travel > 0 && maxV() > 0) {
			vScroll = (my - rowY() - dragOffset) * maxV() / travel;
			clamp();
		}
	}

	private void jumpH(double mx) {
		int travel = gridRight() - colX() - hThumbLen();
		if (travel > 0 && maxH() > 0) {
			hScroll = (mx - colX() - dragOffset) * maxH() / travel;
			clamp();
		}
	}

	@Override
	public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
		if (drag == 0) {
			jumpV(my);
			return true;
		}
		if (drag == 1) {
			jumpH(mx);
			return true;
		}
		return super.mouseDragged(mx, my, button, dx, dy);
	}

	@Override
	public boolean mouseReleased(double mx, double my, int button) {
		if (drag >= 0) {
			drag = -1;
			return true;
		}
		return super.mouseReleased(mx, my, button);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double delta) {
		if (delta != 0 && mx >= SIDE && mx <= gridRight() && my >= TOP && my <= gridBottom()) {
			if (hasShiftDown()) {
				hScroll -= delta * CELL;
			} else {
				vScroll -= delta * CELL;
			}
			clamp();
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
		if (subtitle != null) {
			g.drawCenteredString(font, subtitle, width / 2, 30, 0xAAAAAA);
		}
		clamp();
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
		if (mx >= colX() && mx <= gridRight() && my >= TOP && my < rowY()) {
			int c = colAt(mx);
			return c >= 0 ? members.get(c) : null;
		}
		return null;
	}

	private void renderGrid(GuiGraphics g) {
		int cols = members.size();
		int offX = (int) hScroll;
		int offY = (int) vScroll;
		// cells
		if (cols > 0) {
			g.enableScissor(colX(), rowY(), gridRight(), gridBottom());
			for (int r = 0; r < cols; r++) {
				int y0 = rowY() + r * CELL - offY;
				if (y0 + CELL <= rowY()) continue;
				if (y0 >= gridBottom()) break;
				T row = members.get(r);
				for (int c = 0; c < cols; c++) {
					int x0 = colX() + c * CELL - offX;
					if (x0 + CELL <= colX()) continue;
					if (x0 >= gridRight()) break;
					renderCell(g, r, c, x0, y0, row);
				}
			}
			g.disableScissor();
			// row headers
			g.enableScissor(SIDE, rowY(), colX(), gridBottom());
			for (int r = 0; r < cols; r++) {
				int y0 = rowY() + r * CELL - offY;
				if (y0 + CELL <= rowY()) continue;
				if (y0 >= gridBottom()) break;
				renderRowHeader(g, r, y0);
			}
			g.disableScissor();
			// column headers
			g.enableScissor(colX(), TOP, gridRight(), rowY());
			for (int c = 0; c < cols; c++) {
				int x0 = colX() + c * CELL - offX;
				if (x0 + CELL <= colX()) continue;
				if (x0 >= gridRight()) break;
				renderColHeader(g, c, x0);
			}
			g.disableScissor();
		}
		renderBars(g, cols);
	}

	private void renderCell(GuiGraphics g, int r, int c, int x0, int y0, T row) {
		T col = members.get(c);
		boolean selectedCol = col.equals(this.selected);
		if (r == c) {
			g.fill(x0, y0, x0 + CELL, y0 + CELL, 0x10000000);
			g.fill(x0, y0, x0 + 1, y0 + CELL, 0xFF3C3C3C);
			g.fill(x0, y0 + CELL - 1, x0 + CELL, y0 + CELL, 0xFF3C3C3C);
		} else if (value(row, col) == null && value(col, row) == null) {
			g.fill(x0, y0, x0 + CELL, y0 + CELL, selectedCol ? 0x201D1D6A : 0x2D111111);
			g.fill(x0, y0, x0 + 1, y0 + CELL, 0xFF3C3C3C);
			g.fill(x0, y0 + CELL - 1, x0 + CELL, y0 + CELL, 0xFF3C3C3C);
		} else {
			g.fill(x0, y0, x0 + CELL, y0 + CELL, selectedCol ? 0x4064A64C : 0x30333311);
			drawCellText(g, DoubleMapScreen.format(displayValue(row, col)), x0, y0);
			g.fill(x0, y0, x0 + 1, y0 + CELL, 0xFF3C3C3C);
			g.fill(x0, y0 + CELL - 1, x0 + CELL, y0 + CELL, 0xFF3C3C3C);
		}
	}

	/**
	 * Draws cell text centered, scaled down to fit inside the cell when needed.
	 */
	private void drawCellText(GuiGraphics g, String s, int x0, int y0) {
		int w = font.width(s);
		float scale = w <= 0 ? 1 : Math.min(1f, (CELL - 5f) / w);
		PoseStack pose = g.pose();
		pose.pushPose();
		pose.translate(x0 + (CELL - w * scale) / 2f, y0 + (CELL - 9 * scale) / 2f, 0);
		pose.scale(scale, scale, 1);
		g.drawString(font, s, 0, 0, 0xFFFFFF);
		pose.popPose();
	}

	private void renderRowHeader(GuiGraphics g, int r, int y0) {
		T row = members.get(r);
		g.fill(SIDE, y0, SIDE + HEADER_SIZE, y0 + CELL,
				row.equals(selected) ? 0x40FFFFFF : 0x20AAAAAA);
		ItemStack ic = handler.icon(row);
		if (ic != null && !ic.isEmpty()) {
			g.renderItem(ic, SIDE + (HEADER_SIZE - 16) / 2, y0 + (CELL - 16) / 2);
		}
		g.fill(SIDE, y0 + CELL - 1, SIDE + HEADER_SIZE, y0 + CELL, 0xFF3C3C3C);
	}

	private void renderColHeader(GuiGraphics g, int c, int x0) {
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

	private void renderBars(GuiGraphics g, int cols) {
		// vertical
		g.fill(vBarX0(), rowY(), width - SIDE, gridBottom(),
				maxV() > 0 ? 0x20888888 : 0x10888888);
		if (maxV() > 0) {
			int pos = vThumbPos();
			g.fill(vBarX0(), pos, width - SIDE, pos + vThumbLen(), 0xFF666666);
		}
		// horizontal
		g.fill(colX(), hBarY0(), gridRight(), hBarY0() + BAR,
				maxH() > 0 ? 0x20888888 : 0x10888888);
		if (maxH() > 0) {
			int pos = hThumbPos();
			g.fill(pos, hBarY0(), pos + hThumbLen(), hBarY0() + BAR, 0xFF666666);
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
			clamp();
			Minecraft.getInstance().setScreen(ExclusionGridScreen.this);
		}

	}

	@Override
	public void onClose() {
		exit();
	}

}
