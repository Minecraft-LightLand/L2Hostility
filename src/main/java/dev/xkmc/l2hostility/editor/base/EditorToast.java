package dev.xkmc.l2hostility.editor.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class EditorToast {

	private static final SystemToast.SystemToastId ID = new SystemToast.SystemToastId();

	public static void show(Component title, Component message) {
		SystemToast.add(Minecraft.getInstance().getToasts(), ID, title, message);
	}

}
