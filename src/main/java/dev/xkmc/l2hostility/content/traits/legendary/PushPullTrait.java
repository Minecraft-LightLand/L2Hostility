package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class PushPullTrait extends LegendaryTrait {

	public PushPullTrait(ChatFormatting style) {
		super(style);
	}

	protected abstract int getRange();

	protected abstract double getStrength(double dist);

	@Override
	public void tick(LivingEntity mob, int level) {
		int r = getRange();
		List<? extends LivingEntity> list;
		if (mob.level().isClientSide()) {
			list = mob.level().getEntities(EntityTypeTest.forClass(Player.class),
					mob.getBoundingBox().inflate(r), e -> e.isLocalPlayer() && !e.getAbilities().instabuild);
		} else {
			list = mob.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
					mob.getBoundingBox().inflate(r), e ->
							e instanceof Player pl && !pl.getAbilities().instabuild ||
									e instanceof Mob m && m.getTarget() == mob);
		}
		for (var e : list) {
			double dist = mob.distanceTo(e) / r;
			if (dist > 1) return;
			if (CurioCompat.hasItem(e, LHItems.ABRAHADABRA.get())) continue;
			double strength = getStrength(dist);
			int lv = 0;
			for (var armor : e.getArmorSlots()) {
				lv += armor.getEnchantmentLevel(LHEnchantments.INSULATOR.get());
			}
			if (lv > 0) {
				strength *= Math.pow(LHConfig.COMMON.insulatorFactor.get(), lv);
			}
			Vec3 vec = e.position().subtract(mob.position()).normalize().scale(strength);
			e.push(vec.x, vec.y, vec.z);
		}
	}

}
