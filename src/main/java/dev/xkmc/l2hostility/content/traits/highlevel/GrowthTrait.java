package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;

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
		var cap = MobTraitCap.HOLDER.get(mob);
		var regen = LHTraits.REGEN.get();
		if (regen.allow(mob) && cap.getTraitLevel(regen) < lv) {
			cap.setTrait(regen, lv);
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
