package dev.xkmc.l2hostility.content.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public enum BulletType {
	PLAIN(4), EXPLODE(4);

	private final float damage;

	BulletType(float damage) {
		this.damage = damage;
	}

	public float getDamage(int level) {
		return damage * level;
	}

	public void onHit(HostilityBullet bullet, HitResult result, int level) {
		if (this == EXPLODE) {
			Vec3 pos = result.getLocation();
			bullet.level().explode(bullet, pos.x, pos.y, pos.z, 1 + level, Level.ExplosionInteraction.NONE);
		}
	}

}
