package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class DifficultyLevel {

	@SerialField
	public int level;

	protected long experience;

	@SerialField
	public int extraLevel;

	public static DifficultyLevel merge(DifficultyLevel difficulty, int extraLevel) {
		DifficultyLevel ans = new DifficultyLevel();
		ans.level = difficulty.level;
		ans.experience = difficulty.experience;
		ans.extraLevel = difficulty.extraLevel + extraLevel;
		return ans;
	}

	public static int ofAny(LivingEntity entity) {
		if (entity instanceof Player player) {
			return LHMiscs.PLAYER.type().getOrCreate(player).getLevel(player).getLevel();
		}
		return LHMiscs.MOB.type().getExisting(entity).map(MobTraitCap::getLevel).orElse(0);
	}

	public void grow(double growFactor, MobTraitCap cap) {
		if (level >= LHConfig.SERVER.maxPlayerLevel.get()) {
			level = LHConfig.SERVER.maxPlayerLevel.get();
			experience = 0;
			return;
		}
		experience += (int) (growFactor * cap.getLevel() * cap.getLevel());
		int factor = LHConfig.SERVER.killsPerLevel.get();
		while (experience >= (long) level * level * factor) {
			experience -= (long) level * level * factor;
			level++;
		}
		if (level >= LHConfig.SERVER.maxPlayerLevel.get()) {
			level = LHConfig.SERVER.maxPlayerLevel.get();
			experience = 0;
		}
	}

	public void decay() {
		double rate = LHConfig.SERVER.playerDeathDecay.get();
		if (rate < 1) {
			level = Math.max(0, level - Math.max(1, (int) Math.ceil(level * (1 - rate))));
		}
		experience = 0;
	}

	public long getMaxExp() {
		int factor = LHConfig.SERVER.killsPerLevel.get();
		return Math.max(1L, (long) level * level * factor);
	}

	public int getLevel() {
		return Math.max(0, level + extraLevel);
	}

	public long getExp() {
		return experience;
	}

	public String getStr() {
		return extraLevel == 0 ? "" + level : extraLevel > 0 ? level + "+" + extraLevel : level + "" + extraLevel;
	}
}
