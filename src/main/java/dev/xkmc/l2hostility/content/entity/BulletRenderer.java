package dev.xkmc.l2hostility.content.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ShulkerBulletRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class BulletRenderer extends ShulkerBulletRenderer {

	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(L2Hostility.MODID, "textures/entity/spark.png");

	private final ShulkerBulletModel<ShulkerBullet> model;

	public BulletRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		this.model = new ShulkerBulletModel<>(ctx.bakeLayer(ModelLayers.SHULKER_BULLET));
	}

	public void render(ShulkerBullet e, float p_115863_, float p_115864_, PoseStack p_115865_, MultiBufferSource p_115866_, int p_115867_) {
		var tex = getTextureLocation(e);
		p_115865_.pushPose();
		float $$6 = Mth.rotLerp(p_115864_, e.yRotO, e.getYRot());
		float $$7 = Mth.lerp(p_115864_, e.xRotO, e.getXRot());
		float $$8 = (float) e.tickCount + p_115864_;
		p_115865_.translate(0.0F, 0.15F, 0.0F);
		p_115865_.mulPose(Axis.YP.rotationDegrees(Mth.sin($$8 * 0.1F) * 180.0F));
		p_115865_.mulPose(Axis.XP.rotationDegrees(Mth.cos($$8 * 0.1F) * 180.0F));
		p_115865_.mulPose(Axis.ZP.rotationDegrees(Mth.sin($$8 * 0.15F) * 360.0F));
		p_115865_.scale(-0.5F, -0.5F, 0.5F);
		model.setupAnim(e, 0.0F, 0.0F, 0.0F, $$6, $$7);
		VertexConsumer $$9 = p_115866_.getBuffer(this.model.renderType(tex));
		model.renderToBuffer(p_115865_, $$9, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_115865_.scale(1.5F, 1.5F, 1.5F);
		VertexConsumer $$10 = p_115866_.getBuffer(RenderType.entityTranslucent(tex));
		model.renderToBuffer(p_115865_, $$10, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
		p_115865_.popPose();
		super.render(e, p_115863_, p_115864_, p_115865_, p_115866_, p_115867_);
	}

	@Override
	public ResourceLocation getTextureLocation(ShulkerBullet bullet) {
		if (bullet instanceof HostilityBullet b && b.type == BulletType.EXPLODE) return TEXTURE_LOCATION;
		return super.getTextureLocation(bullet);
	}


}
