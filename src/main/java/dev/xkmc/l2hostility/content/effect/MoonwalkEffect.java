package dev.xkmc.l2hostility.content.effect;

import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class MoonwalkEffect extends InherentEffect implements ForceEffect {

	private static final double FACTOR = 0.7;

	public MoonwalkEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), MathHelper.getUUIDFromString("moonwalk").toString(),
				FACTOR, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public double getAttributeModifierValue(int lv, AttributeModifier op) {
		return Math.pow(FACTOR, lv + 1) - 1;
	}

}
