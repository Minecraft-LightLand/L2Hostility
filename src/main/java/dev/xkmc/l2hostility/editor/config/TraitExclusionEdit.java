package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.TraitExclusion;
import dev.xkmc.l2hostility.editor.base.EditorSaveState;
import dev.xkmc.l2hostility.editor.base.EditorSession;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.ExclusionGridScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Opens the trait exclusion grid for the whole relation group a trait belongs to, wiring the
 * generic {@link ExclusionGridScreen} to L2Hostility data. Saving distributes each relation to
 * the member with more exclusion entries (alphabetically higher member on ties) and writes a
 * file for every group member - including empty ones - so the editor pack replaces built-in
 * datapack defaults instead of deleting back to them.
 */
public final class TraitExclusionEdit {

	private TraitExclusionEdit() {
	}

	public static Screen create(ResourceLocation trait, Screen parent) {
		List<ResourceLocation> group = HostilityEditorUtil.groupOf(trait);
		List<ResourceLocation> members = new ArrayList<>(group.isEmpty() ? List.of(trait) : group);
		LinkedHashMap<ResourceLocation, Map<ResourceLocation, Double>> map = new LinkedHashMap<>();
		for (ResourceLocation id : members) {
			map.put(id, exclusionOf(id));
		}
		return new ExclusionGridScreen<>(HostilityEditorLang.EXCLUDED_GRID.get(), EditorText.FILE.get(trait),
				HostilityEditorLang.GRID_HINT.get(), HostilityEditorLang.CLEAR_CELL.get(), map, members,
				HostilityEditorUtil.listTraitIds(), new Handler(members, map),
				new Saver(trait, map, members), parent, new EditorSession());
	}

	private static LinkedHashMap<ResourceLocation, Double> exclusionOf(ResourceLocation id) {
		TraitExclusion te = L2Hostility.TRAIT_EXCLUSION.getEntry(id);
		LinkedHashMap<ResourceLocation, Double> sub = new LinkedHashMap<>();
		if (te != null) sub.putAll(te.getExcluded());
		return sub;
	}

	private record Handler(List<ResourceLocation> members,
	                       Map<ResourceLocation, Map<ResourceLocation, Double>> map)
			implements ExclusionGridScreen.Handler<ResourceLocation> {

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
				map.computeIfAbsent(id, TraitExclusionEdit::exclusionOf);
			}
			return new ArrayList<>(members);
		}

	}

	/**
	 * Saves one config per group member. Each relation is stored under whichever of its two traits
	 * carries more exclusion entries; ties go to the alphabetically higher id. Members without any
	 * relation get an explicit empty file overriding the built-in JSON.
	 */
	private record Saver(ResourceLocation trait,
	                     Map<ResourceLocation, Map<ResourceLocation, Double>> map,
	                     List<ResourceLocation> members) implements ExclusionGridScreen.Saver {

		@Override
		public boolean save() {
			try {
				Map<ResourceLocation, Map<ResourceLocation, Double>> files = allocate();
				for (var ent : files.entrySet()) {
					TraitExclusion cfg = HostilityEditorUtil.newTraitExclusion();
					cfg.excluded.putAll(ent.getValue());
					HostilityEditorUtil.saveTraitExclusion(ent.getKey(), cfg);
				}
				EditorSaveState.savedFlag = true;
				EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_DONE.get(trait));
				EditorToast.show(EditorText.SAVE.get(), EditorText.SAVE_NOTE.get());
				return true;
			} catch (IOException e) {
				EditorToast.show(EditorText.SAVE_FAIL.get(e.getMessage()), EditorText.NOT_IN_WORLD.get());
				return false;
			}
		}

		private Map<ResourceLocation, Map<ResourceLocation, Double>> allocate() {
			Map<ResourceLocation, Integer> counts = new HashMap<>();
			for (ResourceLocation m : members) {
				counts.put(m, map.getOrDefault(m, Map.of()).size());
			}
			Map<ResourceLocation, Map<ResourceLocation, Double>> files = new LinkedHashMap<>();
			for (ResourceLocation src : members) {
				for (var ent : map.getOrDefault(src, Map.of()).entrySet()) {
					ResourceLocation dst = ent.getKey();
					ResourceLocation carrier = pick(src, dst, counts);
					ResourceLocation key = carrier.equals(src) ? dst : src;
					files.computeIfAbsent(carrier, k -> new LinkedHashMap<>())
							.merge(key, ent.getValue(), (a, b) -> a * b);
				}
			}
			for (ResourceLocation m : members) {
				files.putIfAbsent(m, new LinkedHashMap<>());
			}
			return files;
		}

		private static ResourceLocation pick(ResourceLocation a, ResourceLocation b, Map<ResourceLocation, Integer> counts) {
			int ca = counts.getOrDefault(a, 0);
			int cb = counts.getOrDefault(b, 0);
			if (ca != cb) return ca > cb ? a : b;
			return a.compareTo(b) > 0 ? a : b;
		}

	}

}
