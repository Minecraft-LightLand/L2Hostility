package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.editor.config.DifficultyFileScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DifficultyHomeScreen extends HostilityHomeScreen {

	public DifficultyHomeScreen(Screen parent) {
		super(HostilityEditorLang.WORLD.get(), 0, parent);
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return idsOf(L2Hostility.DIFFICULTY.getAll());
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		WorldDifficultyConfig cfg = L2Hostility.DIFFICULTY.getEntry(id);
		if (cfg == null) return 0;
		return cfg.levelMap.size() + cfg.biomeMap.size()
				+ cfg.levelDefaultTraits.size() + cfg.structureDefaultTraits.size();
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.DIFFICULTY_EMPTY.get();
	}

	@Override
	protected String newFileDefault() {
		return "l2hostility:new_difficulty";
	}

	@Override
	protected void openNew(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new DifficultyFileScreen(id, this));
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new DifficultyFileScreen(id, this));
	}

}