package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public abstract class HostilityInitEvent extends LivingEvent {

	public enum InitPhase {
		/**
		 * Initialize all data. If mob is copied (from split or slime), then COPY will be invoked instead of this.
		 * If Pre is cancelled, ARMOR will not be invoked, but Post will still be fired.
		 */
		INIT,
		/**
		 * Copy mob level and trait data. Then invoke generation of ARMOR.
		 * If Pre is cancelled, trait will not be copied, but level will still be copied, Post will be fired,
		 * and ARMOR generation would still be invoked.
		 */
		COPY,
		/**
		 * Initialize armor. Invoked when mob is valid to spawn armor and INIT is not cancelled.
		 */
		ARMOR,
		/**
		 * Initialize weapon and enchantments. Invoked regardless if anything above is cancselled.
		 */
		WEAPON
	}

	private final MobTraitCap cap;
	private final InitPhase phase;

	public HostilityInitEvent(LivingEntity mob, MobTraitCap cap, InitPhase phase) {
		super(mob);
		this.cap = cap;
		this.phase = phase;
	}

	public MobTraitCap getData() {
		return cap;
	}

	public InitPhase getPhase() {
		return phase;
	}

	public static class Pre extends HostilityInitEvent implements ICancellableEvent {

		public Pre(LivingEntity mob, MobTraitCap cap, InitPhase phase) {
			super(mob, cap, phase);
		}

	}

	public static class Post extends HostilityInitEvent {

		public Post(LivingEntity mob, MobTraitCap cap, InitPhase phase) {
			super(mob, cap, phase);
		}

	}

}
