package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitLevelTrigger extends BaseCriterion<KillTraitLevelTrigger.Ins, KillTraitLevelTrigger> {

	public static Ins ins(MobTrait traits, int rank) {
		var ans = new Ins(HostilityTriggers.TRAIT_LEVEL.getId(), ContextAwarePredicate.ANY);
		ans.trait = traits;
		ans.rank = rank;
		return ans;
	}

	public KillTraitLevelTrigger(ResourceLocation id) {
		super(id, Ins::new, Ins.class);
	}

	public void trigger(ServerPlayer player, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitLevelTrigger> {

		@SerialClass.SerialField
		public MobTrait trait;

		@SerialClass.SerialField
		public int rank;

		public Ins(ResourceLocation id, ContextAwarePredicate player) {
			super(id, player);
		}

		public boolean matchAll(MobTraitCap cap) {
			return trait != null && cap.getTraitLevel(trait) >= rank;
		}
	}

}
