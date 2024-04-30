package dev.xkmc.l2hostility.compat.gateway;

import dev.xkmc.l2hostility.content.config.SpecialConfigCondition;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class GatewayCondition extends SpecialConfigCondition<WaveData> {

	@SerialClass.SerialField
	public int minWave, maxWave, maxCount;

	@SerialClass.SerialField
	public double chance;

	public GatewayCondition() {
		super(WaveData.class);
	}

	@Override
	public boolean test(WaveData wave) {
		int index = wave.id.wave();
		if (index < minWave || maxWave >= 0 && index > maxWave) {
			return false;
		}
		int val = wave.appliedCount.getOrDefault(this, 0);
		if (val >= maxCount) {
			return false;
		}
		wave.appliedCount.put(this, val + 1);
		return true;
	}

}
