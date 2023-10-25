package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.entity.BulletType;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.List;
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

	@Override
	public void tick(LivingEntity e, int level) {
		if (e.level().isClientSide()) return;
		if (e instanceof Mob mob) {
			if ((e.tickCount + offset) % interval.getAsInt() != 0) return;
			if (mob.getTarget() != null) {
				mob.level().addFreshEntity(new HostilityBullet(mob.level(), mob, mob.getTarget(),
						Direction.Axis.Y, type, level));
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

}
