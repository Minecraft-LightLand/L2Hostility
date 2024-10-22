package dev.xkmc.l2hostility.content.menu.tab;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2itemselector.overlay.InfoSideBar;
import dev.xkmc.l2itemselector.overlay.SideBar;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DifficultyOverlay extends InfoSideBar<SideBar.IntSignature> {

	public DifficultyOverlay() {
		super(40, 3);
	}

	@Override
	protected List<Component> getText() {
		List<Pair<Component, Supplier<List<Component>>>> comp = new ArrayList<>();
		DifficultyScreen.addDifficultyInfo(comp,
				ChatFormatting.RED, ChatFormatting.GREEN, ChatFormatting.GOLD);
		return comp.stream().map(Pair::getFirst).toList();
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
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return false;
		return player.getMainHandItem().is(LHItems.DETECTOR.get()) ||
				player.getOffhandItem().is(LHItems.DETECTOR.get());
	}

}
