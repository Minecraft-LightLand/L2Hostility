package dev.xkmc.l2hostility.content.traits.legendary;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class UndyingTrait extends LegendaryTrait {

	public UndyingTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
		entity.heal(entity.getMaxHealth());
		if (!entity.isDeadOrDying()) {
			event.setCanceled(true);
		}
	}

}
