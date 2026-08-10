package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorHandlers;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class TraitBaseListScreen extends ListEditScreen<EntityConfig.TraitBase> {

	public TraitBaseListScreen(Component title, List<EntityConfig.TraitBase> data, Screen parent, EditorSession session) {
		super(title, data, new Handler(), parent, session);
	}

	private static final class Handler implements ListEditScreen.Handler<EntityConfig.TraitBase> {

		@Override
		public Component label(EntityConfig.TraitBase t) {
			return HostilityEditorForms.traitBaseSummary(t);
		}

		@Override
		@Nullable
		public ItemStack icon(EntityConfig.TraitBase t) {
			return t.trait() == null ? null : HostilityEditorUtil.traitIcon(t.trait());
		}

		@Override
		public Component summary(EntityConfig.TraitBase t) {
			return HostilityEditorForms.traitBaseSummary(t);
		}

		@Override
		public void onAdd(Consumer<EntityConfig.TraitBase> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new PickListScreen<>(HostilityEditorLang.SELECT_TRAIT.get(),
					HostilityEditorUtil.listTraits(), EditorHandler.Pick.of(HostilityEditorHandlers.TRAIT,
					trait -> Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.TRAIT.get(),
							HostilityEditorForms.traitBase(trait, null),
							updated -> onDone.accept(updated), parent))), parent));
		}

		@Override
		public void onEdit(EntityConfig.TraitBase cur, Consumer<EntityConfig.TraitBase> onDone, Screen parent) {
			Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.TRAIT.get(),
					HostilityEditorForms.traitBase(cur.trait(), cur),
					updated -> onDone.accept(updated), parent));
		}

	}

}
