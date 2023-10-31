package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2complements.content.entity.fireball.BaseFireball;
import dev.xkmc.l2hostility.content.item.consumable.HostilityChargeItem;
import dev.xkmc.l2hostility.init.registrate.LHEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HostilityCharge extends BaseFireball<HostilityCharge> {

	public HostilityCharge(EntityType<HostilityCharge> type, Level level) {
		super(type, level);
	}

	public HostilityCharge(double x, double y, double z, double vx, double vy, double vz, Level level) {
		super(LHEntities.CHARGE.get(), x, y, z, vx, vy, vz, level);
	}

	public HostilityCharge(LivingEntity owner, double vx, double vy, double vz, Level level) {
		super(LHEntities.CHARGE.get(), owner, vx, vy, vz, level);
	}

	protected void onHitEntity(Entity target) {
		if (target instanceof LivingEntity le && getItem().getItem() instanceof HostilityChargeItem charge) {
			charge.getType().onHit(le);
		}
	}

}
