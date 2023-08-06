package dev.xkmc.l2hostility.content.item.spawner.tile;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.item.spawner.block.TraitSpawnerBlock;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public abstract class TraitSpawnerBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialClass.SerialField
	private final TraitSpawnerData data = new TraitSpawnerData();

	public TraitSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (level != null) {
			data.init(level);
		}
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) {
			return;
		}
		if (getBlockState().getValue(TraitSpawnerBlock.STATE) == TraitSpawnerBlock.State.ACTIVATED) {
			if (data.tick()) {
				data.stop();
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.FAILED));
			}
		}
	}

	public void activate() {
		if (level == null || level.isClientSide()) return;
		data.stop();
		generate(data);
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.ACTIVATED));
	}

	public void deactivate() {
		if (level == null || level.isClientSide()) return;
		data.stop();
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.IDLE));
	}

	protected abstract void generate(TraitSpawnerData data);

}
