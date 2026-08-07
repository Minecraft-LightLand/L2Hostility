package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorUtil;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ItemListScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ItemConfigListScreen extends ListEditScreen<WeaponConfig.ItemConfig> {

	public ItemConfigListScreen(Component title, List<WeaponConfig.ItemConfig> data, Screen parent, EditorSession session) {
		super(title, data, new Handler(), parent, session);
	}

	private static final class Handler implements ListEditScreen.Handler<WeaponConfig.ItemConfig> {

		@Override
		public Component label(WeaponConfig.ItemConfig t) {
			return HostilityEditorForms.itemConfigSummary(t);
		}

		@Override
		@Nullable
		public ItemStack icon(WeaponConfig.ItemConfig t) {
			if (t.stack().isEmpty()) return null;
			return t.stack().get(0);
		}

		@Override
		public Component summary(WeaponConfig.ItemConfig t) {
			return HostilityEditorForms.itemConfigSummary(t);
		}

		@Override
		public void onAdd(Consumer<WeaponConfig.ItemConfig> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new ItemPickScreen(parent, onDone, null));
		}

		@Override
		public void onEdit(WeaponConfig.ItemConfig cur, Consumer<WeaponConfig.ItemConfig> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new ItemPickScreen(parent, onDone, cur));
		}

	}

	/**
	 * Multi-select items, then enter level/weight. When editing, the existing stack list,
	 * level and weight are preserved; a non-null {@code condition} is carried over.
	 */
	private static final class ItemPickScreen extends ItemListScreen<Item> {

		private final Screen parent;
		private final Consumer<WeaponConfig.ItemConfig> onDone;
		@Nullable
		private final WeaponConfig.ItemConfig existing;
		private final Set<Item> picked;

		private ItemPickScreen(Screen parent, Consumer<WeaponConfig.ItemConfig> onDone,
							   @Nullable WeaponConfig.ItemConfig existing) {
			this(parent, onDone, existing, prefill(existing));
		}

		private ItemPickScreen(Screen parent, Consumer<WeaponConfig.ItemConfig> onDone,
							   @Nullable WeaponConfig.ItemConfig existing, Set<Item> picked) {
			super(HostilityEditorLang.SELECT_ITEM.get(), picked, () -> new LinkedHashSet<>(),
					EditorUtil.listItems(), HostilityEditorHandlers.ITEM,
					HostilityEditorLang.SELECT_ITEM.get(), parent, new EditorSession());
			this.parent = parent;
			this.onDone = onDone;
			this.existing = existing;
			this.picked = picked;
		}

		private static Set<Item> prefill(@Nullable WeaponConfig.ItemConfig existing) {
			Set<Item> ans = new LinkedHashSet<>();
			if (existing != null) {
				for (ItemStack s : existing.stack()) {
					if (!s.isEmpty()) ans.add(s.getItem());
				}
			}
			return ans;
		}

		@Override
		public void onClose() {
			java.util.ArrayList<ItemStack> stacks = new java.util.ArrayList<>();
			for (Item it : picked) stacks.add(it.getDefaultInstance());
			WeaponConfig.ItemConfig base = existing;
			int level = base == null ? 0 : base.level();
			int weight = base == null ? 100 : base.weight();
			Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.ITEM_CONFIG.get(),
					HostilityEditorForms.itemConfigForm(level, weight), c -> {
						WeaponConfig.ItemConfig ans = new WeaponConfig.ItemConfig(stacks, c.level(), c.weight(),
								base == null ? null : base.condition());
						onDone.accept(ans);
						Minecraft.getInstance().setScreen(parent);
					}, parent));
		}

	}

}
