package dev.xkmc.l2hostility.content.traits.legendary;

import net.minecraft.ChatFormatting;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class RepellingTrait extends LegendaryTrait {

	public RepellingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.level().isClientSide()) {
			return;
		}
		var list = mob.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
				mob.getBoundingBox().inflate(10), e -> e instanceof Player ||
						e instanceof Mob m && m.getTarget() == mob);
		for (var e : list) {
			double r = mob.distanceTo(e);
			if (r > 10) return;
			Vec3 vec = e.position().subtract(mob.position()).normalize().scale(1);
			e.push(vec.x, vec.y, vec.z);
		}
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
			event.setCanceled(true);
		}
	}
}
