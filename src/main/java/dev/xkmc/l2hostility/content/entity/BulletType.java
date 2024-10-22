package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2library.content.explosion.*;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public enum BulletType {
	PLAIN(4, true), EXPLODE(4, true);

	private final float damage;
	private final boolean limit;

	BulletType(float damage, boolean limit) {
		this.damage = damage;
		this.limit = limit;
	}

	public float getDamage(int level) {
		return damage * level;
	}

	public void onHit(HostilityBullet bullet, HitResult result, int level) {
		if (this == EXPLODE) {
			Vec3 pos = result.getLocation();
			ExplosionHandler.explode(new BaseExplosion(
					new BaseExplosionContext(bullet.level(), pos.x, pos.y, pos.z, 1 + level),
					new VanillaExplosionContext(bullet, null, null, false, Explosion.BlockInteraction.KEEP),
					bullet::isTarget,
					ParticleExplosionContext.of(1 + level)));
		}
	}

	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		if (event.getSource().getDirectEntity() instanceof ShulkerBullet) {
			return true;
		}
		if (this == EXPLODE) {
			return event.getSource().is(DamageTypeTags.IS_EXPLOSION);
		}
		return false;
	}

	public boolean limit() {
		return limit;
	}

}
