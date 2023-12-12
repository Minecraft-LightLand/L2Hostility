package dev.xkmc.l2hostility.backport.explosion;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

import javax.annotation.Nullable;

public record VanillaExplosionContext(@Nullable Entity entity, @Nullable DamageSource source,
									  @Nullable ExplosionDamageCalculator calculator,
									  boolean fire, Explosion.BlockInteraction type) {
}
