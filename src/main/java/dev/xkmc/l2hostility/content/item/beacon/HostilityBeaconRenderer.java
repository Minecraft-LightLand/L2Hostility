package dev.xkmc.l2hostility.content.item.beacon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HostilityBeaconRenderer implements BlockEntityRenderer<HostilityBeaconBlockEntity> {
	public static final ResourceLocation BEAM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");
	public static final int MAX_RENDER_Y = 1024;

	public HostilityBeaconRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	public void render(HostilityBeaconBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		long time = be.getLevel().getGameTime();
		var list = be.getBeamSections();
		int y = 0;
		for (int i = 0; i < list.size(); i++) {
			var sec = list.get(i);
			renderBeaconBeam(pose, buffer, pTick, time, y, i == list.size() - 1 ? 1024 : sec.getHeight(), sec.getColor());
			y += sec.getHeight();
		}
	}

	private static void renderBeaconBeam(
			PoseStack pose, MultiBufferSource buffer, float pt, long t, int y, int h, int col
	) {
		renderBeaconBeam(pose, buffer, BEAM_LOCATION, pt, 1, t, y, h, col, 0.2F, 0.25F);
	}

	public static void renderBeaconBeam(
			PoseStack pose, MultiBufferSource buffer, ResourceLocation rl,
			float pt, float h0, long time, int y0, int h, int col, float r0, float r1
	) {
		int y1 = y0 + h;
		pose.pushPose();
		pose.translate(0.5, 0.0, 0.5);
		float t0 = (float) Math.floorMod(time, 40) + pt;
		float t1 = h < 0 ? t0 : -t0;
		float t2 = Mth.frac(t1 * 0.2F - (float) Mth.floor(t1 * 0.1F));
		pose.pushPose();
		pose.mulPose(Axis.YP.rotationDegrees(t0 * 2.25F - 45.0F));
		float f12 = -1.0F + t2;
		float f13 = (float) h * h0 * (0.5F / r0) + f12;
		renderPart(pose, buffer.getBuffer(RenderType.beaconBeam(rl, false)), col,
				y0, y1, 0, r0, r0, 0, -r0, 0, 0, -r0, 0, 1, f13, f12
		);
		pose.popPose();
		f12 = -1.0F + t2;
		f13 = (float) h * h0 + f12;
		renderPart(pose, buffer.getBuffer(RenderType.beaconBeam(rl, true)), FastColor.ARGB32.color(32, col),
				y0, y1, -r1, -r1, r1, -r1, -r1, r1, r1, r1, 0, 1, f13, f12
		);
		pose.popPose();
	}

	private static void renderPart(
			PoseStack p_112156_,
			VertexConsumer p_112157_,
			int p_112162_,
			int p_112163_,
			int p_351014_,
			float p_112158_,
			float p_112159_,
			float p_112160_,
			float p_112161_,
			float p_112164_,
			float p_112165_,
			float p_112166_,
			float p_112167_,
			float p_112168_,
			float p_112169_,
			float p_112170_,
			float p_112171_
	) {
		PoseStack.Pose posestack$pose = p_112156_.last();
		renderQuad(
				posestack$pose, p_112157_, p_112162_, p_112163_, p_351014_, p_112158_, p_112159_, p_112160_, p_112161_, p_112168_, p_112169_, p_112170_, p_112171_
		);
		renderQuad(
				posestack$pose, p_112157_, p_112162_, p_112163_, p_351014_, p_112166_, p_112167_, p_112164_, p_112165_, p_112168_, p_112169_, p_112170_, p_112171_
		);
		renderQuad(
				posestack$pose, p_112157_, p_112162_, p_112163_, p_351014_, p_112160_, p_112161_, p_112166_, p_112167_, p_112168_, p_112169_, p_112170_, p_112171_
		);
		renderQuad(
				posestack$pose, p_112157_, p_112162_, p_112163_, p_351014_, p_112164_, p_112165_, p_112158_, p_112159_, p_112168_, p_112169_, p_112170_, p_112171_
		);
	}

	private static void renderQuad(
			PoseStack.Pose p_323955_,
			VertexConsumer p_112122_,
			int p_112127_,
			int p_112128_,
			int p_350566_,
			float p_112123_,
			float p_112124_,
			float p_112125_,
			float p_112126_,
			float p_112129_,
			float p_112130_,
			float p_112131_,
			float p_112132_
	) {
		addVertex(p_323955_, p_112122_, p_112127_, p_350566_, p_112123_, p_112124_, p_112130_, p_112131_);
		addVertex(p_323955_, p_112122_, p_112127_, p_112128_, p_112123_, p_112124_, p_112130_, p_112132_);
		addVertex(p_323955_, p_112122_, p_112127_, p_112128_, p_112125_, p_112126_, p_112129_, p_112132_);
		addVertex(p_323955_, p_112122_, p_112127_, p_350566_, p_112125_, p_112126_, p_112129_, p_112131_);
	}

	private static void addVertex(
			PoseStack.Pose p_324495_, VertexConsumer p_253894_, int p_254357_, int p_350652_, float p_253871_, float p_253841_, float p_254568_, float p_254361_
	) {
		p_253894_.addVertex(p_324495_, p_253871_, (float) p_350652_, p_253841_)
				.setColor(p_254357_)
				.setUv(p_254568_, p_254361_)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(15728880)
				.setNormal(p_324495_, 0.0F, 1.0F, 0.0F);
	}

	public boolean shouldRenderOffScreen(HostilityBeaconBlockEntity blockEntity) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	public boolean shouldRender(HostilityBeaconBlockEntity blockEntity, Vec3 cameraPos) {
		return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(cameraPos.multiply(1.0, 0.0, 1.0), (double) this.getViewDistance());
	}

	@Override
	public net.minecraft.world.phys.AABB getRenderBoundingBox(HostilityBeaconBlockEntity blockEntity) {
		net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
		return new net.minecraft.world.phys.AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, MAX_RENDER_Y, pos.getZ() + 1.0);
	}

}