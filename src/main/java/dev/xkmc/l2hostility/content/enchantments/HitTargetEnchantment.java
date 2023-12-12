package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.entity.LivingEntity;

public interface HitTargetEnchantment {

	void hitMob(LivingEntity target, MobTraitCap cap, Integer value, AttackCache cache);

}
