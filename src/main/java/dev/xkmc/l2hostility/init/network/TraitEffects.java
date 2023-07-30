package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public enum TraitEffects {
	UNDYING(() -> ClientSyncHandler::triggerUndying);

	public final Supplier<BiConsumer<LivingEntity, MobTrait>> func;

	TraitEffects(Supplier<BiConsumer<LivingEntity, MobTrait>> func) {
		this.func = func;
	}
}
