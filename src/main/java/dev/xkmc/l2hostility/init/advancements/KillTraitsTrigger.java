package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2core.serial.advancements.BaseCriterion;
import dev.xkmc.l2core.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitsTrigger extends BaseCriterion<KillTraitsTrigger.Ins, KillTraitsTrigger> {

	public static Criterion<Ins> ins(MobTrait... traits) {
		var ans = new Ins();
		ans.traits = traits;
		return ans.build();
	}

	public KillTraitsTrigger() {
		super(Ins.class);
	}

	public void trigger(ServerPlayer player, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitsTrigger> {

		@SerialField
		public MobTrait[] traits;

		protected Ins() {
			super(HostilityTriggers.KILL_TRAITS.get());
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
