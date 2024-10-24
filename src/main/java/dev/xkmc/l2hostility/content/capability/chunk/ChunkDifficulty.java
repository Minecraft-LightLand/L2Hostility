package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2core.capability.attachment.GeneralCapabilityTemplate;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.Optional;

@SerialClass
public class ChunkDifficulty extends GeneralCapabilityTemplate<LevelChunk, ChunkDifficulty> {

	public enum ChunkStage {
		PRE_INIT, INIT
	}

	public static Optional<ChunkCapHolder> at(Level level, BlockPos pos) {
		return at(level, pos.getX() >> 4, pos.getZ() >> 4);
	}

	public static Optional<ChunkCapHolder> at(Level level, int x, int z) {
		ChunkAccess chunk = level.getChunk(x, z, ChunkStatus.CARVERS, false);
		if (chunk instanceof ImposterProtoChunk im) {
			chunk = im.getWrapped();
		}
		if (chunk instanceof LevelChunk c) {
			return Optional.of(new ChunkCapHolder(c, LHMiscs.CHUNK.type().getOrCreate(c)));
		}
		return Optional.empty();
	}

	private ChunkStage stage = ChunkStage.PRE_INIT;

	@SerialField
	protected SectionDifficulty[] sections;

	public ChunkDifficulty() {
	}

	protected void check(LevelChunk chunk) {
		int size = chunk.getLevel().getSectionsCount();
		if (sections != null && sections.length == size && stage != ChunkStage.PRE_INIT) return;
		stage = ChunkStage.INIT;
		if (sections == null || sections.length != size) {
			sections = new SectionDifficulty[size];
			for (int i = 0; i < size; i++) {
				sections[i] = new SectionDifficulty();
				sections[i].index = chunk.getMinSection() + i;
			}
		}
		for (int i = 0; i < size; i++) {
			sections[i].section = chunk.getSection(i);
		}
	}

}
