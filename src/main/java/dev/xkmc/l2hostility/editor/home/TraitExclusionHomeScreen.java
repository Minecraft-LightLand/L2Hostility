package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.config.TraitExclusionGridScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Trait exclusion tab. Rows are traits, split into Active (takes part in an exclusion relation)
 * and Inactive (no relations). Opening any trait edits the grid of the whole relation group the
 * trait belongs to.
 */
public class TraitExclusionHomeScreen extends HostilityHomeScreen {

	private static final String ACTIVE = "active";
	private static final String INACTIVE = "inactive";

	private final Set<ResourceLocation> active = HostilityEditorUtil.activeExclusionIds();

	public TraitExclusionHomeScreen(Screen parent) {
		super(HostilityEditorLang.TRAIT_EXCLUSION.get(), 2, parent);
	}

	@Override
	protected boolean hasSearch() {
		return true;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		LinkedHashSet<ResourceLocation> ans = new LinkedHashSet<>(LHTraits.TRAITS.get().getKeys());
		for (var cfg : L2Hostility.TRAIT_EXCLUSION.getAll()) {
			ResourceLocation id = cfg.getID();
			if (id != null) ans.add(id);
		}
		return new ArrayList<>(ans);
	}

	@Override
	protected String groupOf(ResourceLocation id) {
		return active.contains(id) ? ACTIVE : INACTIVE;
	}

	@Override
	protected String groupName(String ns) {
		if (ACTIVE.equals(ns)) return HostilityEditorLang.ACTIVE.get().getString();
		if (INACTIVE.equals(ns)) return HostilityEditorLang.INACTIVE.get().getString();
		return super.groupName(ns);
	}

	@Override
	protected Component fileLabel(ResourceLocation id) {
		MobTrait trait = LHTraits.TRAITS.get().getValue(id);
		return trait != null ? trait.getDesc() : super.fileLabel(id);
	}

	@Override
	protected boolean canCreate() {
		return false;
	}

	@Override
	protected boolean hasNew() {
		return false;
	}

	@Override
	protected boolean isDisabled(ResourceLocation id) {
		MobTrait trait = LHTraits.TRAITS.get().getValue(id);
		return trait != null && trait.isBanned();
	}

	@Override
	@Nullable
	protected Component fileTooltip(ResourceLocation id) {
		MobTrait trait = LHTraits.TRAITS.get().getValue(id);
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
		return HostilityEditorUtil.groupOf(id).size();
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.TRAIT_EXCLUSION_EMPTY.get();
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
		Minecraft.getInstance().setScreen(new TraitExclusionGridScreen(id, this));
	}

}