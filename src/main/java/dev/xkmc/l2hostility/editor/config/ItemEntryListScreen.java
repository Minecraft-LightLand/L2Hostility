package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorUtil;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.base.PickListScreen;
import dev.xkmc.l2hostility.editor.base.PromptScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemEntryListScreen extends ListEditScreen<EntityConfig.ItemEntry> {

	public ItemEntryListScreen(Component title, List<EntityConfig.ItemEntry> data, Screen parent, EditorSession session) {
		super(title, data, new Handler(), parent, session);
	}

	private static final class Handler implements ListEditScreen.Handler<EntityConfig.ItemEntry> {

		@Override
		public Component label(EntityConfig.ItemEntry t) {
			return HostilityEditorForms.itemEntrySummary(t);
		}

		@Override
		@Nullable
		public ItemStack icon(EntityConfig.ItemEntry t) {
			return t.stack().isEmpty() ? null : t.stack();
		}

		@Override
		public Component summary(EntityConfig.ItemEntry t) {
			return HostilityEditorForms.entry(t.stack().getHoverName(),
					Component.literal("w " + t.weight()));
		}

		@Override
		public void onAdd(Consumer<EntityConfig.ItemEntry> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_ITEM.get(),
					EditorUtil.listItems(), EditorHandler.Pick.of(HostilityEditorHandlers.ITEM,
					item -> promptWeight(new EntityConfig.ItemEntry(100, item.getDefaultInstance()), onDone, parent)), parent));
		}

		@Override
		public void onEdit(EntityConfig.ItemEntry cur, Consumer<EntityConfig.ItemEntry> onDone, Screen parent) {
			promptWeight(cur, onDone, parent);
		}

		private void promptWeight(EntityConfig.ItemEntry cur, Consumer<EntityConfig.ItemEntry> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new PromptScreen(EditorText.EDIT.get(), HostilityEditorLang.WEIGHT.get(),
					"" + cur.weight(), HostilityEditorForms::intValidate, s -> onDone.accept(
					new EntityConfig.ItemEntry(Integer.parseInt(s.trim()), cur.stack())), parent));
		}

	}

}
