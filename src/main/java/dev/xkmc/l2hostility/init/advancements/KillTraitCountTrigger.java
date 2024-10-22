package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2core.serial.advancements.BaseCriterion;
import dev.xkmc.l2core.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitCountTrigger extends BaseCriterion<KillTraitCountTrigger.Ins, KillTraitCountTrigger> {

	public static Ins ins(int count) {
		var ans = new Ins();
		ans.count = count;
		return ans;
	}

	public KillTraitCountTrigger() {
		super(Ins.class);
	}

	public void trigger(ServerPlayer player, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitCountTrigger> {

		@SerialField
		public int count;

		protected Ins() {
			super(HostilityTriggers.TRAIT_COUNT);
		}

		public boolean matchAll(MobTraitCap cap) {
			return cap.traits.size() >= count;
		}
	}

}
