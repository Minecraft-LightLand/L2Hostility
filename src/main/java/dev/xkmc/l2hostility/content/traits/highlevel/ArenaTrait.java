package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class ArenaTrait extends AuraEffectTrait {

	public ArenaTrait() {
		super(LHEffects.ANTIBUILD::get);
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return;
		}
		if (event.getSource().getEntity() instanceof LivingEntity attacker) {
			if (attacker.hasEffect(LHEffects.ANTIBUILD.get())) {
				return;
			}
		}
		event.setCanceled(true);
	}


}
