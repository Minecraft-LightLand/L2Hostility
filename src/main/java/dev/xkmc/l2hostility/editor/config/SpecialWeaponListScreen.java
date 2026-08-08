package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig.ItemConfig;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.ItemListScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.base.PickListScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class SpecialWeaponListScreen extends ListEditScreen<SpecialWeaponListScreen.Entry> {

	private final WeaponConfig config;

	public SpecialWeaponListScreen(WeaponConfig config, Screen parent, EditorSession session) {
		super(HostilityEditorLang.SPECIAL_WEAPONS.get(), toList(config.special_weapons), new Handler(), parent, session);
		this.config = config;
	}

	private static List<Entry> toList(Map<LinkedHashSet<EntityType<?>>, ArrayList<ItemConfig>> map) {
		List<Entry> ans = new ArrayList<>();
		for (var ent : map.entrySet()) {
			ans.add(new Entry(ent.getKey(), ent.getValue()));
		}
		return ans;
	}

	/**
	 * Writes the edited list back into {@code special_weapons}. Keys are mutable sets, so the
	 * map is rebuilt from scratch on save.
	 */
	@Override
	@Nullable
	protected Runnable saveAction() {
		return () -> {
			config.special_weapons.clear();
			for (Entry e : this.data()) {
				config.special_weapons.put(e.entities(), e.configs());
			}
			session().dirty = true;
			EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_DONE.get(new net.minecraft.resources.ResourceLocation("l2hostility", "special_weapons")));
		};
	}

	public record Entry(LinkedHashSet<EntityType<?>> entities, ArrayList<WeaponConfig.ItemConfig> configs) {

	}

	private static final class Handler implements ListEditScreen.Handler<Entry> {

		@Override
		public Component label(Entry e) {
			return Component.literal(e.entities().size() + " entities");
		}

		@Override
		@Nullable
		public ItemStack icon(Entry e) {
			if (e.entities().isEmpty()) return null;
			return HostilityEditorUtil.entityIcon(e.entities().iterator().next());
		}

		@Override
		public Component summary(Entry e) {
			return HostilityEditorForms.entry(
					Component.literal(e.entities().size() + " entities"),
					HostilityEditorForms.counted(HostilityEditorLang.ITEM_CONFIG.get(), e.configs().size()));
		}

		@Override
		public void onAdd(Consumer<Entry> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_ENTITY.get(),
					HostilityEditorUtil.listEntityTypes(), EditorHandler.Pick.of(HostilityEditorHandlers.ENTITY_TYPE,
					t -> {
						LinkedHashSet<EntityType<?>> set = new LinkedHashSet<>();
						set.add(t);
						Entry e = new Entry(set, new ArrayList<>());
						onDone.accept(e);
						Minecraft.getInstance().setScreen(new EntryScreen(parent, e));
					}), parent));
		}

		@Override
		public void onEdit(Entry cur, Consumer<Entry> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new EntryScreen(parent, cur));
		}

	}

	/**
	 * Two rows: entity set (multi-select) + item config list.
	 */
	private static final class EntryScreen extends dev.xkmc.l2hostility.editor.base.EditorScreen {

		private final Screen parent;
		private final Entry entry;

		private EntryScreen(Screen parent, Entry entry) {
			super(HostilityEditorLang.SPECIAL_WEAPONS.get());
			this.parent = parent;
			this.entry = entry;
		}

	@Override
	protected void init() {
		var list = new dev.xkmc.l2hostility.editor.base.EditorList(minecraft, width, height - 70, 30, height - 40);
		addRenderableWidget(list);
			java.util.List<dev.xkmc.l2hostility.editor.base.EditorList.Entry> rows = new ArrayList<>();
			rows.add(new dev.xkmc.l2hostility.editor.base.EditorList.Entry(HostilityEditorLang.ENTITIES.get(), null,
					() -> Minecraft.getInstance().setScreen(new ItemListScreen<>(HostilityEditorLang.ENTITIES.get(),
							entry.entities(), () -> entry.entities(),
							HostilityEditorUtil.listEntityTypes(), HostilityEditorHandlers.ENTITY_TYPE,
							HostilityEditorLang.SELECT_ENTITY.get(), EntryScreen.this, new dev.xkmc.l2hostility.editor.base.EditorSession()))));
			rows.add(new dev.xkmc.l2hostility.editor.base.EditorList.Entry(HostilityEditorLang.ITEM_CONFIG.get(), null,
					() -> Minecraft.getInstance().setScreen(new ItemConfigListScreen(
							HostilityEditorLang.ITEM_CONFIG.get(), entry.configs(), EntryScreen.this,
							new dev.xkmc.l2hostility.editor.base.EditorSession()))));
			list.setData(rows);
			var back = net.minecraft.client.gui.components.Button.builder(EditorText.BACK.get(), b -> Minecraft.getInstance().setScreen(parent))
					.bounds(0, 0, 60, 20).build();
			addRenderableWidget(back);
			dev.xkmc.l2hostility.editor.base.EditorLayout.centerRow(java.util.List.of(back), width / 2, height - 30, 5);
		}

		@Override
		public void onClose() {
			Minecraft.getInstance().setScreen(parent);
		}

		@Override
		public void render(GuiGraphics g, int mx, int my, float pTick) {
			super.renderBackground(g);
			super.render(g, mx, my, pTick);
			g.drawCenteredString(font, this.title, width / 2, 10, 0xFFFFFF);
		}

	}

}
