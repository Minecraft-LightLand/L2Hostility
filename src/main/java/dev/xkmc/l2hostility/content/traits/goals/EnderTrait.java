package dev.xkmc.l2hostility.content.traits.goals;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class EnderTrait extends MobTrait {

	public EnderTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.level().isClientSide()) return;
		int duration = LHConfig.COMMON.teleportDuration.get();
		if (mob.tickCount % duration == 0 && mob instanceof Mob m && m.getTarget() != null) {
			Vec3 target = m.getTarget().position();
			mob.level().gameEvent(GameEvent.TELEPORT, m.position(), GameEvent.Context.of(mob));
			if (!mob.isSilent()) {
				mob.level().playSound(null, mob.xo, mob.yo, mob.zo, SoundEvents.ENDERMAN_TELEPORT, mob.getSoundSource(), 1.0F, 1.0F);
				mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
			mob.teleportTo(target.x(), target.y(), target.z());
		}
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				!event.getSource().is(L2DamageTypes.MAGIC)) {
			if (teleport(entity) || event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
				event.setCanceled(true);
			}
		}
	}

	private static boolean teleport(LivingEntity entity) {
		int r = LHConfig.COMMON.teleportRange.get();
		if (!entity.level().isClientSide() && entity.isAlive()) {
			double d0 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * r * 2;
			double d1 = entity.getY() + (double) (entity.getRandom().nextInt(r * 2) - r);
			double d2 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * r * 2;
			return teleport(entity, d0, d1, d2);
		} else {
			return false;
		}
	}

	private static boolean teleport(LivingEntity entity, double pX, double pY, double pZ) {
		BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos(pX, pY, pZ);
		while (ipos.getY() > entity.level().getMinBuildHeight() && !entity.level().getBlockState(ipos).blocksMotion()) {
			ipos.move(Direction.DOWN);
		}

		BlockState blockstate = entity.level().getBlockState(ipos);
		boolean flag = blockstate.blocksMotion();
		boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
		if (flag && !flag1) {
			EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(entity, pX, pY, pZ);
			if (event.isCanceled()) return false;
			Vec3 vec3 = entity.position();
			boolean flag2 = entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
			if (flag2) {
				entity.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
				if (!entity.isSilent()) {
					entity.level().playSound(null, entity.xo, entity.yo, entity.zo, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
					entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
				}
			}
			return flag2;
		} else {
			return false;
		}
	}

}
