package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.l2complements.content.entity.ISizedItemEntity;
import dev.xkmc.l2complements.content.entity.SpecialSpriteRenderer;
import dev.xkmc.l2hostility.content.entity.BulletRenderer;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import dev.xkmc.l2hostility.content.entity.HostilityCharge;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static dev.xkmc.l2hostility.init.L2Hostility.REGISTRATE;

public class LHEntities {

	public static final EntityEntry<HostilityBullet> BULLET;

	public static final EntityEntry<HostilityCharge> CHARGE;

	static {
		BULLET =
				REGISTRATE.<HostilityBullet>entity("hostility_bullet", HostilityBullet::new, MobCategory.MISC)
						.properties(e -> e.sized(0.3125F, 0.3125F).clientTrackingRange(8))
						.renderer(() -> BulletRenderer::new)
						.register();

		CHARGE = REGISTRATE
				.<HostilityCharge>entity("hostility_charge", HostilityCharge::new, MobCategory.MISC)
				.properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(10))
				.renderer(() -> LHEntities::addRenderer)
				.defaultLang().register();

	}

	@OnlyIn(Dist.CLIENT)
	private static <T extends Entity & ItemSupplier & ISizedItemEntity> EntityRenderer<T> addRenderer(EntityRendererProvider.Context ctx) {
		return new SpecialSpriteRenderer<>(ctx, ctx.getItemRenderer(), true);
	}

	public static void register() {
	}

}
