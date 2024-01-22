package dev.xkmc.l2hostility.events;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

	@SubscribeEvent
	public static void addTooltip(ItemTooltipEvent event) {
		if (event.getEntity() == null) return;
		EnchantmentDisabler.modifyTooltip(event.getItemStack(), event.getToolTip(), event.getEntity().level);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void renderNamePlate(RenderNameTagEvent event) {
		if (event.getEntity() instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
			LocalPlayer player = Proxy.getClientPlayer();
			assert player != null;
			boolean needHover = le.isInvisible() || LHConfig.CLIENT.showOnlyWhenHovered.get();
			if (needHover && RayTraceUtil.rayTraceEntity(player, player.getAttackRange(), e -> e == le) == null) {
				return;
			}
			var list = MobTraitCap.HOLDER.get(le).getTitle(
					LHConfig.CLIENT.showLevelOverHead.get(),
					LHConfig.CLIENT.showTraitOverHead.get()
			);
			int offset = list.size();
			float off = (float) (double) LHConfig.CLIENT.overHeadRenderOffset.get();
			boolean indirect = !player.hasLineOfSight(event.getEntity());
			for (var e : list) {
				renderNameTag(event, e, event.getPoseStack(), (offset + off) * 0.2f, indirect);
				offset--;
			}
		}

	}

	protected static void renderNameTag(RenderNameTagEvent event, Component text, PoseStack pose, float offset, boolean indirect) {
		var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		double d0 = dispatcher.distanceToSqr(event.getEntity());
		int max = LHConfig.CLIENT.overHeadRenderDistance.get();
		int light = LHConfig.CLIENT.overHeadRenderFullBright.get() ? LightTexture.FULL_BRIGHT :
				event.getPackedLight();
		if (d0 < max * max) {
			float f = event.getEntity().getBbHeight() + 0.5f + offset;
			pose.pushPose();
			pose.translate(0.0F, f, 0.0F);
			pose.mulPose(dispatcher.cameraOrientation());
			pose.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = pose.last().pose();
			Font font = event.getEntityRenderer().getFont();
			float f2 = (float) (-font.width(text) / 2);
			float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
			int j = (int) (f1 * 255.0F) << 24;
			font.drawInBatch(text, f2, 0, -1, false, matrix4f,
					event.getMultiBufferSource(), indirect, j, light);
			pose.popPose();
		}
	}

}
