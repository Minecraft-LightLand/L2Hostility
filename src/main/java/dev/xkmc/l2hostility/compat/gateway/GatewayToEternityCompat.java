package dev.xkmc.l2hostility.compat.gateway;

import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.gateways.entity.GatewayEntity;
import dev.shadowsoffire.gateways.event.GateEvent;
import dev.shadowsoffire.gateways.gate.GatewayRegistry;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GatewayToEternityCompat {

	private static final ThreadLocal<Pair<GatewayEntity, WaveData>> CURRENT = new ThreadLocal<>();

	@SubscribeEvent
	public static void onSpawn(GateEvent.WaveEntitySpawned event) {
		var prev = CURRENT.get();
		var gate = event.getEntity();
		int wave = event.getEntity().getWave();
		var rl = GatewayRegistry.INSTANCE.getKey(event.getEntity().getGateway());
		if (rl == null) return;
		WaveId id = new WaveId(rl, wave);
		WaveData data;
		if (prev == null || gate != prev.getFirst() || !prev.getSecond().id.equals(id)) {
			CURRENT.set(Pair.of(gate, data = new WaveData(id)));
		} else data = prev.getSecond();
		var config = L2Hostility.ENTITY.getMerged().get(event.getWaveEntity().getType(), rl, WaveData.class, data);
		if (config != null) initMob(event.getWaveEntity(), config);
	}

	private static void initMob(LivingEntity mob, EntityConfig.Config config) {
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (!mob.level().isClientSide() && !cap.isInitialized()) {
				var opt = ChunkDifficulty.at(mob.level(), mob.blockPosition());
				if (opt.isPresent()) {
					cap.setConfigCache(config);
					cap.init(mob.level(), mob, opt.get());
					cap.dropRate = LHConfig.COMMON.dropRateFromSpawner.get();
				}
			}
		}
	}


}
