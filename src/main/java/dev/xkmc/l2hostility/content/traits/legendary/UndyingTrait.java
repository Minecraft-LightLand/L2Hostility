package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class UndyingTrait extends LegendaryTrait {

	public UndyingTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
		if (entity.level().isClientSide()) {
			return;
		}
		float health = ForgeEventFactory.onLivingHeal(entity, entity.getMaxHealth());
		entity.setHealth(health);
		if (!entity.isDeadOrDying()) {
			event.setCanceled(true);
			L2Hostility.HANDLER.toTrackingPlayers(new TraitEffectToClient(entity, this, TraitEffects.UNDYING), entity);
		}
	}

}
