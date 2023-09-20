package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlock;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

@SerialClass
public class TraitSpawnerData {

	public enum EntityState {
		ALIVE, DEAD, MISSING
	}

	@SerialClass.SerialField
	private final HashMap<UUID, TrackedEntity> list = new HashMap<>();

	private boolean init = false;

	@SerialClass
	public static class TrackedEntity {

		@SerialClass.SerialField
		public UUID uuid;

		@SerialClass.SerialField(toClient = true)
		public int uid;

		@SerialClass.SerialField(toClient = true)
		public EntityState state;

		@Nullable
		private LivingEntity entity;

		private void serverInit(ServerLevel level) {
			uid = -1;
			entity = null;
			if (state == EntityState.ALIVE) {
				if (level.getEntity(uuid) instanceof LivingEntity le) {
					entity = le;
					uid = le.getId();
				} else {
					state = EntityState.MISSING;
				}
			}
		}

		private void clientInit(Level level) {
			if (uid > 0 && level.getEntity(uid) instanceof LivingEntity le) {
				entity = le;
			} else {
				entity = null;
			}
		}

		@Nullable
		public LivingEntity getEntity() {
			if (state != EntityState.ALIVE) return null;
			return entity == null ? null : entity.isDeadOrDying() ? null : entity;
		}

		public void tick() {
			if (state != EntityState.ALIVE) return;
			if (entity == null) {
				state = EntityState.MISSING;
				return;
			}
			if (entity.isRemoved() || entity.isDeadOrDying()) {
				state = EntityState.MISSING;
			}
		}

	}

	protected void init(Level level) {
		if (init) return;
		for (var e : list.values()) {
			if (level instanceof ServerLevel sl) {
				e.serverInit(sl);
			} else {
				e.clientInit(level);
			}
		}
		init = true;
	}

	protected void add(LivingEntity le) {
		TrackedEntity entry = new TrackedEntity();
		entry.uuid = le.getUUID();
		entry.uid = le.getId();
		entry.entity = le;
		entry.state = EntityState.ALIVE;
		list.put(entry.uuid, entry);
	}

	public void onDeath(LivingEntity mob) {
		var ans = list.get(mob.getUUID());
		if (ans != null) {
			ans.state = EntityState.DEAD;
		}
	}

	protected TraitSpawnerBlock.State tick() {
		boolean hasMissing = false;
		boolean hasAlive = false;
		for (var e : list.values()) {
			e.tick();
			hasMissing |= e.state == EntityState.MISSING;
			hasAlive |= e.state == EntityState.ALIVE;
		}
		return hasMissing ? TraitSpawnerBlock.State.FAILED : hasAlive ? TraitSpawnerBlock.State.ACTIVATED : TraitSpawnerBlock.State.CLEAR;
	}

	protected void stop() {
		for (var e : list.values()) {
			if (e.getEntity() == null) continue;
			if (e.state == EntityState.ALIVE) {
				e.getEntity().discard();
			}
		}
		list.clear();
	}

	protected int getMax() {
		return list.size();
	}

	protected int getAlive() {
		int i = 0;
		for (var e : list.values()) {
			if (e.state == EntityState.ALIVE) {
				i++;
			}
		}
		return i;
	}

}
