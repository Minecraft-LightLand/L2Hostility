package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.entity.BulletType;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;

public class ShulkerTrait extends MobTrait {

	private final IntSupplier interval;
	private final BulletType type;
	private final int offset;

	public ShulkerTrait(ChatFormatting format, IntSupplier interval, BulletType type, int offset) {
		super(format);
		this.interval = interval;
		this.type = type;
		this.offset = offset;
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		type.onAttackedByOthers(level, entity, event);
	}

	public double modifyBonusDamage(DamageSource source, double factor, int lv) {
		if (source.getDirectEntity() instanceof HostilityBullet && source.is(DamageTypeTags.IS_EXPLOSION)) {
			return LHConfig.COMMON.grenadeDamageFactor.get();
		}
		return 1;
	}

	@Override
	public void tick(LivingEntity e, int level) {
		if (e.level().isClientSide()) return;
		if (e instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
			var cap = MobTraitCap.HOLDER.get(mob);
			var data = cap.getOrCreateData(getRegistryName(), Data::new);
			if (data.uuid != null &&
					mob.level() instanceof ServerLevel sl &&
					sl.getEntity(data.uuid) instanceof ShulkerBullet)
				return;
			data.tickCount++;
			if (data.tickCount < interval.getAsInt()) return;
			if ((mob.tickCount + offset) % interval.getAsInt() != 0) return;
			if (mob.getTarget() != null && mob.getTarget().isAlive()) {
				var bullet = new HostilityBullet(mob.level(), mob, mob.getTarget(),
						Direction.Axis.Y, type, level);
				data.tickCount = 0;
				if (type.limit())
					data.uuid = bullet.getUUID();
				mob.level().addFreshEntity(bullet);
				mob.playSound(SoundEvents.SHULKER_SHOOT, 2.0F,
						(mob.getRandom().nextFloat() - mob.getRandom().nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
				Component.literal(interval.getAsInt() / 20d + "")
						.withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
	}

	@SerialClass
	public static class Data extends CapStorageData {

		@SerialClass.SerialField
		public int tickCount;

		@SerialClass.SerialField
		public UUID uuid;

		public Data() {

		}

	}

}
