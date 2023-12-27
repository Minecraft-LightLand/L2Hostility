package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class UndyingTrait extends LegendaryTrait {

	public UndyingTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
		if (entity.level.isClientSide()) {
			return;
		}
		if (event.getSource().isBypassInvul()) {
			return;
		}
		if (MobTraitCap.HOLDER.get(entity).hasTrait(LHTraits.SPLIT.get())) {
			return;
		}
		if (!validTarget(entity)) {
			return;
		}
		float health = ForgeEventFactory.onLivingHeal(entity, entity.getMaxHealth());
		entity.setHealth(health);
		if (entity.isAlive()) {
			event.setCanceled(true);
			L2Hostility.HANDLER.toTrackingPlayers(new TraitEffectToClient(entity, this, TraitEffects.UNDYING), entity);
		}
	}

	@Override
	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		return validTarget(le) && super.allow(le, difficulty, maxModLv);
	}

	public boolean validTarget(LivingEntity le) {
		if (le instanceof EnderDragon) {
			return false;
		}
		return le.canBeAffected(new MobEffectInstance(LCEffects.CURSE.get(), 100));
	}

}
