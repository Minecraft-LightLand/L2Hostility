package dev.xkmc.l2hostility.events;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkClearRenderer;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.ClientEffectRenderEvents;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
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
			var cap = MobTraitCap.HOLDER.get(le);
			boolean needHover = le.isInvisible() || LHConfig.CLIENT.showOnlyWhenHovered.get();
			if (needHover && RayTraceUtil.rayTraceEntity(player, player.getEntityReach(), e -> e == le) == null) {
				return;
			}
			var list = cap.getTitle(
					LHConfig.CLIENT.showLevelOverHead.get(),
					LHConfig.CLIENT.showTraitOverHead.get()
			);
			int offset = list.size();
			float off = (float) (double) LHConfig.CLIENT.overHeadRenderOffset.get();
			Font.DisplayMode mode = player.hasLineOfSight(event.getEntity()) ?
					Font.DisplayMode.SEE_THROUGH :
					Font.DisplayMode.NORMAL;
			for (var e : list) {
				renderNameTag(event, e, event.getPoseStack(), (offset + off) * 0.2f, mode);
				offset--;
			}
		}

	}

	protected static void renderNameTag(RenderNameTagEvent event, Component text, PoseStack pose, float offset, Font.DisplayMode mode) {
		var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		double d0 = dispatcher.distanceToSqr(event.getEntity());
		int max = LHConfig.CLIENT.overHeadRenderDistance.get();
		int light = LHConfig.CLIENT.overHeadRenderFullBright.get() ? LightTexture.FULL_BRIGHT :
				event.getPackedLight();
		if (d0 < max * max) {
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
					event.getMultiBufferSource(), mode, j, light);
			pose.popPose();
		}
	}

	@SubscribeEvent
	public static void onLevelRenderLast(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
			Player player = Minecraft.getInstance().player;
			if (player == null) return;
			var opt = ChunkDifficulty.at(player.level(), player.blockPosition());
			if (opt.isEmpty()) return;
			if (!CurioCompat.hasItemInCurioOrSlot(player, LHItems.DETECTOR_GLASSES.get())) return;
			if (!CurioCompat.hasItemInCurioOrSlot(player, LHItems.DETECTOR.get())) return;
			ChunkClearRenderer.render(event.getPoseStack(), player, opt.get(), event.getPartialTick());
		}
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
			LevelRenderer renderer = event.getLevelRenderer();
			MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
			VertexConsumer cons = buffers.getBuffer(ClientEffectRenderEvents.get2DIcon(
					new ResourceLocation("l2hostility:textures/entity/chain.png")));
			var pose = event.getPoseStack();
			pose.pushPose();
			var cam = event.getCamera().getPosition();
			pose.translate(-cam.x, -cam.y, -cam.z);
			var level = Minecraft.getInstance().level;
			if (level != null) {
				for (var e : level.entitiesForRendering()) {
					if (!(e instanceof Mob mob)) continue;
					if (!e.isAlive() || !MobTraitCap.HOLDER.isProper(mob)) continue;
					var cap = MobTraitCap.HOLDER.get(mob);
					if (cap.asMaster == null) continue;
					Vec3 p0 = e.position().add(0, e.getBbHeight() / 2, 0);
					for (var minions : cap.asMaster.data) {
						var m = minions.minion;
						if (m == null || !m.isAlive()) continue;
						var scap = MobTraitCap.HOLDER.get(m);
						if (scap.asMinion == null) continue;
						Vec3 p1 = m.position().add(0, m.getBbHeight() / 2, 0);
						renderLink(event.getPoseStack(), cons, p0, p1, scap.asMinion.protectMaster);
					}
				}
			}
			pose.popPose();
		}
	}

	private static void renderLink(PoseStack pose, VertexConsumer cons, Vec3 p0, Vec3 p1, boolean protect) {
		Vec3 vec3 = p1.subtract(p0);
		float len = (float) vec3.length();
		if (len < 0.2f) return;

		pose.pushPose();
		pose.translate(p0.x, p0.y, p0.z);
		double d0 = vec3.horizontalDistance();
		pose.mulPose(Axis.YP.rotation((float) Mth.atan2(vec3.x, vec3.z)));
		pose.mulPose(Axis.XP.rotation((float) (Math.PI / 2 - Mth.atan2(vec3.y, d0))));
		float r = 0.125f;
		float off = protect ? 0.5f : 0;
		renderQuad(pose.last(), cons, 0, len, -r, r, 0, 0, off, off + 0.25f, 0, len);
		renderQuad(pose.last(), cons, 0, len, r, -r, 0, 0, off, off + 0.25f, 0, len);
		renderQuad(pose.last(), cons, 0, len, 0, 0, -r, r, off + 0.25f, off + 0.5f, 0, len);
		renderQuad(pose.last(), cons, 0, len, 0, 0, r, -r, off + 0.25f, off + 0.5f, 0, len);
		pose.popPose();
	}

	private static void renderQuad(PoseStack.Pose entry, VertexConsumer vc,
								   float y0, float y1, float x0, float x1, float z0, float z1,
								   float u0, float u1, float v0, float v1) {
		vertex(entry, vc, x0, y1, z0, u1, v0);
		vertex(entry, vc, x0, y0, z0, u1, v1);
		vertex(entry, vc, x1, y0, z1, u0, v1);
		vertex(entry, vc, x1, y1, z1, u0, v0);
	}

	private static void vertex(PoseStack.Pose entry, VertexConsumer vc, float x, float y, float z, float u, float v) {
		vc.vertex(entry.pose(), x, y, z).uv(u, v).normal(entry.normal(), 0.0F, 1.0F, 0.0F).endVertex();
	}

}
