package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemPoolListScreen extends ListEditScreen<EntityConfig.ItemPool> {

	public ItemPoolListScreen(Component title, List<EntityConfig.ItemPool> data, Screen parent, EditorSession session) {
		super(title, data, new Handler(session), parent, session);
	}

	private record Handler(EditorSession session) implements ListEditScreen.Handler<EntityConfig.ItemPool> {

		@Override
			public Component label(EntityConfig.ItemPool t) {
				return HostilityEditorForms.itemPoolSummary(t);
			}

			@Override
			@Nullable
			public ItemStack icon(EntityConfig.ItemPool t) {
				if (t.entries().isEmpty()) return null;
				ItemStack stack = t.entries().get(0).stack();
				return stack.isEmpty() ? null : stack;
			}

			@Override
			public Component summary(EntityConfig.ItemPool t) {
				return HostilityEditorForms.itemPoolSummary(t);
			}

			@Override
			public void onAdd(Consumer<EntityConfig.ItemPool> onDone, Screen parent) {
				Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.ITEM_POOL.get(),
						HostilityEditorForms.itemPool(), pool -> {
					openEntries(pool, onDone, parent);
				}, parent));
			}

			@Override
			public void onEdit(EntityConfig.ItemPool cur, Consumer<EntityConfig.ItemPool> onDone, Screen parent) {
				openEntries(cur, onDone, parent);
			}

			private void openEntries(EntityConfig.ItemPool cur, Consumer<EntityConfig.ItemPool> onDone, Screen parent) {
				Minecraft.getInstance().setScreen(new ItemEntryListScreen(
						HostilityEditorLang.ITEM_ENTRY_LIST.get(), cur.entries(), parent, session));
			}

		}

}
