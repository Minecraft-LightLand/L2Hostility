package dev.xkmc.l2complements.content.capability.chunk;

import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkDifficultyCap implements ICapabilitySerializable<CompoundTag> {

	public final LevelChunk w;
	public final ChunkDifficulty handler;
	public final LazyOptional<ChunkDifficulty> lo;

	public ChunkDifficultyCap(LevelChunk level) {
		this.w = level;
		handler = new ChunkDifficulty(level);
		lo = LazyOptional.of(() -> this.handler);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
		if (capability == ChunkDifficulty.CAPABILITY)
			return lo.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return TagCodec.toTag(new CompoundTag(), lo.resolve().get());
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		Wrappers.get(() -> TagCodec.fromTag(tag, ChunkDifficulty.class, handler, f -> true));
		handler.init();
	}

}
