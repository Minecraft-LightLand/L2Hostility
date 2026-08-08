package dev.xkmc.l2hostility.editor.tag;

import com.google.gson.JsonArray;
import dev.xkmc.l2hostility.editor.base.TagFile;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class HostilityTagUtil {

	private HostilityTagUtil() {
	}

	/**
	 * Merges the raw values for a tag across the singleplayer server's selected packs, in
	 * selection order, applying {@code replace} semantics. Values stay in raw form.
	 */
	public static List<TagValue> load(ResourceLocation tagId) {
		List<TagValue> values = new ArrayList<>();
		IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
		if (server == null) return values;
		for (var pack : server.getPackRepository().getSelectedPacks()) {
			try (PackResources res = pack.open()) {
				TagFile.Loaded loaded = TagFile.readAll(res, tagId);
				if (loaded == null) continue;
				if (loaded.replace()) values.clear();
				for (var e : loaded.values()) {
					values.add(TagValue.parse(e));
				}
			} catch (Exception e) {
				// skip unreadable pack
			}
		}
		return values;
	}

	public static Path save(ResourceLocation tagId, List<TagValue> values) throws IOException {
		JsonArray arr = new JsonArray();
		for (TagValue v : values) arr.add(v.toJson());
		return TagFile.save(tagId, arr, HostilityEditorUtil.PACK_FOLDER);
	}

}
