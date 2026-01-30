package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ShulkerBulletRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class BulletRenderer extends ShulkerBulletRenderer {

	private static final ResourceLocation TEXTURE_LOCATION = L2Hostility.loc("textures/entity/spark.png");

	public BulletRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(ShulkerBullet bullet) {
		if (bullet instanceof HostilityBullet b && b.type == BulletType.EXPLODE) return TEXTURE_LOCATION;
		return super.getTextureLocation(bullet);
	}

}
