package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.config.TraitFileScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TraitHomeScreen extends HostilityHomeScreen {

	public TraitHomeScreen(Screen parent) {
		super(HostilityEditorLang.TRAIT.get(), 1, parent);
	}

	@Override
	protected boolean hasSearch() {
		return true;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return new ArrayList<>(LHTraits.TRAITS.get().getKeys());
	}

	@Override
	protected Component fileLabel(ResourceLocation id) {
		var trait = LHTraits.TRAITS.get().getValue(id);
		return trait != null ? trait.getDesc() : super.fileLabel(id);
	}

	@Override
	protected boolean canCreate() {
		return false;
	}

	@Override
	protected boolean isDisabled(ResourceLocation id) {
		var trait = LHTraits.TRAITS.get().getValue(id);
		return trait != null && trait.isBanned();
	}

	@Override
	@Nullable
	protected Component fileTooltip(ResourceLocation id) {
		var trait = LHTraits.TRAITS.get().getValue(id);
		if (trait == null) return null;
		if (trait instanceof LegendaryTrait && !LHConfig.COMMON.allowLegendary.get()) {
			return HostilityEditorLang.TRAIT_DISABLED_LEGENDARY.get();
		}
		if (trait.isBanned()) {
			return HostilityEditorLang.TRAIT_DISABLED_TOGGLE.get();
		}
		return null;
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		return 1;
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.TRAIT_EMPTY.get();
	}

	@Override
	protected String newFileDefault() {
		return "l2hostility:new";
	}

	@Override
	protected void openNew(ResourceLocation id) {
		EditorToast.show(EditorText.NEW.get(), EditorText.NO_FILE.get());
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new TraitFileScreen(id, this));
	}

}