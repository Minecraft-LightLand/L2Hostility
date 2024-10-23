package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2complements.content.entity.fireball.BaseFireball;
import dev.xkmc.l2hostility.content.item.consumable.HostilityChargeItem;
import dev.xkmc.l2hostility.init.registrate.LHEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HostilityCharge extends BaseFireball<HostilityCharge> {

	public HostilityCharge(EntityType<HostilityCharge> type, Level level) {
		super(type, level);
	}

	public HostilityCharge(double x, double y, double z, Vec3 vec, Level level) {
		super(LHEntities.CHARGE.get(), x, y, z, vec, level);
	}

	public HostilityCharge(LivingEntity owner, Vec3 vec, Level level) {
		super(LHEntities.CHARGE.get(), owner, vec, level);
	}

	protected void onHitEntity(Entity target) {
		if (target instanceof LivingEntity le && getItem().getItem() instanceof HostilityChargeItem charge) {
			charge.getType().onHit(le);
		}
	}

}
