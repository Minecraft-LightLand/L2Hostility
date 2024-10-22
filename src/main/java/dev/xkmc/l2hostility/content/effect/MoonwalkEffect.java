package dev.xkmc.l2hostility.content.effect;

import dev.xkmc.l2core.base.effects.api.ForceEffect;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MoonwalkEffect extends InherentEffect implements ForceEffect {

	private static final double FACTOR = 0.7;

	public MoonwalkEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.GRAVITY, L2Hostility.loc("moonwalk"),
				AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
				lv -> Math.pow(FACTOR, lv + 1) - 1);
	}

}
