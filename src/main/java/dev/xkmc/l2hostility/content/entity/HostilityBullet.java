package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class HostilityBullet extends ShulkerBullet {

	private BulletType type;
	private int lv;

	public HostilityBullet(EntityType<HostilityBullet> type, Level level) {
		super(type, level);
	}

	public HostilityBullet(Level level, LivingEntity owner, Entity target, Direction.Axis direction, BulletType type, int lv) {
		this(LHMiscs.BULLET.get(), level);
		this.setOwner(owner);
		BlockPos blockpos = owner.blockPosition();
		double d0 = (double) blockpos.getX() + 0.5D;
		double d1 = (double) blockpos.getY() + 0.5D;
		double d2 = (double) blockpos.getZ() + 0.5D;
		this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
		this.finalTarget = target;
		this.currentMoveDirection = Direction.UP;
		this.selectNextMoveDirection(direction);
		this.type = type;
		this.lv = lv;
	}

	protected void onHitEntity(EntityHitResult result) {
		if (type == null) return;
		Entity target = result.getEntity();
		Entity owner = this.getOwner();
		LivingEntity leowner = owner instanceof LivingEntity ? (LivingEntity) owner : null;
		target.hurt(this.damageSources().mobProjectile(this, leowner), type.getDamage(lv));
		type.onHit(this, result, lv);
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		type.onHit(this, result, lv);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("BulletType", type.name());
		tag.putInt("BulletLevel", lv);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		type = BulletType.valueOf(tag.getString("BulletType"));
		lv = tag.getInt("BulletLevel");
	}

}
