package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class ArenaTrait extends AuraEffectTrait {

	public ArenaTrait() {
		super(LHEffects.ANTIBUILD);
	}

	@Override
	protected boolean canApply(LivingEntity e) {
		return true;
	}

	@Override
	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return false;
		}
		if (event.getSource().getEntity() instanceof LivingEntity attacker) {
			if (attacker.hasEffect(LHEffects.ANTIBUILD)) {
				return false;
			}
			if (attacker instanceof Mob mob && LHMiscs.MOB.type().getExisting(mob)
					.map(e -> e.getTraitLevel(this)).orElse(0) >= level) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onDamaged(int level, LivingEntity mob, DamageData.Defence cache) {
		if (cache.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return;
		}
		if (cache.getAttacker() != null && cache.getAttacker().hasEffect(LHEffects.ANTIBUILD)) {
			return;
		}
		cache.addDealtModifier(DamageModifier.nonlinearFinal(12345, e -> 0, getRegistryName()));
	}

}
