package dev.xkmc.l2hostility.content.traits.goals;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.UUID;

public class CounterStrikeTrait extends MobTrait {

	public CounterStrikeTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void tick(LivingEntity le, int level) {
		if (le.level.isClientSide()) return;
		var data = MobTraitCap.HOLDER.get(le).getOrCreateData(getRegistryName(), Data::new);
		if (data.cooldown > 0) {
			data.cooldown--;
			return;
		}
		if (!(le instanceof Mob mob)) return;
		if (!le.isOnGround()) return;
		var target = mob.getTarget();
		if (target == null || !target.isAlive()) return;
		if (data.strikeId == null || !data.strikeId.equals(target.getUUID())) return;
		Vec3 diff = target.position().subtract(le.position());
		diff = diff.normalize().scale(3);
		if (diff.y <= 0.2)
			diff = diff.add(0, 0.2, 0);
		le.setDeltaMovement(diff);
		le.hasImpulse = true;
		data.duration = LHConfig.COMMON.counterStrikeDuration.get();
		data.strikeId = null;
	}

	@Override
	public void onHurtByOthers(int level, LivingEntity le, LivingHurtEvent event) {
		if (le.level.isClientSide()) return;
		var target = event.getSource().getEntity();
		var data = MobTraitCap.HOLDER.get(le).getOrCreateData(getRegistryName(), Data::new);
		if (target instanceof LivingEntity && le instanceof Mob mob && mob.getTarget() == target) {
			data.strikeId = target.getUUID();
		}
	}

	@SerialClass
	public static class Data extends CapStorageData {

		@SerialClass.SerialField
		public int cooldown, duration;

		@SerialClass.SerialField
		public UUID strikeId;

	}

}
