package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.TraitExclusion;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.ExclusionGridScreen;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Group editor for trait exclusion. Edits the whole relation group the entered trait belongs to;
 * the working data is a {@code Map<carrier, Map<target, factor>>} built from the loaded per-trait
 * configs. Saving writes one {@code trait_exclusion/<carrier>.json} per carrier with entries and
 * deletes the editor-pack file for carriers that ended up with none.
 */
public class TraitExclusionGridScreen extends HostilityFileScreen {

	private final List<ResourceLocation> members;
	private final LinkedHashMap<ResourceLocation, Map<ResourceLocation, Double>> map = new LinkedHashMap<>();

	public TraitExclusionGridScreen(ResourceLocation trait, Screen parent) {
		super(HostilityEditorLang.TRAIT_EXCLUSION_FILE.get(), trait, parent);
		List<ResourceLocation> group = HostilityEditorUtil.groupOf(trait);
		members = new ArrayList<>(group.isEmpty() ? List.of(trait) : group);
		for (ResourceLocation id : members) {
			TraitExclusion te = L2Hostility.TRAIT_EXCLUSION.getEntry(id);
			LinkedHashMap<ResourceLocation, Double> sub = new LinkedHashMap<>();
			if (te != null) sub.putAll(te.getExcluded());
			map.put(id, sub);
		}
	}

	private final ExclusionGridScreen.Handler<ResourceLocation> handler = new ExclusionGridScreen.Handler<>() {

		@Override
		public Component label(ResourceLocation id) {
			return HostilityEditorUtil.traitIdName(id);
		}

		@Override
		@Nullable
		public ItemStack icon(ResourceLocation id) {
			return HostilityEditorUtil.traitIdIcon(id);
		}

		@Override
		public Component cellLabel(ResourceLocation a, ResourceLocation b) {
			return HostilityEditorLang.EXCLUDES_TITLE.get(HostilityEditorUtil.traitIdName(a), HostilityEditorUtil.traitIdName(b));
		}

		@Override
		public List<ResourceLocation> onAdd(ResourceLocation picked) {
			List<ResourceLocation> cluster = HostilityEditorUtil.groupOf(picked);
			for (ResourceLocation id : cluster) {
				if (!members.contains(id)) members.add(id);
				if (!map.containsKey(id)) {
					TraitExclusion te = L2Hostility.TRAIT_EXCLUSION.getEntry(id);
					LinkedHashMap<ResourceLocation, Double> sub = new LinkedHashMap<>();
					if (te != null) sub.putAll(te.getExcluded());
					map.put(id, sub);
				}
			}
			return new ArrayList<>(members);
		}

	};

	@Override
	protected void rebuild() {
		int links = 0;
		for (Map<ResourceLocation, Double> sub : map.values()) links += sub.size();
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.EXCLUDED_GRID.get(), links),
				null, () -> Minecraft.getInstance().setScreen(new ExclusionGridScreen<ResourceLocation>(
						HostilityEditorLang.EXCLUDED_GRID.get(), HostilityEditorLang.GRID_HINT.get(),
						HostilityEditorLang.CLEAR_CELL.get(), map, members,
						HostilityEditorUtil.listTraitIds(), handler, TraitExclusionGridScreen.this, session)),
				false, HostilityEditorLang.ROW_GRID_TIP.get()));
		list.setData(entries);
	}

	@Override
	protected void save() {
		if (batchSave()) {
			saveDone(fileId);
			Minecraft.getInstance().setScreen(this);
		}
	}

	@Override
	protected boolean doSave() {
		return batchSave();
	}

	/**
	 * Writes a config per carrier with entries and deletes the editor file for carriers that now
	 * have none (restoring the mod's built-in defaults).
	 */
	private boolean batchSave() {
		try {
			for (ResourceLocation id : members) {
				Map<ResourceLocation, Double> sub = map.get(id);
				if (sub == null || sub.isEmpty()) {
					HostilityEditorUtil.deleteTraitExclusion(id);
				} else {
					TraitExclusion cfg = HostilityEditorUtil.newTraitExclusion();
					cfg.excluded.putAll(sub);
					HostilityEditorUtil.saveTraitExclusion(id, cfg);
				}
			}
			return true;
		} catch (IOException e) {
			EditorToast.show(EditorText.SAVE_FAIL.get(e.getMessage()), EditorText.NOT_IN_WORLD.get());
			return false;
		}
	}

}