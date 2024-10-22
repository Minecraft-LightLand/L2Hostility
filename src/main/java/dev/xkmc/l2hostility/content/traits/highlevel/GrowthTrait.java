package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class GrowthTrait extends MobTrait {

	public GrowthTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void tick(LivingEntity mob, int level) {
		if (mob.getHealth() == mob.getMaxHealth() && mob instanceof Slime slime) {
			int size = slime.getSize();
			if (size < 1 << (level + 2)) {
				slime.setSize(size + 1, false);
			}
		}
	}

	@Override
	public void postInit(LivingEntity mob, int lv) {
		var cap = LHMiscs.MOB.type().getOrCreate(mob);
		var regen = LHTraits.REGEN.get();
		if (regen.allow(mob) && cap.getTraitLevel(regen) < lv) {
			cap.setTrait(regen, lv);
		}
	}

	@Override
	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		return event.getSource().is(DamageTypes.IN_WALL);
	}

	@Override
	public void onDeath(int lv, LivingEntity entity, LivingDeathEvent event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			entity.setHealth(entity.getMaxHealth());
			entity.discard();
		}
	}

	@Override
	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		return le instanceof Slime && super.allow(le, difficulty, maxModLv);
	}

	@Override
	public int inherited(MobTraitCap mobTraitCap, int rank, InheritContext ctx) {
		return ctx.isPrimary() ? rank - 1 : 0;
	}

}
