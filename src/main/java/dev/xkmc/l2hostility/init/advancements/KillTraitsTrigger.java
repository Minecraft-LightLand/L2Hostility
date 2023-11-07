package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitsTrigger extends BaseCriterion<KillTraitsTrigger.Ins, KillTraitsTrigger> {

	public static Ins ins(MobTrait... traits) {
		var ans = new Ins(HostilityTriggers.KILL_TRAITS.getId(), ContextAwarePredicate.ANY);
		ans.traits = traits;
		return ans;
	}

	public KillTraitsTrigger(ResourceLocation id) {
		super(id, Ins::new, Ins.class);
	}

	public void trigger(ServerPlayer player, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitsTrigger> {

		@SerialClass.SerialField
		public MobTrait[] traits;

		public Ins(ResourceLocation id, ContextAwarePredicate player) {
			super(id, player);
		}

		public boolean matchAll(MobTraitCap cap) {
			if (cap.traits.isEmpty()) return false;
			for (var e : traits) {
				if (!cap.hasTrait(e)) {
					return false;
				}
			}
			return true;
		}
	}

}
