package dev.xkmc.l2hostility.backport.explosion;

import net.minecraft.world.entity.Entity;

public interface ModExplosionContext {

	/**
	 * return false to cancel damage
	 */
	boolean hurtEntity(Entity entity);

}
