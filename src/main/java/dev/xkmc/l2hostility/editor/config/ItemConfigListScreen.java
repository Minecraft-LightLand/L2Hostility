package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorUtil;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
		@Nullable
		public Supplier<ItemStack> iconSupplier(WeaponConfig.ItemConfig t) {
			if (t.stack().isEmpty()) return null;
			return () -> {
				int idx = (int) ((Util.getMillis() / 1000) % t.stack().size());
				return t.stack().get(idx);
			};
		}

		@Override
		public Component summary(WeaponConfig.ItemConfig t) {
			return HostilityEditorForms.itemConfigSummary(t);
		}

		@Override
		public void onAdd(Consumer<WeaponConfig.ItemConfig> onDone, Screen parent) {
			openEditor(parent, onDone, null);
		}

		@Override
		public void onEdit(WeaponConfig.ItemConfig cur, Consumer<WeaponConfig.ItemConfig> onDone, Screen parent) {
			openEditor(parent, onDone, cur);
		}

		/**
		 * Opens the value page (level/weight) first; the picked set is edited from there.
		 */
		private static void openEditor(Screen parent, Consumer<WeaponConfig.ItemConfig> onDone,
		                               @Nullable WeaponConfig.ItemConfig existing) {
			Set<Item> picked = new LinkedHashSet<>();
			if (existing != null) {
				for (ItemStack s : existing.stack()) {
					if (!s.isEmpty()) picked.add(s.getItem());
				}
			}
			int level = existing == null ? 0 : existing.level();
			int weight = existing == null ? 100 : existing.weight();
			Minecraft.getInstance().setScreen(new SetValueEditScreen<>(HostilityEditorLang.ITEM_CONFIG.get(),
					parent, onDone, picked, EditorUtil.listItems(), HostilityEditorHandlers.ITEM,
					HostilityEditorLang.SELECT_ITEM.get(),
					List.of(
							FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "" + level, HostilityEditorForms::intValidate),
							FormScreen.FormField.text(HostilityEditorLang.WEIGHT.get(), "" + weight, HostilityEditorForms::intValidate)
					),
					(list, v) -> {
						ArrayList<ItemStack> stacks = new ArrayList<>();
						for (Item it : list) stacks.add(it.getDefaultInstance());
						return new WeaponConfig.ItemConfig(stacks, Integer.parseInt(v.get(0).trim()),
								Integer.parseInt(v.get(1).trim()), existing == null ? null : existing.condition());
					}));
		}

	}

}
