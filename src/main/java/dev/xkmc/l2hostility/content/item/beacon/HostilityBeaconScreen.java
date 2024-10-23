package dev.xkmc.l2hostility.content.item.beacon;

import com.google.common.collect.Lists;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class HostilityBeaconScreen extends AbstractContainerScreen<HostilityBeaconMenu> implements ContainerListener {
	static final ResourceLocation BEACON_LOCATION = L2Hostility.loc("textures/gui/container/beacon.png");
	private static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");
	private final List<HostilityBeaconScreen.BeaconButton> beaconButtons = Lists.newArrayList();

	int primary = -1;

	public HostilityBeaconScreen(final HostilityBeaconMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 230;
		this.imageHeight = 219;
		menu.addSlotListener(this);
	}

	public void slotChanged(AbstractContainerMenu self, int index, ItemStack stack) {
	}

	public void dataChanged(AbstractContainerMenu self, int index, int value) {
		primary = menu.getPrimaryEffect();
	}

	private <T extends AbstractWidget & HostilityBeaconScreen.BeaconButton> void addBeaconButton(T btn) {
		this.addRenderableWidget(btn);
		this.beaconButtons.add(btn);
	}

	protected void init() {
		super.init();
		this.beaconButtons.clear();
		this.addBeaconButton(new HostilityBeaconScreen.BeaconConfirmButton(this.leftPos + 164, this.topPos + 107));
		this.addBeaconButton(new HostilityBeaconScreen.BeaconCancelButton(this.leftPos + 190, this.topPos + 107));

		for (int i = 0; i < 3; ++i) {
			int n = 2;
			int w = n * 22 + (n - 1) * 2;
			for (int j = 0; j < n; ++j) {
				HostilityBeaconScreen.BeaconPowerButton btn = new HostilityBeaconScreen.BeaconPowerButton(
						this.leftPos + 76 + j * 24 - w / 2,
						this.topPos + 22 + i * 25,
						i * 2 + j, i);
				btn.active = false;
				this.addBeaconButton(btn);
			}
		}

	}

	public void containerTick() {
		super.containerTick();
		this.updateButtons();
	}

	void updateButtons() {
		int i = this.menu.getLevels();
		this.beaconButtons.forEach((p_169615_) -> {
			p_169615_.updateStatus(i);
		});
	}

	protected void renderLabels(GuiGraphics p_283369_, int p_282699_, int p_281296_) {
		p_283369_.drawCenteredString(this.font, PRIMARY_EFFECT_LABEL, 62, 10, 14737632);
	}

	protected void renderBg(GuiGraphics p_282454_, float p_282185_, int p_282362_, int p_282987_) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		p_282454_.blit(BEACON_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
		p_282454_.pose().pushPose();
		p_282454_.pose().translate(0.0F, 0.0F, 100.0F);
		p_282454_.renderItem(LHItems.CHAOS_INGOT.asStack(), i + 42 + 44, j + 109);
		p_282454_.renderItem(LHItems.MIRACLE_INGOT.asStack(), i + 42 + 66, j + 109);
		p_282454_.pose().popPose();
	}

	public void render(GuiGraphics p_283062_, int p_282876_, int p_282015_, float p_281395_) {
		super.render(p_283062_, p_282876_, p_282015_, p_281395_);
		this.renderTooltip(p_283062_, p_282876_, p_282015_);
	}

	@OnlyIn(Dist.CLIENT)
	interface BeaconButton {
		void updateStatus(int p_169631_);
	}

	@OnlyIn(Dist.CLIENT)
	class BeaconCancelButton extends HostilityBeaconScreen.BeaconSpriteScreenButton {
		public BeaconCancelButton(int p_97982_, int p_97983_) {
			super(p_97982_, p_97983_, 112, 220, CommonComponents.GUI_CANCEL);
		}

		public void onPress() {
			minecraft.player.closeContainer();
		}

		public void updateStatus(int p_169636_) {
		}
	}

	@OnlyIn(Dist.CLIENT)
	class BeaconConfirmButton extends HostilityBeaconScreen.BeaconSpriteScreenButton {
		public BeaconConfirmButton(int p_97992_, int p_97993_) {
			super(p_97992_, p_97993_, 90, 220, CommonComponents.GUI_DONE);
		}

		public void onPress() {
			click(primary);
			minecraft.player.closeContainer();
		}

		public void updateStatus(int p_169638_) {
			this.active = menu.hasPayment() && primary != -1;
		}
	}

	@OnlyIn(Dist.CLIENT)
	class BeaconPowerButton extends HostilityBeaconScreen.BeaconScreenButton {
		protected final int tier;
		private int effect;
		private TextureAtlasSprite sprite;

		public BeaconPowerButton(int x, int y, int eff, int tier) {
			super(x, y);
			this.tier = tier;
			this.setEffect(eff);
		}

		protected Holder<MobEffect> getEffect() {
			return HostilityBeaconBlockEntity.BEACON_EFFECTS.get(effect);
		}

		protected void setEffect(int eff) {
			this.effect = eff;
			this.sprite = Minecraft.getInstance().getMobEffectTextures().get(getEffect());
			this.setTooltip(Tooltip.create(this.createEffectDescription(getEffect().value()), null));
		}

		protected MutableComponent createEffectDescription(MobEffect eff) {
			return Component.translatable(eff.getDescriptionId());
		}

		public void onPress() {
			if (!this.isSelected()) {
				primary = this.effect;
				updateButtons();
			}
		}

		protected void renderIcon(GuiGraphics g) {
			g.blit(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
		}

		public void updateStatus(int lv) {
			this.active = this.tier < lv;
			this.setSelected(this.effect == primary);
		}

		protected MutableComponent createNarrationMessage() {
			return this.createEffectDescription(getEffect().value());
		}

	}

	@OnlyIn(Dist.CLIENT)
	abstract static class BeaconScreenButton extends AbstractButton implements HostilityBeaconScreen.BeaconButton {
		private boolean selected;

		protected BeaconScreenButton(int p_98022_, int p_98023_) {
			super(p_98022_, p_98023_, 22, 22, CommonComponents.EMPTY);
		}

		protected BeaconScreenButton(int p_169654_, int p_169655_, Component p_169656_) {
			super(p_169654_, p_169655_, 22, 22, p_169656_);
		}

		public void renderWidget(GuiGraphics p_281837_, int p_281780_, int p_283603_, float p_283562_) {
			int i = 219;
			int j = 0;
			if (!this.active) {
				j += this.width * 2;
			} else if (this.selected) {
				j += this.width * 1;
			} else if (this.isHoveredOrFocused()) {
				j += this.width * 3;
			}

			p_281837_.blit(HostilityBeaconScreen.BEACON_LOCATION, this.getX(), this.getY(), j, 219, this.width, this.height);
			this.renderIcon(p_281837_);
		}

		protected abstract void renderIcon(GuiGraphics p_283292_);

		public boolean isSelected() {
			return this.selected;
		}

		public void setSelected(boolean p_98032_) {
			this.selected = p_98032_;
		}

		public void updateWidgetNarration(NarrationElementOutput p_259705_) {
			this.defaultButtonNarrationText(p_259705_);
		}
	}

	@OnlyIn(Dist.CLIENT)
	abstract static class BeaconSpriteScreenButton extends HostilityBeaconScreen.BeaconScreenButton {
		private final int iconX;
		private final int iconY;

		protected BeaconSpriteScreenButton(int p_169663_, int p_169664_, int p_169665_, int p_169666_, Component p_169667_) {
			super(p_169663_, p_169664_, p_169667_);
			this.iconX = p_169665_;
			this.iconY = p_169666_;
		}

		protected void renderIcon(GuiGraphics p_283624_) {
			p_283624_.blit(HostilityBeaconScreen.BEACON_LOCATION, this.getX() + 2, this.getY() + 2, this.iconX, this.iconY, 18, 18);
		}
	}

	protected boolean click(int btn) {
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		if (this.menu.clickMenuButton(player, btn) && Minecraft.getInstance().gameMode != null) {
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, btn);
			return true;
		} else {
			return false;
		}
	}

}