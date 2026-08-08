package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.editor.base.EditorSaveState;
import dev.xkmc.l2hostility.editor.base.EditorTab;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.config.DifficultyFileScreen;
import dev.xkmc.l2hostility.editor.config.EntityFileScreen;
import dev.xkmc.l2hostility.editor.config.TraitFileScreen;
import dev.xkmc.l2hostility.editor.config.WeaponFileScreen;
import dev.xkmc.l2hostility.editor.tag.TagEditScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.editor.tag.HostilityTagUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HostilityHomeScreen extends dev.xkmc.l2hostility.editor.base.EditorHomeScreen {

	private final TabKind kind;

	public HostilityHomeScreen(TabKind kind, Screen parent) {
		super(kind.title(), parent);
		this.kind = kind;
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return switch (kind) {
			case DIFFICULTY -> idsOf(L2Hostility.DIFFICULTY.getAll());
			case TRAIT -> new ArrayList<>(LHTraits.TRAITS.get().getKeys());
			case WEAPON -> idsOf(L2Hostility.WEAPON.getAll());
			case ENTITY -> idsOf(L2Hostility.ENTITY.getAll());
			case TAGS -> HostilityEditorUtil.listManagedTags();
		};
	}

	private static List<ResourceLocation> idsOf(java.util.Collection<? extends dev.xkmc.l2library.serial.config.BaseConfig> all) {
		List<ResourceLocation> ids = new ArrayList<>();
		for (var cfg : all) {
			ResourceLocation id = cfg.getID();
			if (id != null) ids.add(id);
		}
		return ids;
	}

	@Override
	protected boolean hasSearch() {
		return switch (kind) {
			case TRAIT, ENTITY -> true;
			default -> false;
		};
	}

	@Override
	protected Component fileLabel(ResourceLocation id) {
		if (kind == TabKind.TRAIT) {
			var trait = LHTraits.TRAITS.get().getValue(id);
			if (trait != null) return trait.getDesc();
		}
		return super.fileLabel(id);
	}

	@Override
	protected boolean canCreate() {
		return switch (kind) {
			case DIFFICULTY, WEAPON, ENTITY -> true;
			case TRAIT, TAGS -> false;
		};
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		return switch (kind) {
			case DIFFICULTY -> {
				WorldDifficultyConfig cfg = L2Hostility.DIFFICULTY.getEntry(id);
				if (cfg == null) yield 0;
				yield cfg.levelMap.size() + cfg.biomeMap.size()
						+ cfg.levelDefaultTraits.size() + cfg.structureDefaultTraits.size();
			}
			case TRAIT -> 1;
			case WEAPON -> {
				WeaponConfig cfg = L2Hostility.WEAPON.getEntry(id);
				if (cfg == null) yield 0;
				yield cfg.melee_weapons.size() + cfg.armors.size() + cfg.ranged_weapons.size()
						+ cfg.special_weapons.size() + cfg.weapon_enchantments.size()
						+ cfg.armor_enchantments.size();
			}
			case ENTITY -> {
				EntityConfig cfg = L2Hostility.ENTITY.getEntry(id);
				yield cfg == null ? 0 : cfg.list.size();
			}
			case TAGS -> HostilityTagUtil.load(id).size();
		};
	}

	@Override
	protected Component emptyMessage() {
		return switch (kind) {
			case DIFFICULTY -> HostilityEditorLang.DIFFICULTY_EMPTY.get();
			case TRAIT -> HostilityEditorLang.TRAIT_EMPTY.get();
			case WEAPON -> HostilityEditorLang.WEAPON_EMPTY.get();
			case ENTITY -> HostilityEditorLang.ENTITY_EMPTY.get();
			case TAGS -> HostilityEditorLang.TAG_EMPTY.get();
		};
	}

	@Override
	protected String newFileDefault() {
		return switch (kind) {
			case DIFFICULTY -> "l2hostility:new_difficulty";
			case TRAIT, TAGS -> "l2hostility:new";
			case WEAPON -> "l2hostility:new_weapon";
			case ENTITY -> "l2hostility:new_entity";
		};
	}

	@Override
	protected void openNew(ResourceLocation id) {
		switch (kind) {
			case DIFFICULTY -> Minecraft.getInstance().setScreen(new DifficultyFileScreen(id, this));
			case WEAPON -> Minecraft.getInstance().setScreen(new WeaponFileScreen(id, this));
			case ENTITY -> Minecraft.getInstance().setScreen(new EntityFileScreen(id, this));
			case TRAIT, TAGS -> EditorToast.show(EditorText.NEW.get(), EditorText.NO_FILE.get());
		}
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		switch (kind) {
			case DIFFICULTY -> Minecraft.getInstance().setScreen(new DifficultyFileScreen(id, this));
			case TRAIT -> Minecraft.getInstance().setScreen(new TraitFileScreen(id, this));
			case WEAPON -> Minecraft.getInstance().setScreen(new WeaponFileScreen(id, this));
			case ENTITY -> Minecraft.getInstance().setScreen(new EntityFileScreen(id, this));
			case TAGS -> Minecraft.getInstance().setScreen(new TagEditScreen(id, this));
		}
	}

	@Override
	protected List<EditorTab> tabs() {
		List<EditorTab> ans = new ArrayList<>();
		for (TabKind k : TabKind.values()) {
			ans.add(new EditorTab(k.title(), () -> {
				if (k != kind) {
					Minecraft.getInstance().setScreen(new HostilityHomeScreen(k, parent));
				}
			}));
		}
		return ans;
	}

	@Override
	protected int activeTab() {
		return kind.ordinal();
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
