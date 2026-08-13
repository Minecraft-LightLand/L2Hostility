package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.editor.config.EntityFileScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class EntityHomeScreen extends HostilityHomeScreen {

	public EntityHomeScreen(Screen parent) {
		super(HostilityEditorLang.ENTITY.get(), 4, parent);
	}

	@Override
	protected boolean hasSearch() {
		return true;
	}

	@Override
	protected boolean featureDisabled() {
		return !LHConfig.COMMON.enableEntitySpecificDatapack.get();
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return idsOf(L2Hostility.ENTITY.getAll());
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		EntityConfig cfg = L2Hostility.ENTITY.getEntry(id);
		return cfg == null ? 0 : cfg.list.size();
	}

	@Override
	protected Component rowSuffix(ResourceLocation id) {
		EntityConfig cfg = L2Hostility.ENTITY.getEntry(id);
		if (cfg != null && cfg.list.size() == 1 && cfg.list.get(0).entities.size() == 1) {
			Component name = HostilityEditorForms.entityListName(cfg.list.get(0).entities);
			return Component.literal("  ").append(name).withStyle(ChatFormatting.WHITE);
		}
		return super.rowSuffix(id);
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.ENTITY_EMPTY.get();
	}

	@Override
	protected String newFileDefault() {
		return "l2hostility:new_entity";
	}

	@Override
	protected void openNew(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new EntityFileScreen(id, this));
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new EntityFileScreen(id, this));
	}

}