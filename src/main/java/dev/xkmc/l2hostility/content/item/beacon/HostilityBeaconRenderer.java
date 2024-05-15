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
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class HostilityBeaconRenderer implements BlockEntityRenderer<HostilityBeaconBlockEntity> {
	public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");
	public static final int MAX_RENDER_Y = 1024;

	public HostilityBeaconRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	public void render(HostilityBeaconBlockEntity be, float pTick, PoseStack pose, MultiBufferSource source, int light, int overlay) {
		long time = be.getLevel().getGameTime();
		var list = be.getBeamSections();
		int y = 0;
		for (int k = 0; k < list.size(); ++k) {
			var sec = list.get(k);
			renderBeaconBeam(pose, source, pTick, time, y,
					k == list.size() - 1 ? MAX_RENDER_Y : sec.getHeight(), sec.getColor());
			y += sec.getHeight();
		}

	}

	private static void renderBeaconBeam(PoseStack pose, MultiBufferSource source, float pTick, long time, int y0, int y1, float[] color) {
		renderBeaconBeam(pose, source, BEAM_LOCATION, pTick, 1.0F, time, y0, y1, color, 0.2F, 0.25F);
	}

	public static void renderBeaconBeam(PoseStack pose, MultiBufferSource source, ResourceLocation id, float pTick, float p_112189_, long time, int y0, int y1, float[] color, float p_112194_, float p_112195_) {
		int i = y0 + y1;
		pose.pushPose();
		pose.translate(0.5D, 0.0D, 0.5D);
		float f = (float) Math.floorMod(time, 40) + pTick;
		float f1 = y1 < 0 ? f : -f;
		float f2 = Mth.frac(f1 * 0.2F - (float) Mth.floor(f1 * 0.1F));
		float f3 = color[0];
		float f4 = color[1];
		float f5 = color[2];
		pose.pushPose();
		pose.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
		float f6;
		float f8;
		float f9 = -p_112194_;
		float f12 = -p_112194_;
		float f15 = -1.0F + f2;
		float f16 = (float) y1 * p_112189_ * (0.5F / p_112194_) + f15;
		renderPart(pose, source.getBuffer(RenderType.beaconBeam(id, false)), f3, f4, f5, 1.0F, y0, i, 0.0F, p_112194_, p_112194_, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
		pose.popPose();
		f6 = -p_112195_;
		float f7 = -p_112195_;
		f8 = -p_112195_;
		f9 = -p_112195_;
		f15 = -1.0F + f2;
		f16 = (float) y1 * p_112189_ + f15;
		renderPart(pose, source.getBuffer(RenderType.beaconBeam(id, true)), f3, f4, f5, 0.125F, y0, i, f6, f7, p_112195_, f8, f9, p_112195_, p_112195_, p_112195_, 0.0F, 1.0F, f16, f15);
		pose.popPose();
	}

	private static void renderPart(PoseStack p_112156_, VertexConsumer p_112157_, float p_112158_, float p_112159_, float p_112160_, float p_112161_, int p_112162_, int p_112163_, float p_112164_, float p_112165_, float p_112166_, float p_112167_, float p_112168_, float p_112169_, float p_112170_, float p_112171_, float p_112172_, float p_112173_, float p_112174_, float p_112175_) {
		PoseStack.Pose posestack$pose = p_112156_.last();
		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();
		renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112164_, p_112165_, p_112166_, p_112167_, p_112172_, p_112173_, p_112174_, p_112175_);
		renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112170_, p_112171_, p_112168_, p_112169_, p_112172_, p_112173_, p_112174_, p_112175_);
		renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112166_, p_112167_, p_112170_, p_112171_, p_112172_, p_112173_, p_112174_, p_112175_);
		renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112168_, p_112169_, p_112164_, p_112165_, p_112172_, p_112173_, p_112174_, p_112175_);
	}

	private static void renderQuad(Matrix4f p_253960_, Matrix3f p_254005_, VertexConsumer p_112122_, float p_112123_, float p_112124_, float p_112125_, float p_112126_, int p_112127_, int p_112128_, float p_112129_, float p_112130_, float p_112131_, float p_112132_, float p_112133_, float p_112134_, float p_112135_, float p_112136_) {
		addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112128_, p_112129_, p_112130_, p_112134_, p_112135_);
		addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112127_, p_112129_, p_112130_, p_112134_, p_112136_);
		addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112127_, p_112131_, p_112132_, p_112133_, p_112136_);
		addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112128_, p_112131_, p_112132_, p_112133_, p_112135_);
	}

	private static void addVertex(Matrix4f p_253955_, Matrix3f p_253713_, VertexConsumer p_253894_, float p_253871_, float p_253841_, float p_254568_, float p_254361_, int p_254357_, float p_254451_, float p_254240_, float p_254117_, float p_253698_) {
		p_253894_.vertex(p_253955_, p_254451_, (float) p_254357_, p_254240_).color(p_253871_, p_253841_, p_254568_, p_254361_).uv(p_254117_, p_253698_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_253713_, 0.0F, 1.0F, 0.0F).endVertex();
	}

	public boolean shouldRenderOffScreen(HostilityBeaconBlockEntity p_112138_) {
		return true;
	}

	public int getViewDistance() {
		return 256;
	}

	public boolean shouldRender(HostilityBeaconBlockEntity p_173531_, Vec3 p_173532_) {
		return Vec3.atCenterOf(p_173531_.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(p_173532_.multiply(1.0D, 0.0D, 1.0D), (double) this.getViewDistance());
	}

}