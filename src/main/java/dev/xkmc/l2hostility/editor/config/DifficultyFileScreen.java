package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.FormScreen;
import dev.xkmc.l2hostility.editor.base.ValueMapScreen;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DifficultyFileScreen extends HostilityFileScreen {

	private final WorldDifficultyConfig config;

	public DifficultyFileScreen(ResourceLocation id, Screen parent) {
		super(HostilityEditorLang.DIFFICULTY_FILE.get(), id, parent);
		WorldDifficultyConfig base = L2Hostility.DIFFICULTY.getEntry(id);
		WorldDifficultyConfig copy = base == null ? null : dev.xkmc.l2hostility.editor.base.EditorUtil.copy(L2Hostility.DIFFICULTY, base);
		this.config = copy == null ? HostilityEditorUtil.newDifficulty() : copy;
	}

	@Override
	protected void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.DIMENSIONS.get(), config.levelMap.size()), null,
				() -> Minecraft.getInstance().setScreen(new ValueMapScreen<>(HostilityEditorLang.DIMENSIONS.get(),
						config.levelMap, () -> config.levelMap, new DimHandler(), DifficultyFileScreen.this, session))));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.BIOMES.get(), config.biomeMap.size()), null,
				() -> Minecraft.getInstance().setScreen(new ValueMapScreen<>(HostilityEditorLang.BIOMES.get(),
						config.biomeMap, () -> config.biomeMap, new BiomeHandler(), DifficultyFileScreen.this, session))));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.LEVEL_DEFAULT_TRAITS.get(), config.levelDefaultTraits.size()), null,
				() -> Minecraft.getInstance().setScreen(new ValueMapScreen<>(HostilityEditorLang.LEVEL_DEFAULT_TRAITS.get(),
						config.levelDefaultTraits, () -> config.levelDefaultTraits,
						new ConfigListHandler(session, false), DifficultyFileScreen.this, session))));
		boolean structures = HostilityEditorUtil.hasStructureRegistry();
		Component structureLabel = HostilityEditorForms.counted(HostilityEditorLang.STRUCTURE_DEFAULT_TRAITS.get(), config.structureDefaultTraits.size())
				.copy().append(structures ? Component.empty() : HostilityEditorLang.STRUCTURE_SERVER_HINT.get());
		entries.add(new EditorList.Entry(structureLabel, null,
				structures ? () -> Minecraft.getInstance().setScreen(new ValueMapScreen<>(HostilityEditorLang.STRUCTURE_DEFAULT_TRAITS.get(),
						config.structureDefaultTraits, () -> config.structureDefaultTraits,
						new ConfigListHandler(session, true), DifficultyFileScreen.this, session)) : null,
				!structures));
		list.setData(entries);
	}

	@Override
	protected boolean doSave() {
		try {
			HostilityEditorUtil.saveDifficulty(fileId, config);
			saveDone(fileId);
			return true;
		} catch (Exception e) {
			dev.xkmc.l2hostility.editor.base.EditorToast.show(
					HostilityEditorLang.INVALID_INTEGER.get(e.getMessage()), dev.xkmc.l2hostility.editor.base.EditorText.NOT_IN_WORLD.get());
			return false;
		}
	}

	private record DimHandler() implements ValueMapScreen.Handler<ResourceLocation, WorldDifficultyConfig.DifficultyConfig> {

		@Override
		public Component keyLabel(ResourceLocation k) {
			return Component.literal(k.toString());
		}

		@Override
		@Nullable
		public ItemStack keyIcon(ResourceLocation k) {
			return null;
		}

		@Override
		public List<ResourceLocation> allKeys() {
			return List.of();
		}

		@Override
		public String keyDefault() {
			return "minecraft:overworld";
		}

		@Override
		@Nullable
		public Function<String, Component> keyValidate() {
			return HostilityEditorUtil::validateFileId;
		}

		@Override
		public Component valueSummary(WorldDifficultyConfig.DifficultyConfig v) {
			return HostilityEditorForms.difficultySummary(v);
		}

		@Override
		public void openValue(Screen parent, @Nullable WorldDifficultyConfig.DifficultyConfig current, Consumer<WorldDifficultyConfig.DifficultyConfig> onDone) {
			WorldDifficultyConfig.DifficultyConfig base = current == null
					? new WorldDifficultyConfig.DifficultyConfig(0, 0, 0, 0, 1, 1, 0) : current;
			Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.DIFFICULTY.get(),
					HostilityEditorForms.difficultyConfig(base), onDone, parent));
		}

	}

	private record BiomeHandler() implements ValueMapScreen.Handler<ResourceLocation, WorldDifficultyConfig.DifficultyConfig> {

		@Override
		public Component keyLabel(ResourceLocation k) {
			return Component.translatable("biome." + k.getNamespace() + "." + k.getPath());
		}

		@Override
		@Nullable
		public ItemStack keyIcon(ResourceLocation k) {
			return null;
		}

		@Override
		public List<ResourceLocation> allKeys() {
			return HostilityEditorUtil.listBiomes().stream().map(e -> e.location()).toList();
		}

		@Override
		public String keyDefault() {
			return "minecraft:plains";
		}

		@Override
		@Nullable
		public Function<String, Component> keyValidate() {
			return null;
		}

		@Override
		public Component valueSummary(WorldDifficultyConfig.DifficultyConfig v) {
			return HostilityEditorForms.difficultySummary(v);
		}

		@Override
		public void openValue(Screen parent, @Nullable WorldDifficultyConfig.DifficultyConfig current, Consumer<WorldDifficultyConfig.DifficultyConfig> onDone) {
			WorldDifficultyConfig.DifficultyConfig base = current == null
					? new WorldDifficultyConfig.DifficultyConfig(0, 0, 0, 0, 1, 1, 0) : current;
			Minecraft.getInstance().setScreen(new FormScreen<>(HostilityEditorLang.DIFFICULTY.get(),
					HostilityEditorForms.difficultyConfig(base), onDone, parent));
		}

	}

	/**
	 * Value editor for the difficulty default-traits maps: value = list of Configs (empty
	 * entities = "all entities" fallback). Structure variant picks keys from the structure list.
	 */
	private record ConfigListHandler(dev.xkmc.l2hostility.editor.base.EditorSession session, boolean structure)
			implements ValueMapScreen.Handler<ResourceLocation, ArrayList<EntityConfig.Config>> {

		@Override
		public Component keyLabel(ResourceLocation k) {
			return Component.literal(k.toString());
		}

		@Override
		@Nullable
		public ItemStack keyIcon(ResourceLocation k) {
			return null;
		}

		@Override
		public List<ResourceLocation> allKeys() {
			return structure
					? HostilityEditorUtil.listStructures().stream().map(e -> e.location()).toList()
					: List.of();
		}

		@Override
		public String keyDefault() {
			return structure ? "minecraft:stronghold" : "minecraft:overworld";
		}

		@Override
		@Nullable
		public Function<String, Component> keyValidate() {
			return structure ? null : HostilityEditorUtil::validateFileId;
		}

		@Override
		public Component valueSummary(ArrayList<EntityConfig.Config> v) {
			return HostilityEditorLang.SUMMARY_CONFIGS.get(v.size());
		}

		@Override
		public void openValue(Screen parent, @Nullable ArrayList<EntityConfig.Config> current, Consumer<ArrayList<EntityConfig.Config>> onDone) {
			ArrayList<EntityConfig.Config> list = current == null ? new ArrayList<>() : current;
			if (current == null) onDone.accept(list);
			Minecraft.getInstance().setScreen(new ConfigListScreen(HostilityEditorLang.CONFIG_LIST.get(), list, false, parent, session));
		}

	}

}
