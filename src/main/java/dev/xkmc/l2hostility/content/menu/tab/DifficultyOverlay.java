package dev.xkmc.l2hostility.content.menu.tab;

import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.base.overlay.InfoSideBar;
import dev.xkmc.l2library.base.overlay.SideBar;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class DifficultyOverlay extends InfoSideBar<SideBar.IntSignature> {

	public DifficultyOverlay() {
		super(40, 3);
	}

	@Override
	protected List<Component> getText() {
		List<Component> comp = new ArrayList<>();
		DifficultyScreen.addDifficultyInfo(comp, ChatFormatting.RED, ChatFormatting.GREEN);
		return comp;
	}

	@Override
	protected boolean isOnHold() {
		return true;
	}

	@Override
	public IntSignature getSignature() {
		return new IntSignature(0);
	}

	@Override
	public boolean isScreenOn() {
		if (Minecraft.getInstance().screen != null) return false;
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return false;
		return player.getMainHandItem().is(LHItems.DETECTOR.get()) ||
				player.getOffhandItem().is(LHItems.DETECTOR.get());
	}

}
