package dev.xkmc.l2hostility.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBullet.class)
public abstract class ShulkerBulletMixin extends Projectile {

	protected ShulkerBulletMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
		super(p_37248_, p_37249_);
	}

	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
	private boolean l2hostility$addParticle(Level level, ParticleOptions particle, double x, double y, double z, double vx, double vy, double vz) {
		Projectile self = this;
		return !(self instanceof HostilityBullet);
	}

}
