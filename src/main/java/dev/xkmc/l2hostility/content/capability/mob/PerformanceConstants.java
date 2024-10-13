package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.init.data.LHConfig;

public class PerformanceConstants {

	public static final int NAN_FIX = 10;
	public static final int CHUNK_RENDER = 2;

	public static int removeTraitInterval() {
		return LHConfig.COMMON.removeTraitCheckInterval.get();
	}

	public static int auraApplyInterval() {
		return LHConfig.COMMON.auraEffectApplicationInterval.get();
	}

	public static int selfEffectInterval() {
		return LHConfig.COMMON.selfEffectApplicationInterval.get();
	}

}
