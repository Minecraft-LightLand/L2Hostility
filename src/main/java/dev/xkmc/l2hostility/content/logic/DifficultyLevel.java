package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class DifficultyLevel {

	@SerialClass.SerialField
	public int level, exp;

	@SerialClass.SerialField
	public int extraLevel;

	public static DifficultyLevel merge(DifficultyLevel difficulty, int extraLevel) {
		DifficultyLevel ans = new DifficultyLevel();
		ans.level = difficulty.level;
		ans.exp = difficulty.exp;
		ans.extraLevel = difficulty.extraLevel + extraLevel;
		return ans;
	}

	public static int ofAny(LivingEntity entity) {
		if (entity instanceof Player player) {
			return PlayerDifficulty.HOLDER.get(player).getLevel().getLevel();
		}
		if (MobTraitCap.HOLDER.isProper(entity)) {
			return MobTraitCap.HOLDER.get(entity).getLevel();
		}
		return 0;
	}

	public void grow(double growFactor, MobTraitCap cap) {
		exp += (int) (growFactor * cap.getLevel() * cap.getLevel());
		int factor = LHConfig.COMMON.killsPerLevel.get();
		while (exp >= level * level * factor) {
			exp -= level * level * factor;
			level++;
		}
	}

	public void decay() {
		double rate = LHConfig.COMMON.playerDeathDecay.get();
		if (rate < 1) {
			level = Math.max(0, level - Math.max(1, (int) Math.ceil(level * (1 - rate))));
		}
		exp = 0;
	}

	public int getMaxExp() {
		int factor = LHConfig.COMMON.killsPerLevel.get();
		return Math.max(1, level * level * factor);
	}

	public int getLevel() {
		return Math.max(0, level + extraLevel);
	}

	public int getExp() {
		return exp;
	}

	public String getStr() {
		return extraLevel == 0 ? "" + level : extraLevel > 0 ? level + "+" + extraLevel : level + "" + extraLevel;
	}
}
