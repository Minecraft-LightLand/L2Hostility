package dev.xkmc.l2hostility.editor.config;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.editor.base.EditorList;
import dev.xkmc.l2hostility.editor.base.EditorText;
import dev.xkmc.l2hostility.editor.base.EditorToast;
import dev.xkmc.l2hostility.editor.base.EditorUtil;
import dev.xkmc.l2hostility.editor.util.HostilityEditorForms;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.editor.util.HostilityEditorUtil;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class WeaponFileScreen extends HostilityFileScreen {

	private final WeaponConfig config;

	public WeaponFileScreen(ResourceLocation id, Screen parent) {
		super(HostilityEditorLang.WEAPON_FILE.get(), id, parent);
		WeaponConfig base = L2Hostility.WEAPON.getEntry(id);
		WeaponConfig copy = base == null ? null : EditorUtil.copy(L2Hostility.WEAPON, base);
		this.config = copy == null ? HostilityEditorUtil.newWeapon() : copy;
	}

	@Override
	protected void rebuild() {
		List<EditorList.Entry> entries = new ArrayList<>();
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.MELEE_WEAPONS.get(), config.melee_weapons.size()), null,
				() -> Minecraft.getInstance().setScreen(new ItemConfigListScreen(HostilityEditorLang.MELEE_WEAPONS.get(),
						config.melee_weapons, WeaponFileScreen.this, session)), config.melee_weapons.isEmpty(),
				HostilityEditorLang.ROW_MELEE_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.RANGED_WEAPONS.get(), config.ranged_weapons.size()), null,
				() -> Minecraft.getInstance().setScreen(new ItemConfigListScreen(HostilityEditorLang.RANGED_WEAPONS.get(),
						config.ranged_weapons, WeaponFileScreen.this, session)), config.ranged_weapons.isEmpty(),
				HostilityEditorLang.ROW_RANGED_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.ARMORS.get(), config.armors.size()), null,
				() -> Minecraft.getInstance().setScreen(new ItemConfigListScreen(HostilityEditorLang.ARMORS.get(),
						config.armors, WeaponFileScreen.this, session)), config.armors.isEmpty(),
				HostilityEditorLang.ROW_ARMOR_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.SPECIAL_WEAPONS.get(), config.special_weapons.size()), null,
				() -> Minecraft.getInstance().setScreen(new SpecialWeaponListScreen(config, WeaponFileScreen.this, session)), config.special_weapons.isEmpty(),
				HostilityEditorLang.ROW_SPECIAL_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.WEAPON_ENCHANTMENTS.get(), config.weapon_enchantments.size()), null,
				() -> Minecraft.getInstance().setScreen(new EnchConfigListScreen(HostilityEditorLang.WEAPON_ENCHANTMENTS.get(),
						config.weapon_enchantments, WeaponFileScreen.this, session)), config.weapon_enchantments.isEmpty(),
				HostilityEditorLang.ROW_WEAPON_ENCH_TIP.get()));
		entries.add(new EditorList.Entry(HostilityEditorForms.counted(HostilityEditorLang.ARMOR_ENCHANTMENTS.get(), config.armor_enchantments.size()), null,
				() -> Minecraft.getInstance().setScreen(new EnchConfigListScreen(HostilityEditorLang.ARMOR_ENCHANTMENTS.get(),
						config.armor_enchantments, WeaponFileScreen.this, session)), config.armor_enchantments.isEmpty(),
				HostilityEditorLang.ROW_ARMOR_ENCH_TIP.get()));
		list.setData(entries);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.render(g, mx, my, pTick);
		list.renderRowTooltip(g);
	}

	@Override
	protected boolean doSave() {
		try {
			HostilityEditorUtil.saveWeapon(fileId, config);
			saveDone(fileId);
			return true;
		} catch (Exception e) {
			EditorToast.show(EditorText.SAVE_FAIL.get(e.getMessage()), EditorText.NOT_IN_WORLD.get());
			return false;
		}
	}

}
