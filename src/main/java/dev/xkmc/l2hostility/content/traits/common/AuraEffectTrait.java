package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.capability.mob.PerformanceConstants;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class AuraEffectTrait extends MobTrait {

	private final Supplier<MobEffect> eff;

	public AuraEffectTrait(Supplier<MobEffect> eff) {
		super(() -> eff.get().getColor());
		this.eff = eff;
	}

	protected boolean canApply(LivingEntity e) {
		if (LHItems.RING_REFLECTION.get().isOn(e)) return false;
		if (LHItems.ABRAHADABRA.get().isOn(e)) return false;
		return true;
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		int range = LHConfig.COMMON.range.get(getRegistryName().getPath()).get();
		if (!mob.level().isClientSide() && mob.tickCount % PerformanceConstants.auraApplyInterval() == 0) {
			AABB box = mob.getBoundingBox().inflate(range);
			for (var e : mob.level().getEntitiesOfClass(LivingEntity.class, box)) {
				if (!(e instanceof Player pl) || !pl.getAbilities().instabuild) {
					if (e.distanceTo(mob) > range) continue;
					if (!canApply(e)) continue;
					EffectUtil.refreshEffect(e, new MobEffectInstance(eff.get(), 40, level - 1,
									true, true),
							EffectUtil.AddReason.FORCE, mob);
				}
			}
		}
		if (mob.level().isClientSide()) {
			Vec3 center = mob.position();
			float tpi = (float) (Math.PI * 2);
			Vec3 v0 = new Vec3(0, range, 0);
			v0 = v0.xRot(tpi / 4).yRot(mob.getRandom().nextFloat() * tpi);
			int k = eff.get().getColor();
			mob.level().addAlwaysVisibleParticle(ParticleTypes.EFFECT,
					center.x + v0.x,
					center.y + v0.y + 0.5f,
					center.z + v0.z,
					(k >> 16 & 255) / 255.0,
					(k >> 8 & 255) / 255.0,
					(k & 255) / 255.0);
		}
	}

}
