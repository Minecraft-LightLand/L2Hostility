package dev.xkmc.l2hostility.content.item.spawner.tile;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

@SerialClass
public class TraitSpawnerData {

	public enum EntityState {
		ALIVE, DEAD, MISSING
	}

	@SerialClass.SerialField
	private final ArrayList<TrackedEntity> list = new ArrayList<>();

	@SerialClass
	public static class TrackedEntity {

		@SerialClass.SerialField
		public UUID uuid;

		@SerialClass.SerialField
		public int uid;

		@SerialClass.SerialField
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
		for (var e : list) {
			if (level instanceof ServerLevel sl) {
				e.serverInit(sl);
			} else {
				e.clientInit(level);
			}
		}
	}

	protected void add(LivingEntity le) {
		TrackedEntity entry = new TrackedEntity();
		entry.uuid = le.getUUID();
		entry.uid = le.getId();
		entry.entity = le;
		entry.state = EntityState.ALIVE;
		list.add(entry);
	}

	protected boolean tick() {
		boolean hasMissing = false;
		for (var e : list) {
			e.tick();
			hasMissing |= e.state == EntityState.MISSING;
		}
		return hasMissing;
	}

	protected void stop() {
		for (var e : list) {
			if (e.getEntity() == null) continue;
			if (e.state == EntityState.ALIVE) {
				e.getEntity().discard();
			}
		}
		list.clear();
	}

}
