package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.DamageTypeGen;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class KillerAuraTrait extends MobTrait {

	public KillerAuraTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		int itv = LHConfig.COMMON.killerAuraInterval.get() / level;
		int damage = LHConfig.COMMON.killerAuraDamage.get() * level;
		int range = LHConfig.COMMON.killerAuraRange.get();
		if (!mob.level().isClientSide() && mob.tickCount % itv == 0) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			AABB box = mob.getBoundingBox().inflate(range);
			for (var e : mob.level().getEntitiesOfClass(LivingEntity.class, box)) {
				if (e instanceof Player || e instanceof Mob target && target.getTarget() == mob) {
					if (e.distanceTo(mob) > range) continue;
					cap.traitEvent((k, v) -> k.postHurt(v, mob, e));
					e.hurt(new DamageSource(DamageTypeGen.forKey(mob.level(), DamageTypeGen.KILLER_AURA),
							mob, null), damage);
				}
			}
			L2Hostility.HANDLER.toTrackingPlayers(new TraitEffectToClient(mob, this, TraitEffects.AURA), mob);
		}
		if (mob.level().isClientSide()) {
			Vec3 center = mob.position();
			float tpi = (float) (Math.PI * 2);
			Vec3 v0 = new Vec3(0, range, 0);
			v0 = v0.xRot(tpi / 4).yRot(mob.getRandom().nextFloat() * tpi);
			mob.level().addAlwaysVisibleParticle(ParticleTypes.FLAME,
					center.x + v0.x,
					center.y + v0.y + 0.5f,
					center.z + v0.z, 0, 0, 0);
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
				mapLevel(i -> Component.literal(LHConfig.COMMON.killerAuraDamage.get() * i + "")
						.withStyle(ChatFormatting.AQUA)),
				Component.literal("" + LHConfig.COMMON.killerAuraRange.get())
						.withStyle(ChatFormatting.AQUA),
				mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.killerAuraInterval.get() * 5d / i) * 0.01 + "")
						.withStyle(ChatFormatting.AQUA))
		).withStyle(ChatFormatting.GRAY));
	}

}
