package dev.xkmc.l2hostility.events;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

	@SubscribeEvent
	public static void addTooltip(ItemTooltipEvent event) {
		if (event.getEntity() == null) return;
		EnchantmentDisabler.modifyTooltip(event.getItemStack(), event.getToolTip(), event.getEntity().level());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void renderNamePlate(RenderNameTagEvent event) {
		if (event.getEntity() instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
			LocalPlayer player = Proxy.getClientPlayer();
			assert player != null;
			if (le.isInvisible() && RayTraceUtil.rayTraceEntity(player, player.getEntityReach(), e -> e == le) == null) {
				return;
			}
			var list = MobTraitCap.HOLDER.get(le).getTitle(LHConfig.CLIENT.showAsNamePlate.get());
			int offset = list.size();
			Font.DisplayMode mode = player.hasLineOfSight(event.getEntity()) ?
					Font.DisplayMode.SEE_THROUGH :
					Font.DisplayMode.NORMAL;
			for (var e : list) {
				renderNameTag(event, e, event.getPoseStack(), offset * 0.2f, mode);
				offset--;
			}
		}

	}

	protected static void renderNameTag(RenderNameTagEvent event, Component text, PoseStack pose, float offset, Font.DisplayMode mode) {
		var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		double d0 = dispatcher.distanceToSqr(event.getEntity());
		if (ForgeHooksClient.isNameplateInRenderDistance(event.getEntity(), d0)) {
			float f = event.getEntity().getNameTagOffsetY() + offset;
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
					event.getMultiBufferSource(), mode, j, event.getPackedLight());
			pose.popPose();
		}
	}

}
