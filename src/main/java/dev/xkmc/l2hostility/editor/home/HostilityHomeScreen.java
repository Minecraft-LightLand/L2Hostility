package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.editor.base.*;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2library.serial.config.BaseConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Base tab of the editor home screen. Each concrete tab is its own subclass created from the
 * {@link #TABS} list, so the tab order and wiring lives here while per-tab behavior is inherited.
 */
public abstract class HostilityHomeScreen extends EditorHomeScreen {

	private final int index;

	protected HostilityHomeScreen(Component title, int index, Screen parent) {
		super(title, parent);
		this.index = index;
	}

	private static final List<Function<Screen, HostilityHomeScreen>> TABS = List.of(
			DifficultyHomeScreen::new,
			TraitHomeScreen::new,
			WeaponHomeScreen::new,
			EntityHomeScreen::new,
			TagsHomeScreen::new,
			ConfigHomeScreen::new);

	protected static List<ResourceLocation> idsOf(Collection<? extends BaseConfig> all) {
		List<ResourceLocation> ids = new ArrayList<>();
		for (var cfg : all) {
			ResourceLocation id = cfg.getID();
			if (id != null) ids.add(id);
		}
		return ids;
	}

	/**
	 * Whether this tab is shown red and strikethrough because its datapack feature is disabled by
	 * config.
	 */
	protected boolean featureDisabled() {
		return false;
	}

	@Override
	protected List<EditorTab> tabs() {
		List<EditorTab> ans = new ArrayList<>();
		for (int i = 0; i < TABS.size(); i++) {
			HostilityHomeScreen tab = TABS.get(i).apply(parent);
			Component label = tab.title;
			if (tab.featureDisabled()) {
				label = label.copy().withStyle(ChatFormatting.RED, ChatFormatting.STRIKETHROUGH);
			}
			int idx = i;
			ans.add(new EditorTab(label, () -> {
				if (idx != index) {
					Minecraft.getInstance().setScreen(TABS.get(idx).apply(parent));
				}
			}));
		}
		return ans;
	}

	@Override
	protected int activeTab() {
		return index;
	}

	@Override
	protected Component fileIdLabel() {
		return EditorText.FILE_ID.get();
	}

	@Override
	protected Function<String, Component> validateId() {
		return HostilityEditorUtil::validateFileId;
	}

	@Override
	protected boolean hasPendingReload() {
		return EditorSaveState.savedFlag;
	}

	@Override
	protected void setReloaded() {
		EditorSaveState.savedFlag = false;
	}

}