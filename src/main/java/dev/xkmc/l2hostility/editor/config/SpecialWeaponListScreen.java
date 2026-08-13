package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig.ItemConfig;
import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpecialWeaponListScreen extends ListEditScreen<SpecialWeaponListScreen.Entry> {

	private final WeaponConfig config;

	public SpecialWeaponListScreen(WeaponConfig config, Screen parent, EditorSession session) {
		super(HostilityEditorLang.SPECIAL_WEAPONS.get(), toList(config.special_weapons), new Handler(), parent, session);
		this.config = config;
	}

	private static List<Entry> toList(Map<HolderSet<EntityType<?>>, ArrayList<ItemConfig>> map) {
		List<Entry> ans = new ArrayList<>();
		for (var ent : map.entrySet()) {
			LinkedHashSet<EntityType<?>> set = new LinkedHashSet<>(ent.getKey().stream().map(Holder::value).toList());
			ans.add(new Entry(set, ent.getValue()));
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
				config.special_weapons.put(HolderSet.direct(EntityType::builtInRegistryHolder, e.entities()), e.configs());
			}
			session().dirty = true;
			EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_DONE.get(ResourceLocation.fromNamespaceAndPath("l2hostility", "special_weapons")));
		};
	}

	public record Entry(LinkedHashSet<EntityType<?>> entities, ArrayList<WeaponConfig.ItemConfig> configs) {

	}

	private static final class Handler implements ListEditScreen.Handler<Entry> {

		@Override
		public Component label(Entry e) {
			return Component.literal(e.entities().size() + " entities");
		}

		/**
		 * All non-empty item stacks across the entry's item configs, in order.
		 */
		private static List<ItemStack> allStacks(Entry e) {
			List<ItemStack> ans = new ArrayList<>();
			for (WeaponConfig.ItemConfig c : e.configs()) {
				for (ItemStack s : c.stack()) {
					if (!s.isEmpty()) ans.add(s);
				}
			}
			return ans;
		}

		@Override
		@Nullable
		public ItemStack icon(Entry e) {
			List<ItemStack> stacks = allStacks(e);
			return stacks.isEmpty() ? null : stacks.get(0);
		}

		@Override
		@Nullable
		public Supplier<ItemStack> iconSupplier(Entry e) {
			List<ItemStack> stacks = allStacks(e);
			if (stacks.isEmpty()) return null;
			return () -> {
				int idx = (int) ((Util.getMillis() / 1000) % stacks.size());
				return stacks.get(idx);
			};
		}

		@Override
		public Component summary(Entry e) {
			return HostilityEditorForms.entityListName(e.entities());
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
	private static final class EntryScreen extends EditorScreen {

		private final Screen parent;
		private final Entry entry;

		private EntryScreen(Screen parent, Entry entry) {
			super(HostilityEditorLang.SPECIAL_WEAPONS.get());
			this.parent = parent;
			this.entry = entry;
		}

		@Override
		protected void init() {
			var list = new EditorList(minecraft, width, height - 70, 30, EditorList.ITEM_HEIGHT);
			addRenderableWidget(list);
			List<EditorList.Entry> rows = new ArrayList<>();
			rows.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.ENTITIES.get(), entry.entities().size()), null,
					() -> Minecraft.getInstance().setScreen(new ItemListScreen<>(HostilityEditorLang.ENTITIES.get(),
							entry.entities(), () -> entry.entities(),
							HostilityEditorUtil.listEntityTypes(), HostilityEditorHandlers.ENTITY_TYPE,
							HostilityEditorLang.SELECT_ENTITY.get(), EntryScreen.this, new EditorSession()))));
			rows.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.ITEM_CONFIG.get(), entry.configs().size()), null,
					() -> Minecraft.getInstance().setScreen(new ItemConfigListScreen(
							HostilityEditorLang.ITEM_CONFIG.get(), entry.configs(), EntryScreen.this,
							new EditorSession()))));
			list.setData(rows);
			var back = Button.builder(EditorText.BACK.get(), b -> Minecraft.getInstance().setScreen(parent))
					.bounds(0, 0, 60, 20).build();
			addRenderableWidget(back);
			EditorLayout.centerRow(List.of(back), width / 2, height - 30, 5);
		}

		@Override
		public void onClose() {
			Minecraft.getInstance().setScreen(parent);
		}

		@Override
		public void render(GuiGraphics g, int mx, int my, float pTick) {
			super.renderBackground(g, mx, my, pTick);
			super.render(g, mx, my, pTick);
			g.drawCenteredString(font, this.title, width / 2, 10, 0xFFFFFF);
		}

	}

}
