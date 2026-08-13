package dev.xkmc.l2hostility.editor.base;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TabButton extends Button {

	private static final WidgetSprites SPRITES = new WidgetSprites(
			ResourceLocation.withDefaultNamespace("widget/tab_selected"),
			ResourceLocation.withDefaultNamespace("widget/tab"),
			ResourceLocation.withDefaultNamespace("widget/tab_selected_highlighted"),
			ResourceLocation.withDefaultNamespace("widget/tab_highlighted")
	);

	private static final int SELECTED_OFFSET = 3;

	private final boolean selected;

	public TabButton(int x, int y, int width, int height, Component message, boolean selected, OnPress onPress) {
		super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
		this.selected = selected;
	}

	@Override
	protected void renderWidget(GuiGraphics g, int mx, int my, float partialTick) {
		RenderSystem.enableBlend();
		g.blitSprite(SPRITES.get(selected, isHoveredOrFocused()), getX(), getY(), getWidth(), getHeight());
		RenderSystem.disableBlend();
		var font = Minecraft.getInstance().font;
		int color = selected ? 0xFFFFFFFF : 0xFFA0A0A0;
		int top = getY() + (selected ? 0 : SELECTED_OFFSET);
		g.drawCenteredString(font, getMessage(), getX() + getWidth() / 2,
				top + (getY() + getHeight() - top - font.lineHeight) / 2, color);
		if (selected) {
			int w = Math.min(font.width(getMessage()), getWidth() - 4);
			int x = getX() + (getWidth() - w) / 2;
			int y = getY() + getHeight() - 2;
			g.fill(x, y, x + w, y + 1, color);
		}
	}

}
