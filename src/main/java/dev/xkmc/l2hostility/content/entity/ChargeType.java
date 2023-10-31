package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2hostility.content.item.traits.EffectBooster;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public enum ChargeType {
	BOOST(EffectBooster::boostCharge),
	ETERNAL(EffectBooster::boostInfinite);

	private final Consumer<LivingEntity> cons;

	ChargeType(Consumer<LivingEntity> cons) {
		this.cons = cons;
	}

	public void onHit(LivingEntity le) {
		cons.accept(le);
	}

}
