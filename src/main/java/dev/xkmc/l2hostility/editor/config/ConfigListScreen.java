package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.editor.base.EditorHandler;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.ListEditScreen;
import dev.xkmc.l2hostility.editor.base.PickListScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Shared ordered list of {@link EntityConfig.Config}. Used by the entity file screen
 * (Add picks an entity first) and by the difficulty default-traits maps
 * (Add creates an empty-entities "all entities" fallback config).
 */
public class ConfigListScreen extends ListEditScreen<EntityConfig.Config> {

	private final boolean pickEntities;

	public ConfigListScreen(Component title, List<EntityConfig.Config> data, boolean pickEntities,
							Screen parent, EditorSession session) {
		super(title, data, new ConfigHandler(pickEntities), parent, session);
		this.pickEntities = pickEntities;
	}

	private static final class ConfigHandler implements ListEditScreen.Handler<EntityConfig.Config> {

		private final boolean pickEntities;

		private ConfigHandler(boolean pickEntities) {
			this.pickEntities = pickEntities;
		}

		@Override
		public Component label(EntityConfig.Config c) {
			return HostilityEditorForms.configSummary(c);
		}

		@Override
		@Nullable
		public ItemStack icon(EntityConfig.Config c) {
			return null;
		}

		@Override
		public Component summary(EntityConfig.Config c) {
			return HostilityEditorForms.configSummary(c);
		}

		@Override
		public void onAdd(Consumer<EntityConfig.Config> onDone, Screen parent) {
			if (!pickEntities) {
				openEditor(onDone, parent, newEntityConfig(null));
				return;
			}
			Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_ENTITY.get(),
					HostilityEditorUtil.listEntityTypes(),
					EditorHandler.Pick.of(HostilityEditorHandlers.ENTITY_TYPE, t -> {
						openEditor(onDone, parent, newEntityConfig(t));
					}), parent));
		}

		private static EntityConfig.Config newEntityConfig(@Nullable EntityType<?> type) {
			EntityConfig.Config ans = new EntityConfig.Config();
			if (type != null) ans.entities.add(type);
			return ans;
		}

		private void openEditor(Consumer<EntityConfig.Config> onDone, Screen parent, EntityConfig.Config cur) {
			Minecraft.getInstance().setScreen(new EntityConfigEntryScreen(
					HostilityEditorLang.ENTITY_CONFIG.get(), cur, onDone, parent));
		}

		@Override
		public void onEdit(EntityConfig.Config cur, Consumer<EntityConfig.Config> onDone, Screen parent) {
			openEditor(onDone, parent, cur);
		}

	}

}
