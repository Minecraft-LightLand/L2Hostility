package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.legendary.MasterTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerialClass
public class MasterData {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<Minion> data = new ArrayList<>();

	public boolean tick(MobTraitCap cap, Mob mob) {
		var config = MasterTrait.getConfig(mob.getType());
		if (config == null) return false;
		Map<EntityType<?>, Data> map = new HashMap<>();
		for (var e : config.minions())
			map.put(e.type(), new Data(e));
		boolean updated = data.removeIf(e -> {
			e.tick(mob);
			if (e.minion == null) {
				return true;
			} else {
				var ent = map.get(e.minion.getType());
				if (ent != null) ent.count++;
				return false;
			}
		});
		if (mob.level() instanceof ServerLevel sl &&
				mob.getTarget() != null &&
				data.size() < config.maxTotalCount() &&
				mob.tickCount % config.spawnInterval() == 0) {
			for (var e : map.values()) {
				if (data.size() < config.maxTotalCount() &&
						e.count < e.config.maxCount() &&
						cap.getLevel() >= e.config.minLevel()) {
					var nd = e.spawn(cap, sl, mob);
					if (nd != null) {
						data.add(nd);
						updated = true;
					}
				}
			}
		}
		return updated;
	}

	@SerialClass
	public static class Minion {

		@SerialClass.SerialField(toClient = true)
		public UUID uuid;

		@SerialClass.SerialField(toClient = true)
		public int id;

		public Mob minion;

		public void tick(Mob mob) {
			if (mob.level() instanceof ServerLevel sl) {
				if (minion == null) {
					var e = sl.getEntity(uuid);
					if (e instanceof Mob m) {
						minion = m;
					}
				}
				if (minion != null) {
					if (!minion.isAlive()) {
						minion = null;
					}
				}
			} else {
				if (minion == null) {
					var e = mob.level().getEntity(id);
					if (e instanceof Mob m && m.getUUID().equals(uuid)) {
						minion = m;
					}
				}
			}
		}

	}

	public static class Data {

		private final EntityConfig.Minion config;

		private int count;

		public Data(EntityConfig.Minion e) {
			config = e;
		}

		@Nullable
		public Minion spawn(MobTraitCap parent, ServerLevel sl, Mob mob) {
			int r = config.spawnRange();
			var rand = mob.getRandom();
			var eye = mob.getEyePosition();
			BlockPos pos = mob.blockPosition();
			BlockPos target = null;
			for (int i = 0; i < 16; i++) {
				BlockPos p = pos.offset(
						rand.nextInt(0, r * 2 + 1) - r,
						rand.nextInt(0, 3),
						rand.nextInt(0, r * 2 + 1) - r
				);
				if (sl.noCollision(config.type().getAABB(p.getX(), p.getY(), p.getZ()))) {
					Vec3 e = Vec3.atBottomCenterOf(p).add(0, config.type().getHeight() / 2, 0);
					var bhit = sl.clip(new ClipContext(eye, e, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
					if (bhit.getType() == HitResult.Type.MISS) {
						target = p;
						break;
					}
				}
			}
			if (target == null) return null;
			var e = config.type().spawn(sl, target, MobSpawnType.MOB_SUMMONED);
			if (!(e instanceof Mob m)) return null;
			var cap = MobTraitCap.HOLDER.get(m);
			RegionalDifficultyModifier diff = (p, c) -> {
				if (config.copyLevel()) {
					c.base = parent.getLevel();
				} else {
					ChunkDifficulty.at(sl, p).ifPresent(x -> x.modifyInstance(p, c));
				}
				if (config.copyTrait()) {
					cap.traits.putAll(parent.traits);
					c.delegateTrait();
				}
			};
			cap.minion = true;
			cap.asMinion = new MinionData().init(mob, config);
			cap.init(sl, m, diff);
			m.setTarget(mob.getTarget());
			sl.addFreshEntity(m);
			var ans = new Minion();
			ans.minion = m;
			ans.uuid = m.getUUID();
			ans.id = m.getId();
			return ans;
		}


	}

}
