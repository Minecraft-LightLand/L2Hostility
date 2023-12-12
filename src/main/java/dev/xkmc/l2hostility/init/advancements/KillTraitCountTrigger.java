package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2library.base.advancements.BaseCriterion;
import dev.xkmc.l2library.base.advancements.BaseCriterionInstance;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitCountTrigger extends BaseCriterion<KillTraitCountTrigger.Ins, KillTraitCountTrigger> {

	public static Ins ins(int count) {
		var ans = new Ins(HostilityTriggers.TRAIT_COUNT.getId(), EntityPredicate.Composite.ANY);
		ans.count = count;
		return ans;
	}

	public KillTraitCountTrigger(ResourceLocation id) {
		super(id, Ins::new, Ins.class);
	}

	public void trigger(ServerPlayer player, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitCountTrigger> {

		@SerialClass.SerialField
		public int count;

		public Ins(ResourceLocation id, EntityPredicate.Composite player) {
			super(id, player);
		}

		public boolean matchAll(MobTraitCap cap) {
			return cap.traits.size() >= count;
		}
	}

}
