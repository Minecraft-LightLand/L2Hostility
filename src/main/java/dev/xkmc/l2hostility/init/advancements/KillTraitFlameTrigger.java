package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2core.serial.advancements.BaseCriterion;
import dev.xkmc.l2core.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.advancements.Criterion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class KillTraitFlameTrigger extends BaseCriterion<KillTraitFlameTrigger.Ins, KillTraitFlameTrigger> {

	public enum Type {
		FLAME(Entity::isOnFire);

		private final Predicate<LivingEntity> pred;

		Type(Predicate<LivingEntity> pred) {
			this.pred = pred;
		}

		public boolean match(LivingEntity le) {
			return pred.test(le);
		}
	}

	public static Criterion<Ins> ins(MobTrait traits, Type effect) {
		var ans = new Ins();
		ans.trait = traits;
		ans.effect = effect;
		return ans.build();
	}

	public KillTraitFlameTrigger(ResourceLocation id) {
		super(Ins.class);
	}

	public void trigger(ServerPlayer player, LivingEntity le, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(le, cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitFlameTrigger> {

		@SerialField
		public MobTrait trait;

		@SerialField
		public Type effect;

		public Ins() {
			super(HostilityTriggers.TRAIT_FLAME.get());
		}

		public boolean matchAll(LivingEntity le, MobTraitCap cap) {
			return cap.hasTrait(trait) && effect.match(le);
		}
	}

}
