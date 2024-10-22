package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@SerialClass
public abstract class TraitSpawnerBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialField
	public final TraitSpawnerData data = new TraitSpawnerData();

	@Nullable
	protected CustomBossEvent event;

	public TraitSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) {
			return;
		}
		if (getBlockState().getValue(TraitSpawnerBlock.STATE) == TraitSpawnerBlock.State.ACTIVATED) {
			data.init(level);
			var next = data.tick();
			if (next == TraitSpawnerBlock.State.FAILED) {
				stop();
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.FAILED));
				return;
			} else if (next == TraitSpawnerBlock.State.CLEAR) {
				clearStage();
				stop();
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.CLEAR));
				return;
			}
			if (event == null)
				event = createBossEvent();
			int max = data.getMax();
			int alive = data.getAlive();
			event.setMax(max);
			event.setValue(alive);
			event.setName(LangData.BOSS_EVENT.get(max - alive, max).withStyle(ChatFormatting.GOLD));
			Set<ServerPlayer> set = new HashSet<>();
			for (var e : event.getPlayers()) {
				if (e.distanceToSqr(Vec3.atCenterOf(getBlockPos())) > 1024) {
					set.add(e);
				}
			}
			for (var e : set) {
				event.removePlayer(e);
			}
		}
	}

	public void activate() {
		if (level == null || level.isClientSide()) return;
		data.stop();
		if (event != null) {
			event.removeAllPlayers();
		}
		event = createBossEvent();
		generate(data);
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.ACTIVATED));
	}

	@Override
	public void setRemoved() {
		if (event != null) {
			event.removeAllPlayers();
		}
		super.setRemoved();
	}

	public void stop() {
		if (event != null) {
			event.removeAllPlayers();
			event = null;
		}
		data.stop();
	}

	public void deactivate() {
		if (level == null || level.isClientSide()) return;
		stop();
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.IDLE));
	}

	protected abstract void generate(TraitSpawnerData data);

	protected abstract void clearStage();

	protected abstract CustomBossEvent createBossEvent();

	public void track(Player player) {
		if (event != null && player instanceof ServerPlayer sp) {
			event.addPlayer(sp);
		}
	}
}
