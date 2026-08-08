package dev.xkmc.l2hostility.editor.base;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public abstract class EditorHomeScreen extends EditorScreen {

	protected final Screen parent;
	private EditorList list;
	private Button reloadBtn;
	private Button editBtn;
	private EditBox search;
	private final Set<String> collapsed = new HashSet<>();

	protected EditorHomeScreen(Component title, Screen parent) {
		super(title);
		this.parent = parent;
	}

	/**
	 * Whether this tab shows a search box filtering the file list.
	 */
	protected boolean hasSearch() {
		return false;
	}

	@Override
	protected void init() {
		int listTop = 34;
		if (hasSearch()) {
			listTop = 58;
		}
		list = new EditorList(minecraft, width, height - 40 - listTop, listTop, height - 40);
		//list.setRenderTopAndBottom(false);
		addRenderableWidget(list);
		if (hasSearch()) {
			search = new EditBox(font, width / 2 - 100, 36, 200, 18, EditorText.SEARCH.get());
			search.setMaxLength(64);
			search.setResponder(s -> rebuild());
			addRenderableWidget(search);
			setInitialFocus(search);
		}
		initTabs();
		List<Button> row = new ArrayList<>();
		Button newBtn = Button.builder(EditorText.NEW.get(), b -> newFile()).bounds(0, 0, 60, 20).build();
		newBtn.active = canCreate();
		row.add(newBtn);
		editBtn = Button.builder(EditorText.EDIT.get(), b -> editFile()).bounds(0, 0, 60, 20).build();
		row.add(editBtn);
		reloadBtn = Button.builder(EditorText.RELOAD.get(), b -> reloadNow(false)).bounds(0, 0, 60, 20).build();
		row.add(reloadBtn);
		row.add(Button.builder(EditorText.BACK.get(), b -> exit()).bounds(0, 0, 60, 20).build());
		row.forEach(this::addRenderableWidget);
		EditorLayout.centerRow(row, width / 2, height - 30, 5);
		reloadBtn.active = hasPendingReload();
		editBtn.active = false;
		list.setOnSelect(() -> editBtn.active = selected() != null);
		list.setOnDoubleClick(this::editFile);
		rebuild();
	}

	private void initTabs() {
		List<EditorTab> tabs = tabs();
		int active = activeTab();
		int gap = 6;
		int h = 24;
		int total = 0;
		List<Integer> widths = new ArrayList<>();
		for (EditorTab t : tabs) {
			int w = Math.max(60, font.width(t.label()) + 24);
			widths.add(w);
			total += w;
		}
		total += gap * Math.max(0, tabs.size() - 1);
		int x = (width - total) / 2;
		for (int i = 0; i < tabs.size(); i++) {
			int idx = i;
			addRenderableWidget(new TabButton(x, 10, widths.get(i), h, tabs.get(i).label(), i == active,
					b -> openTab(idx)));
			x += widths.get(i) + gap;
		}
	}

	private void openTab(int idx) {
		if (idx == activeTab()) return;
		List<EditorTab> tabs = tabs();
		if (idx < 0 || idx >= tabs.size()) return;
		tabs.get(idx).onSelect().run();
	}

	@Override
	public void onClose() {
		exit();
	}

	private void exit() {
		if (hasPendingReload()) {
			Minecraft.getInstance().setScreen(new ReloadConfirmScreen(this, () -> reloadNow(true), () ->
					Minecraft.getInstance().setScreen(parent)));
		} else {
			Minecraft.getInstance().setScreen(parent);
		}
	}

	private void reloadNow(boolean exit) {
		setReloaded();
		IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
		if (server != null) {
			server.execute(() -> server.reloadResources(server.getPackRepository().getSelectedIds()));
			EditorToast.show(EditorText.RELOAD.get(), EditorText.RELOAD_DONE.get());
		}
		Minecraft.getInstance().setScreen(exit ? parent : this);
	}

	/**
	 * Whether a file row is drawn disabled (light gray). Overridden by the mod's home screen.
	 */
	protected boolean isDisabled(ResourceLocation id) {
		return false;
	}

	private void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		List<ResourceLocation> ids = listFiles();
		if (ids.isEmpty()) {
			entries.add(new EditorList.Entry(emptyMessage(), null, null));
			list.setData(entries);
			return;
		}
		Map<String, List<ResourceLocation>> groups = new TreeMap<>();
		for (ResourceLocation id : ids) {
			groups.computeIfAbsent(id.getNamespace(), k -> new ArrayList<>()).add(id);
		}
		String query = searchText().toLowerCase(Locale.ROOT);
		for (var ent : groups.entrySet()) {
			String ns = ent.getKey();
			List<ResourceLocation> files = ent.getValue();
			files.sort(ResourceLocation::compareTo);
			if (!query.isEmpty()) {
				files.removeIf(f -> !(ns + " " + f.getPath()).toLowerCase(Locale.ROOT).contains(query));
			}
			boolean isCollapsed = collapsed.contains(ns);
			boolean matches = !query.isEmpty() && ns.toLowerCase(Locale.ROOT).contains(query);
			boolean showFiles = !isCollapsed || matches;
			if (files.isEmpty() && !matches) continue;
			entries.add(new EditorList.Entry(Component.literal(modName(ns)), true,
					() -> toggleCollapsed(ns), isCollapsed));
			if (showFiles) {
				for (ResourceLocation f : files) {
					Component label = fileLabel(f).copy().append(Component.literal("   (" + fileCount(f) + ")"));
					boolean disabled = isDisabled(f);
					if (disabled) {
						label = label.copy().withStyle(ChatFormatting.RED, ChatFormatting.STRIKETHROUGH);
					}
					entries.add(new EditorList.Entry(label, null, null, f, disabled));
				}
			}
		}
		list.setData(entries);
	}

	private void toggleCollapsed(String ns) {
		if (!collapsed.add(ns)) {
			collapsed.remove(ns);
		}
		rebuild();
	}

	private String searchText() {
		return search == null ? "" : search.getValue();
	}

	private static String modName(String ns) {
		var opt = ModList.get().getModContainerById(ns);
		if (opt.isPresent()) {
			return opt.get().getModInfo().getDisplayName();
		}
		return ns;
	}

	@Nullable
	private ResourceLocation selected() {
		EditorList.Entry sel = list.getSelected();
		if (sel == null) return null;
		Object data = sel.getData();
		return data instanceof ResourceLocation id ? id : null;
	}

	private void newFile() {
		Minecraft.getInstance().setScreen(new PromptScreen(EditorText.NEW.get(), fileIdLabel(),
				newFileDefault(), validateId(), s -> {
			ResourceLocation id = EditorFile.parseId(s);
			if (id == null) return;
			openNew(id);
		}, this));
	}

	private void editFile() {
		ResourceLocation id = selected();
		if (id == null) return;
		openEdit(id);
	}

	protected abstract List<ResourceLocation> listFiles();

	protected Component fileLabel(ResourceLocation id) {
		return Component.literal(id.getPath());
	}

	protected boolean canCreate() {
		return true;
	}

	protected abstract int fileCount(ResourceLocation id);

	protected abstract Component emptyMessage();

	protected abstract String newFileDefault();

	protected abstract void openNew(ResourceLocation id);

	protected abstract void openEdit(ResourceLocation id);

	protected abstract List<EditorTab> tabs();

	protected abstract int activeTab();

	protected abstract Component fileIdLabel();

	protected abstract Function<String, Component> validateId();

	protected abstract boolean hasPendingReload();

	protected abstract void setReloaded();

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.renderBackground(g);
		super.render(g, mx, my, pTick);
	}

}
