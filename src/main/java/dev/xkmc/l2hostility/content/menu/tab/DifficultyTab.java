package dev.xkmc.l2hostility.content.menu.tab;

import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class DifficultyTab extends TabBase<InvTabData, DifficultyTab> {
	
	public DifficultyTab(int index, TabToken<InvTabData, DifficultyTab> token, TabManager<InvTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		Minecraft.getInstance().setScreen(new DifficultyScreen(this.getMessage()));
	}

}
