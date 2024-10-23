package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerFinder {

	@Nullable
	public static Player getNearestPlayer(Level level, LivingEntity le) {
		int safeZone = LHConfig.SERVER.newPlayerProtectRange.get();
		int sr = safeZone * safeZone;

		int lowLv = 0;
		Player lowPl = null;
		double nearDist = 0;
		Player nearPl = null;
		for (var pl : level.players()) {
			double dist = pl.distanceToSqr(le);
			if (dist > 128 * 128)
				continue;
			if (!pl.isAlive())
				continue;
			var plOpt = LHMiscs.PLAYER.type().getOrCreate(pl);
			int lv = plOpt.getLevel(pl).getLevel();
			if (dist < sr) {
				if (lowPl == null || lv < lowLv) {
					lowPl = pl;
					lowLv = lv;
				}
			} else {
				if (nearPl == null || dist < nearDist) {
					nearPl = pl;
					nearDist = dist;
				}
			}
		}
		if (lowPl != null) return lowPl;
		else return nearPl;
	}

}
