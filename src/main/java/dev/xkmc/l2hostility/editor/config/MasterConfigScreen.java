package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MasterConfigScreen extends EditorScreen {

	private final Consumer<EntityConfig.MasterConfig> onDone;
	private final Screen parent;
	private final EditorSession session;
	private EntityConfig.MasterConfig master;

	private EditorList list;
	private EditBox maxBox, spawnBox;
	private Button addBtn, editBtn, removeBtn;

	public MasterConfigScreen(@Nullable EntityConfig.MasterConfig master,
	                          Consumer<EntityConfig.MasterConfig> onDone,
	                          Screen parent, EditorSession session) {
		super(HostilityEditorLang.MASTER_CONFIG.get());
		this.master = master;
		this.onDone = onDone;
		this.parent = parent;
		this.session = session;
	}

	@Override
	protected void init() {
		List<Button> row = new ArrayList<>();
		if (master == null) {
			list = new EditorList(minecraft, width, height - 70, 30, height - 40);
			addRenderableWidget(list);
		} else {
			maxBox = new EditBox(font, boxX(), 28, 140, 20, Component.literal(""));
			maxBox.setMaxLength(64);
			maxBox.setValue("" + master.maxTotalCount());
			maxBox.setResponder(s -> setField(parseInt(s, master.maxTotalCount()), master.spawnInterval()));
			spawnBox = new EditBox(font, boxX(), 56, 140, 20, Component.literal(""));
			spawnBox.setMaxLength(64);
			spawnBox.setValue("" + master.spawnInterval());
			spawnBox.setResponder(s -> setField(master.maxTotalCount(), parseInt(s, master.spawnInterval())));
			list = new EditorList(minecraft, width, height - 136, 86, height - 50);
			addRenderableWidget(list);
			addRenderableWidget(maxBox);
			addRenderableWidget(spawnBox);
			setInitialFocus(maxBox);
			addBtn = Button.builder(EditorText.ADD.get(), b -> addMinion()).bounds(0, 0, 60, 20).build();
			row.add(addBtn);
			editBtn = Button.builder(EditorText.EDIT.get(), b -> editMinion()).bounds(0, 0, 60, 20).build();
			row.add(editBtn);
			removeBtn = Button.builder(EditorText.REMOVE.get(), b -> removeMinion()).bounds(0, 0, 60, 20).build();
			row.add(removeBtn);
			row.add(EditorTip.tip(Button.builder(HostilityEditorLang.REMOVE_MASTER.get(), b -> {
				master = null;
				onDone.accept(null);
				session.dirty = true;
				Minecraft.getInstance().setScreen(parent);
			}).bounds(0, 0, 60, 20).build(), HostilityEditorLang.REMOVE_MASTER_TIP.get()));
		}
		row.add(Button.builder(EditorText.BACK.get(), b -> exit()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		rebuild();
	}

	private void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		if (master == null) {
			entries.add(new EditorList.Entry(HostilityEditorLang.ADD_MASTER.get(), null,
					() -> Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.MASTER_FIELDS.get(),
							HostilityEditorForms.masterConfig(new EntityConfig.MasterConfig(8, 600, new ArrayList<>())),
							m -> {
								master = m;
								session.dirty = true;
								Minecraft.getInstance().setScreen(MasterConfigScreen.this);
							}, MasterConfigScreen.this))));
		} else {
			if (master.minions().isEmpty()) {
				entries.add(new EditorList.Entry(EditorText.EMPTY_FILE.get(), null, null));
			}
			for (EntityConfig.Minion m : master.minions()) {
				entries.add(new EditorList.Entry(HostilityEditorForms.minionSummary(m),
						HostilityEditorUtil.entityIcon(m.type()), null));
			}
		}
		list.setData(entries);
		if (master != null) {
			list.setOnSelect(this::updateButtons);
			list.setOnDoubleClick(this::editMinion);
			updateButtons();
		}
	}

	private void setField(int maxTotal, int spawnInterval) {
		master = new EntityConfig.MasterConfig(maxTotal, spawnInterval, master.minions());
		session.dirty = true;
	}

	private int parseInt(String s, int fallback) {
		try {
			return Integer.parseInt(s.trim());
		} catch (NumberFormatException e) {
			return fallback;
		}
	}

	private void updateButtons() {
		addBtn.active = true;
		boolean has = selectedMinion() != null;
		editBtn.active = has;
		removeBtn.active = has;
	}

	@Nullable
	private EntityConfig.Minion selectedMinion() {
		EditorList.Entry sel = list.getSelected();
		if (sel == null) return null;
		int i = list.children().indexOf(sel);
		if (i < 0 || i >= master.minions().size()) return null;
		return master.minions().get(i);
	}

	private void addMinion() {
		new MinionHandler(master).onAdd(m -> {
			ArrayList<EntityConfig.Minion> minions = new ArrayList<>(master.minions());
			minions.add(m);
			setMinions(minions);
		}, this);
	}

	private void editMinion() {
		int i = list.children().indexOf(list.getSelected());
		if (i < 0 || i >= master.minions().size()) return;
		EntityConfig.Minion cur = master.minions().get(i);
		new MinionHandler(master).onEdit(cur, m -> {
			ArrayList<EntityConfig.Minion> minions = new ArrayList<>(master.minions());
			minions.set(i, m);
			setMinions(minions);
		}, this);
	}

	private void removeMinion() {
		int i = list.children().indexOf(list.getSelected());
		if (i < 0 || i >= master.minions().size()) return;
		ArrayList<EntityConfig.Minion> minions = new ArrayList<>(master.minions());
		minions.remove(i);
		setMinions(minions);
	}

	private void setMinions(ArrayList<EntityConfig.Minion> minions) {
		master = new EntityConfig.MasterConfig(master.maxTotalCount(), master.spawnInterval(), minions);
		session.dirty = true;
		Minecraft.getInstance().setScreen(MasterConfigScreen.this);
	}

	private void exit() {
		if (session.dirty) onDone.accept(master);
		Minecraft.getInstance().setScreen(parent);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g, mx, my, pTick);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, 10, 0xFFFFFF);
		if (master != null) {
			g.drawString(font, HostilityEditorLang.MAX_TOTAL_COUNT.get(), labelX(), 33, 0xAAAAAA);
			g.drawString(font, HostilityEditorLang.SPAWN_INTERVAL.get(), labelX(), 61, 0xAAAAAA);
			List<Component> tip = labelTip(mx, my);
			if (tip != null && !tip.isEmpty()) {
				g.renderComponentTooltip(font, tip, mx, my);
			}
		}
	}

	@Nullable
	private List<Component> labelTip(int mx, int my) {
		if (hoverLabel(mx, my, 28, HostilityEditorLang.MAX_TOTAL_COUNT.get())) {
			return List.of(HostilityEditorLang.MASTER_MAX_TOTAL_TIP.get());
		}
		if (hoverLabel(mx, my, 56, HostilityEditorLang.SPAWN_INTERVAL.get())) {
			return List.of(HostilityEditorLang.MASTER_SPAWN_INTERVAL_TIP.get());
		}
		return null;
	}

	private boolean hoverLabel(int mx, int my, int boxY, Component label) {
		return my >= boxY && my < boxY + 20 && mx >= labelX() && mx <= labelX() + font.width(label);
	}

	private int labelX() {
		return Math.max(4, width / 2 - 160);
	}

	private int boxX() {
		return Math.max(4, width / 2 + 40);
	}

	@Override
	public void onClose() {
		exit();
	}

	private record MinionHandler(
			EntityConfig.MasterConfig master) implements ListEditScreen.Handler<EntityConfig.Minion> {

		@Override
		public Component label(EntityConfig.Minion m) {
			return HostilityEditorForms.minionSummary(m);
		}

		@Override
		public ItemStack icon(EntityConfig.Minion m) {
			return HostilityEditorUtil.entityIcon(m.type());
		}

		@Override
		public Component summary(EntityConfig.Minion m) {
			return HostilityEditorForms.minionSummary(m);
		}

		@Override
		public void onAdd(Consumer<EntityConfig.Minion> onDone, Screen parent) {
			List<EntityType<?>> candidates = HostilityEditorUtil.listEntityTypes();
			Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_ENTITY.get(),
					candidates, EditorHandler.Pick.of(HostilityEditorHandlers.ENTITY_TYPE,
					t -> Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.MINION_FIELDS.get(),
							HostilityEditorForms.minion(t, null), m -> onDone.accept(m), parent))), parent));
		}

		@Override
		public void onEdit(EntityConfig.Minion cur, Consumer<EntityConfig.Minion> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.MINION_FIELDS.get(),
					HostilityEditorForms.minion(cur.type(), cur), m -> onDone.accept(m), parent));
		}

	}

}
