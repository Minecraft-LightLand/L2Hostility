package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;

public interface HitTargetEnchantment {

	void hitMob(LivingEntity target, MobTraitCap cap, int value, DamageData.Offence cache);

}
