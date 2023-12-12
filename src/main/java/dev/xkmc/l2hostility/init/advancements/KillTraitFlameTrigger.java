package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.base.advancements.BaseCriterion;
import dev.xkmc.l2library.base.advancements.BaseCriterionInstance;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class KillTraitFlameTrigger extends BaseCriterion<KillTraitFlameTrigger.Ins, KillTraitFlameTrigger> {

	public enum Type {
		FLAME(le -> le.isOnFire());

		private final Predicate<LivingEntity> pred;

		Type(Predicate<LivingEntity> pred) {
			this.pred = pred;
		}

		public boolean match(LivingEntity le) {
			return pred.test(le);
		}
	}

	public static Ins ins(MobTrait traits, Type effect) {
		var ans = new Ins(HostilityTriggers.TRAIT_FLAME.getId(), EntityPredicate.Composite.ANY);
		ans.trait = traits;
		ans.effect = effect;
		return ans;
	}

	public KillTraitFlameTrigger(ResourceLocation id) {
		super(id, Ins::new, Ins.class);
	}

	public void trigger(ServerPlayer player, LivingEntity le, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(le, cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitFlameTrigger> {

		@SerialClass.SerialField
		public MobTrait trait;

		@SerialClass.SerialField
		public Type effect;

		public Ins(ResourceLocation id, EntityPredicate.Composite player) {
			super(id, player);
		}

		public boolean matchAll(LivingEntity le, MobTraitCap cap) {
			return cap.hasTrait(trait) && effect.match(le);
		}
	}

}
