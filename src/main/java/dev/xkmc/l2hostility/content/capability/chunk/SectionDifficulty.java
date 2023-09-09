package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.Optional;

@SerialClass
public class SectionDifficulty {

	public enum SectionStage {
		INIT, CLEARED
	}

	public static Optional<SectionDifficulty> sectionAt(Level level, BlockPos pos) {
		return ChunkDifficulty.at(level, pos).map(e -> e.getSection(pos.getY()));
	}

	@SerialClass.SerialField
	int index;

	@SerialClass.SerialField
	public BlockPos activePos = null;

	@SerialClass.SerialField(toClient = true)
	private final DifficultyLevel difficulty = new DifficultyLevel();

	@SerialClass.SerialField(toClient = true)
	private SectionStage stage = SectionStage.INIT;

	LevelChunkSection section;

	public void modifyInstance(Holder<Biome> biome, MobDifficultyCollector instance) {
		biome.unwrapKey().map(e -> L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location())).ifPresent(instance::acceptConfig);
		if (LHConfig.COMMON.allowSectionDifficulty.get())
			instance.acceptBonus(difficulty);
		if (stage == SectionStage.CLEARED) {
			instance.setCap(0);
		}
	}

	public boolean isCleared() {
		return stage == SectionStage.CLEARED;
	}

	public void setClear(ChunkDifficulty chunk, BlockPos pos) {
		stage = SectionStage.CLEARED;
		L2Hostility.toTrackingChunk(chunk.chunk, new TraitEffectToClient(pos, TraitEffects.CLEAR));
		chunk.chunk.setUnsaved(true);
	}

	public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
		difficulty.grow(cap);
	}

}
