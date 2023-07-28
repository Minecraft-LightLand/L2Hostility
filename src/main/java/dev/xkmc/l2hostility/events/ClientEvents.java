package dev.xkmc.l2hostility.events;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2hostility.content.capability.mob.MobModifierCap;
import dev.xkmc.l2hostility.content.item.modifiers.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
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
	public static void addTooltip(ItemTooltipEvent event){
		EnchantmentDisabler.modifyTooltip(event.getItemStack(), event.getToolTip(), event.getEntity().level());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void renderNamePlate(RenderNameTagEvent event) {
		if (event.getEntity() instanceof LivingEntity le && MobModifierCap.HOLDER.isProper(le)) {
			renderNameTag(event.getEntityRenderer(),
					Wrappers.cast(le),
					MobModifierCap.HOLDER.get(le).getTitle(),
					event.getPoseStack(),
					event.getMultiBufferSource(),
					event.getPackedLight());
		} ;
	}

	protected static <T extends Entity> void renderNameTag(EntityRenderer<T> renderer, T entity, Component text, PoseStack pose, MultiBufferSource source, int light) {
		var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		double d0 = dispatcher.distanceToSqr(entity);
		if (ForgeHooksClient.isNameplateInRenderDistance(entity, d0)) {
			float f = entity.getNameTagOffsetY();
			pose.pushPose();
			pose.translate(0.0F, f, 0.0F);
			pose.mulPose(dispatcher.cameraOrientation());
			pose.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = pose.last().pose();
			Font font = renderer.getFont();
			float f2 = (float) (-font.width(text) / 2);
			font.drawInBatch(text, f2, 0, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, light);
			pose.popPose();
		}
	}

}
