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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

public class EntityConfigEntryScreen extends EditorScreen {

	private final EntityConfig.Config config;
	private final Consumer<EntityConfig.Config> onDone;
	private final Screen parent;
	private final EditorSession session = new EditorSession();
	private final LinkedHashSet<EntityType<?>> entitySet = new LinkedHashSet<>();

	private EditorList list;

	public EntityConfigEntryScreen(Component title, EntityConfig.Config config,
	                               Consumer<EntityConfig.Config> onDone, Screen parent) {
		super(title);
		this.config = config;
		this.onDone = onDone;
		this.parent = parent;
		config.entities.stream().map(Holder::value).forEach(entitySet::add);
	}

	@Override
	protected void init() {
		list = new EditorList(minecraft, width, height - 70, 30, height - 40);
		addRenderableWidget(list);
		List<Button> row = new ArrayList<>();
		row.add(Button.builder(EditorText.BACK.get(), b -> exit()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		rebuild();
	}

	private void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorForms.entry(HostilityEditorLang.APPLIES_TO.get(),
				HostilityEditorForms.entityListName(entitySet)),
				entityIcon(), this::editEntities, entitySet.isEmpty(),
				HostilityEditorLang.ROW_APPLIES_TO_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.entry(HostilityEditorLang.DIFFICULTY_EDIT.get(),
				HostilityEditorForms.difficultySummary(config.difficulty())),
				null, this::editDifficulty, HostilityEditorForms.defaultDifficulty(config.difficulty()),
				HostilityEditorLang.ROW_DIFF_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.TRAIT_BASE_LIST.get(), config.traits().size()),
				null, this::editTraits, config.traits().isEmpty(), HostilityEditorLang.ROW_TRAITS_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.TRAIT_BLACKLIST.get(), config.blacklist().size()),
				null, this::editBlacklist, config.blacklist().isEmpty(), HostilityEditorLang.ROW_BLACK_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.ITEMS.get(), config.items.size()),
				null, this::editItems, config.items.isEmpty(), HostilityEditorLang.ROW_ITEMS_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.entry(HostilityEditorLang.VALUES_EDIT.get(),
				HostilityEditorForms.entityValuesSummary(config)),
				null, this::editValues, HostilityEditorForms.defaultValues(config), HostilityEditorLang.ROW_VALUES_TIP.get()));
		if (HostilityEditorForms.hasMaster(config)) {
			Component text = config.asMaster == null ? HostilityEditorLang.MASTER_CONFIG.get()
					: HostilityEditorForms.entry(HostilityEditorLang.MASTER_CONFIG.get(),
					HostilityEditorForms.masterSummary(config.asMaster));
			entries.add(new EditorList.Entry(text, null, this::editMaster, config.asMaster == null,
					HostilityEditorLang.ROW_MASTER_TIP.get()));
		}
		list.setData(entries);
	}

	@Nullable
	private ItemStack entityIcon() {
		if (entitySet.isEmpty()) return null;
		return HostilityEditorUtil.entityIcon(entitySet.iterator().next());
	}

	private void editEntities() {
		Minecraft.getInstance().setScreen(new ItemListScreen<>(HostilityEditorLang.ENTITY_LIST.get(),
				entitySet, () -> entitySet,
				HostilityEditorUtil.listEntityTypes(), HostilityEditorHandlers.ENTITY_TYPE,
				HostilityEditorLang.SELECT_ENTITY.get(), EntityConfigEntryScreen.this, session));
	}

	private void editDifficulty() {
		Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.DIFFICULTY_EDIT.get(),
				HostilityEditorForms.difficultyConfig(config.difficulty()), c -> {
			config.setDifficulty(c);
			session.dirty = true;
		}, EntityConfigEntryScreen.this));
	}

	private void editTraits() {
		Minecraft.getInstance().setScreen(new TraitBaseListScreen(
				HostilityEditorLang.TRAIT_BASE_LIST.get(), config.traits(), EntityConfigEntryScreen.this, session));
	}

	private void editBlacklist() {
		Minecraft.getInstance().setScreen(new ItemListScreen<>(HostilityEditorLang.TRAIT_BLACKLIST.get(),
				config.blacklist(), () -> config.blacklist(),
				HostilityEditorUtil.listTraits(), HostilityEditorHandlers.TRAIT,
				HostilityEditorLang.SELECT_TRAIT.get(), EntityConfigEntryScreen.this, session));
	}

	private void editItems() {
		Minecraft.getInstance().setScreen(new ItemPoolListScreen(
				HostilityEditorLang.ITEMS.get(), config.items, EntityConfigEntryScreen.this, session));
	}

	private void editValues() {
		Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.VALUES_EDIT.get(),
				HostilityEditorForms.entityValues(config), c -> {
			session.dirty = true;
		}, EntityConfigEntryScreen.this));
	}

	private void editMaster() {
		Minecraft.getInstance().setScreen(new MasterConfigScreen(
				config.asMaster, this::setMaster, EntityConfigEntryScreen.this, session));
	}

	private void setMaster(@Nullable EntityConfig.MasterConfig master) {
		config.asMaster = master;
		session.dirty = true;
	}

	private void exit() {
		if (session.dirty) {
			config.entities = HolderSet.direct(EntityType::builtInRegistryHolder, entitySet);
			onDone.accept(config);
		}
		Minecraft.getInstance().setScreen(parent);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g, mx, my, pTick);
		super.render(g, mx, my, pTick);
		g.drawCenteredString(font, this.title, width / 2, 10, 0xFFFFFF);
		list.renderRowTooltip(g);
	}

	@Override
	public void onClose() {
		exit();
	}

}
