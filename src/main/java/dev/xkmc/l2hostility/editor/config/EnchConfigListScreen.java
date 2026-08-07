package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.base.PickListScreen;
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
			Minecraft.getInstance().setScreen(new EnchPickScreen(parent, onDone, null));
		}

		@Override
		public void onEdit(WeaponConfig.EnchConfig cur, Consumer<WeaponConfig.EnchConfig> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new EnchPickScreen(parent, onDone, cur));
		}

	}

	private static final class EnchPickScreen extends dev.xkmc.l2hostility.editor.base.ItemListScreen<Enchantment> {

		private final Screen parent;
		private final Consumer<WeaponConfig.EnchConfig> onDone;
		@Nullable
		private final WeaponConfig.EnchConfig existing;
		private final Set<Enchantment> picked;

		private EnchPickScreen(Screen parent, Consumer<WeaponConfig.EnchConfig> onDone,
							   @Nullable WeaponConfig.EnchConfig existing) {
			this(parent, onDone, existing, new LinkedHashSet<>());
		}

		private EnchPickScreen(Screen parent, Consumer<WeaponConfig.EnchConfig> onDone,
							   @Nullable WeaponConfig.EnchConfig existing, Set<Enchantment> picked) {
			super(HostilityEditorLang.SELECT_ENCHANTMENT.get(), picked, () -> new LinkedHashSet<>(),
					HostilityEditorUtil.listEnchantments(), HostilityEditorHandlers.ENCHANTMENT,
					HostilityEditorLang.SELECT_ENCHANTMENT.get(), parent, new EditorSession());
			this.parent = parent;
			this.onDone = onDone;
			this.existing = existing;
			this.picked = picked;
			if (existing != null) picked.addAll(existing.enchantments());
		}

		@Override
		public void onClose() {
			WeaponConfig.EnchConfig base = existing;
			int level = base == null ? 0 : base.level();
			float chance = base == null ? 0 : base.chance();
			Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.ENCH_CONFIG.get(),
					HostilityEditorForms.enchConfigForm(level, chance), c -> {
						WeaponConfig.EnchConfig ans = new WeaponConfig.EnchConfig(new ArrayList<>(picked), c.level(), c.chance());
						onDone.accept(ans);
						Minecraft.getInstance().setScreen(parent);
					}, parent));
		}

	}

}
