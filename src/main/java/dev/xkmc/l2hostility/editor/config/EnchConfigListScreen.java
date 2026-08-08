package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class EnchConfigListScreen extends ListEditScreen<WeaponConfig.EnchConfig> {

	public EnchConfigListScreen(Component title, List<WeaponConfig.EnchConfig> data, Screen parent, EditorSession session) {
		super(title, data, new Handler(), parent, session);
	}

	private static final class Handler implements ListEditScreen.Handler<WeaponConfig.EnchConfig> {

		@Override
		public Component label(WeaponConfig.EnchConfig t) {
			return HostilityEditorForms.enchConfigSummary(t);
		}

		@Override
		@Nullable
		public ItemStack icon(WeaponConfig.EnchConfig t) {
			if (t.enchantments().isEmpty()) return null;
			return HostilityEditorUtil.enchantIcon();
		}

		@Override
		public Component summary(WeaponConfig.EnchConfig t) {
			return HostilityEditorForms.enchConfigSummary(t);
		}

		@Override
		public void onAdd(Consumer<WeaponConfig.EnchConfig> onDone, Screen parent) {
			openEditor(parent, onDone, null);
		}

		@Override
		public void onEdit(WeaponConfig.EnchConfig cur, Consumer<WeaponConfig.EnchConfig> onDone, Screen parent) {
			openEditor(parent, onDone, cur);
		}

		/**
		 * Opens the value page (level/chance) first; the picked set is edited from there.
		 */
		private static void openEditor(Screen parent, Consumer<WeaponConfig.EnchConfig> onDone,
									  @Nullable WeaponConfig.EnchConfig existing) {
			Set<Enchantment> picked = new LinkedHashSet<>();
			if (existing != null) picked.addAll(existing.enchantments());
			int level = existing == null ? 0 : existing.level();
			float chance = existing == null ? 0 : existing.chance();
			Minecraft.getInstance().setScreen(new SetValueEditScreen<>(HostilityEditorLang.ENCH_CONFIG.get(),
					parent, onDone, picked, HostilityEditorUtil.listEnchantments(), HostilityEditorHandlers.ENCHANTMENT,
					HostilityEditorLang.SELECT_ENCHANTMENT.get(), HostilityEditorLang.SELECT_ENCHANTMENT.get(),
					List.of(
							FormScreen.FormField.text(HostilityEditorLang.LEVEL.get(), "" + level, HostilityEditorForms.intValidate()),
							FormScreen.FormField.text(HostilityEditorLang.CHANCE.get(), "" + chance, HostilityEditorForms.doubleValidate())
					),
					(list, v) -> new WeaponConfig.EnchConfig(new ArrayList<>(list),
							Integer.parseInt(v.get(0).trim()), (float) Double.parseDouble(v.get(1).trim()))));
		}

	}

}
