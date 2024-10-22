package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2core.capability.attachment.GeneralCapabilityTemplate;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.OnInject;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.Optional;

@SerialClass
public class ChunkDifficulty extends GeneralCapabilityTemplate<LevelChunk, ChunkDifficulty> implements RegionalDifficultyModifier {

	public enum ChunkStage {
		PRE_INIT, INIT
	}

	public static Optional<ChunkDifficulty> at(Level level, BlockPos pos) {
		return at(level, pos.getX() >> 4, pos.getZ() >> 4);
	}

	public static Optional<ChunkDifficulty> at(Level level, int x, int z) {
		ChunkAccess chunk = level.getChunk(x, z, ChunkStatus.CARVERS, false);
		if (chunk instanceof ImposterProtoChunk im) {
			chunk = im.getWrapped();
		}
		if (chunk instanceof LevelChunk c) {
			return Optional.of(LHMiscs.CHUNK.type().getOrCreate(c));
		}
		return Optional.empty();
	}

	public final LevelChunk chunk;

	@SerialField
	private ChunkStage stage = ChunkStage.PRE_INIT;

	@SerialField
	private SectionDifficulty[] sections;

	protected ChunkDifficulty(LevelChunk chunk) {
		this.chunk = chunk;
	}

	private void check() {
		if (stage != ChunkStage.PRE_INIT) return;
		stage = ChunkStage.INIT;
	}

	public SectionDifficulty getSection(int y) {
		int index = (y >> 4) - chunk.getMinSection();
		index = Mth.clamp(index, 0, sections.length - 1);
		return sections[index];
	}

	public void modifyInstance(BlockPos pos, MobDifficultyCollector instance) {
		check();
		getSection(pos.getY()).modifyInstance(chunk.getLevel(), pos, instance);
	}

	public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
		BlockPos pos = mob.blockPosition();
		int index = -chunk.getMinSection() + (pos.getY() >> 4);
		if (index >= 0 && index < sections.length) {
			sections[index].addKillHistory(this, player, mob, cap);
		}
	}

	@OnInject
	public void init() {
		int size = chunk.getLevel().getSectionsCount();
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

	public static void register() {
	}

}
