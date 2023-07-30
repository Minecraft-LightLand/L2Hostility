package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.List;

public class RepellingTrait extends LegendaryTrait {

	public RepellingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		int r = LHConfig.COMMON.repellRange.get();
		List<? extends LivingEntity> list;
		if (mob.level().isClientSide()) {
			list = mob.level().getEntities(EntityTypeTest.forClass(Player.class),
					mob.getBoundingBox().inflate(r), e -> e.isLocalPlayer() && !e.getAbilities().instabuild);
		} else {
			list = mob.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
					mob.getBoundingBox().inflate(r), e -> e instanceof Player ||
							e instanceof Mob m && m.getTarget() == mob);
		}
		for (var e : list) {
			double dist = mob.distanceTo(e) / r;
			if (dist > 1) return;
			Vec3 vec = e.position().subtract(mob.position()).normalize().scale((1 - dist) * 0.2);
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


	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.COMMON.repellRange.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
		super.addDetail(list);
	}

}
