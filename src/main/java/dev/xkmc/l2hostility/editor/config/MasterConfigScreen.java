package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import dev.xkmc.l2hostility.editor.base.EditorLayout;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.EditorScreen;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.base.PickListScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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
		list = new EditorList(minecraft, width, height - 70, 30, height - 40);
		addRenderableWidget(list);
		List<Button> row = new ArrayList<>();
		if (master != null) {
			row.add(Button.builder(EditorText.REMOVE.get(), b -> {
				master = null;
				onDone.accept(null);
				session.dirty = true;
				Minecraft.getInstance().setScreen(parent);
			}).bounds(0, 0, 60, 20).build());
		}
		row.add(Button.builder(EditorText.BACK.get(), b -> Minecraft.getInstance().setScreen(parent))
				.bounds(0, 0, 60, 20).build());
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
								openMinions();
							}, MasterConfigScreen.this))));
		} else {
			entries.add(new EditorList.Entry(HostilityEditorLang.MASTER_FIELDS.get(), null,
					() -> Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.MASTER_FIELDS.get(),
							HostilityEditorForms.masterConfig(master),
							m -> {
								master = new EntityConfig.MasterConfig(m.maxTotalCount(), m.spawnInterval(), m.minions());
								session.dirty = true;
								Minecraft.getInstance().setScreen(MasterConfigScreen.this);
							}, MasterConfigScreen.this))));
			entries.add(new EditorList.Entry(HostilityEditorLang.MINION_LIST.get(), null,
					() -> Minecraft.getInstance().setScreen(new MinionListScreen(master, MasterConfigScreen.this, session))));
		}
		list.setData(entries);
	}

	private void openMinions() {
		Minecraft.getInstance().setScreen(new MinionListScreen(master, MasterConfigScreen.this, session));
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, 10, 0xFFFFFF);
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(parent);
	}

	private static final class MinionListScreen extends ListEditScreen<EntityConfig.Minion> {

		private MinionListScreen(EntityConfig.MasterConfig master, Screen parent, EditorSession session) {
			super(HostilityEditorLang.MINION_LIST.get(), master.minions(), new MinionHandler(master), parent, session);
		}

	}

	private record MinionHandler(EntityConfig.MasterConfig master) implements ListEditScreen.Handler<EntityConfig.Minion> {

		@Override
		public Component label(EntityConfig.Minion m) {
			return HostilityEditorForms.minionSummary(m);
		}

		@Override
		@Nullable
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
