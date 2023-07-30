package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.List;

public class FieryTrait extends SelfEffectTrait {

	public FieryTrait() {
		super(() -> MobEffects.FIRE_RESISTANCE);
	}

	@Override
	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		return super.allow(le, difficulty, maxModLv) && !le.fireImmune();
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache) {
		if (cache.getLivingHurtEvent().getSource().getDirectEntity() instanceof LivingEntity le)
			le.setSecondsOnFire(LHConfig.COMMON.fieryTime.get());
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (event.getSource().getDirectEntity() instanceof LivingEntity le)
			le.setSecondsOnFire(LHConfig.COMMON.fieryTime.get());
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.COMMON.fieryTime.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
		super.addDetail(list);
	}

}
