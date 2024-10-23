package dev.xkmc.l2hostility.events;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2core.events.ClientEffectRenderEvents;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkClearRenderer;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.PerformanceConstants;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Hostility.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientEvents {

	@SubscribeEvent
	public static void addTooltip(ItemTooltipEvent event) {
		var level = event.getContext().level();
		if (level == null) return;
		EnchantmentDisabler.modifyTooltip(event.getItemStack(), event.getToolTip(), level, event.getContext(), event.getFlags());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void renderNamePlate(RenderNameTagEvent event) {
		if (!(event.getEntity() instanceof LivingEntity le)) return;
		boolean needHover = le.isInvisible() || LHConfig.CLIENT.showOnlyWhenHovered.get();
		if (needHover && Minecraft.getInstance().crosshairPickEntity != le) return;
		var opt = LHMiscs.MOB.type().getExisting(le);
		LocalPlayer player = Minecraft.getInstance().player;
		if (opt.isEmpty() || player == null) return;
		var cap = opt.get();
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
			renderNameTag(le, event, e, event.getPoseStack(), (offset + off) * 0.2f, mode);
			offset--;
		}
	}

	protected static void renderNameTag(LivingEntity le, RenderNameTagEvent event, Component text, PoseStack pose, float offset, Font.DisplayMode mode) {
		var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		double d0 = dispatcher.distanceToSqr(le);
		int max = LHConfig.CLIENT.overHeadRenderDistance.get();
		if (d0 > max * max) return;
		int light = LHConfig.CLIENT.overHeadRenderFullBright.get() ? LightTexture.FULL_BRIGHT :
				event.getPackedLight();
		Vec3 vec3 = le.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, le.getViewYRot(event.getPartialTick()));
		if (vec3 == null) vec3 = new Vec3(0, le.getBoundingBox().getYsize(), 0);
		pose.pushPose();
		pose.translate(vec3.x, vec3.y + offset, vec3.z);
		pose.mulPose(dispatcher.cameraOrientation());
		pose.scale(0.025F, -0.025F, 0.025F);
		Matrix4f matrix4f = pose.last().pose();
		Font font = event.getEntityRenderer().getFont();
		float f2 = (float) (-font.width(text) / 2);
		float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
		int j = (int) (f1 * 255.0F) << 24;
		font.drawInBatch(text, f2, 0, -1, false, matrix4f, event.getMultiBufferSource(), mode, j, light);
		pose.popPose();
	}

	private static boolean renderChunk = false;
	public static final List<Mob> MASTERS = new ArrayList<>();

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event) {
		MASTERS.clear();
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		var player = Minecraft.getInstance().player;
		if (player != null && player.tickCount % PerformanceConstants.CHUNK_RENDER == 0) {
			renderChunk = CurioCompat.hasItemInCurioOrSlot(player, LHItems.DETECTOR_GLASSES.get()) &&
					CurioCompat.hasItemInCurioOrSlot(player, LHItems.DETECTOR.get());
		}
	}

	@SubscribeEvent
	public static void onLevelRenderLast(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
			Player player = Minecraft.getInstance().player;
			if (player == null) return;
			var opt = ChunkDifficulty.at(player.level(), player.blockPosition());
			if (opt.isEmpty()) return;
			if (!renderChunk) return;
			ChunkClearRenderer.render(event.getPoseStack(), player, opt.get(), event.getPartialTick().getGameTimeDeltaPartialTick(true));
		}
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
			LevelRenderer renderer = event.getLevelRenderer();
			MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
			VertexConsumer cons = buffers.getBuffer(ClientEffectRenderEvents.get2DIcon(
					L2Hostility.loc("textures/entity/chain.png")));
			var pose = event.getPoseStack();
			pose.pushPose();
			var cam = event.getCamera().getPosition();
			pose.translate(-cam.x, -cam.y, -cam.z);
			var level = Minecraft.getInstance().level;
			if (level != null) {
				for (var e : MASTERS) {
					if (!e.isAlive()) continue;
					var cap = LHMiscs.MOB.type().getOrCreate(e);
					if (cap.asMaster == null) continue;
					Vec3 p0 = e.position().add(0, e.getBbHeight() / 2, 0);
					for (var minions : cap.asMaster.data) {
						var m = minions.minion;
						if (m == null || !m.isAlive()) continue;
						var scap = LHMiscs.MOB.type().getOrCreate(m);
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
		vc.addVertex(entry.pose(), x, y, z).setUv(u, v).setNormal(entry, 0.0F, 1.0F, 0.0F);
	}

}
