package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2core.serial.advancements.BaseCriterion;
import dev.xkmc.l2core.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitLevelTrigger extends BaseCriterion<KillTraitLevelTrigger.Ins, KillTraitLevelTrigger> {

	public static Ins ins(MobTrait traits, int rank) {
		var ans = new Ins();
		ans.trait = traits;
		ans.rank = rank;
		return ans;
	}

	public KillTraitLevelTrigger() {
		super(Ins.class);
	}

	public void trigger(ServerPlayer player, MobTraitCap cap) {
		this.trigger(player, e -> e.matchAll(cap));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, KillTraitLevelTrigger> {

		@SerialField
		public MobTrait trait;

		@SerialField
		public int rank;

		public Ins() {
			super(HostilityTriggers.TRAIT_LEVEL);
		}

		public boolean matchAll(MobTraitCap cap) {
			return trait != null && cap.getTraitLevel(trait) >= rank;
		}
	}

}
