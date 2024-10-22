package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2core.serial.advancements.BaseCriterion;
import dev.xkmc.l2core.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public class KillTraitEffectTrigger extends BaseCriterion<KillTraitEffectTrigger.Ins, KillTraitEffectTrigger> {

	public static Ins ins(MobTrait traits, Holder<MobEffect> effect) {
		var ans = new Ins();
		ans.trait = traits;
		ans.effect = effect;
		return ans;
	}

	public KillTraitEffectTrigger(ResourceLocation id) {
		super(Ins.class);
	}

	public void trigger(ServerPlayer player, LivingEntity le, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(le, cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitEffectTrigger> {

		@SerialField
		public MobTrait trait;

		@SerialField
		public Holder<MobEffect> effect;

		public Ins() {
			super(HostilityTriggers.TRAIT_EFFECT);
		}

		public boolean matchAll(LivingEntity le, MobTraitCap cap) {
			return cap.hasTrait(trait) && le.hasEffect(effect);
		}
	}

}
