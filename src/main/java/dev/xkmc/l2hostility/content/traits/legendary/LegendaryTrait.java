package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.IntSupplier;

public class LegendaryTrait extends MobTrait {

	public LegendaryTrait(ChatFormatting format) {
		super(format);
	}

	public LegendaryTrait(IntSupplier color) {
		super(color);
	}

	@Override
	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		return maxModLv > TraitManager.getMaxLevel() && super.allow(le, difficulty, maxModLv);
	}

	@Override
	public boolean isBanned() {
		return !LHConfig.SERVER.allowLegendary.get() || super.isBanned();
	}

}
