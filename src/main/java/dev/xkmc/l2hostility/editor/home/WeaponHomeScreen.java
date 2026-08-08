package dev.xkmc.l2hostility.editor.home;

import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.editor.config.WeaponFileScreen;
import dev.xkmc.l2hostility.editor.util.HostilityEditorLang;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class WeaponHomeScreen extends HostilityHomeScreen {

	public WeaponHomeScreen(Screen parent) {
		super(HostilityEditorLang.WEAPON.get(), 2, parent);
	}

	@Override
	protected boolean featureDisabled() {
		return !LHConfig.COMMON.enableEquipmentDatapack.get();
	}

	@Override
	protected List<ResourceLocation> listFiles() {
		return idsOf(L2Hostility.WEAPON.getAll());
	}

	@Override
	protected int fileCount(ResourceLocation id) {
		WeaponConfig cfg = L2Hostility.WEAPON.getEntry(id);
		if (cfg == null) return 0;
		return cfg.melee_weapons.size() + cfg.armors.size() + cfg.ranged_weapons.size()
				+ cfg.special_weapons.size() + cfg.weapon_enchantments.size()
				+ cfg.armor_enchantments.size();
	}

	@Override
	protected Component emptyMessage() {
		return HostilityEditorLang.WEAPON_EMPTY.get();
	}

	@Override
	protected String newFileDefault() {
		return "l2hostility:new_weapon";
	}

	@Override
	protected void openNew(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new WeaponFileScreen(id, this));
	}

	@Override
	protected void openEdit(ResourceLocation id) {
		Minecraft.getInstance().setScreen(new WeaponFileScreen(id, this));
	}

}