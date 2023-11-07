package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
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

	public void modifyInstance(Level level, BlockPos pos, MobDifficultyCollector instance) {
		modifyInstanceInternal(level, pos, instance);
		if (LHConfig.COMMON.allowSectionDifficulty.get())
			instance.acceptBonusLevel(difficulty.getLevel());
		if (stage == SectionStage.CLEARED) {
			instance.setCap(0);
		}
	}

	private void modifyInstanceInternal(Level level, BlockPos pos, MobDifficultyCollector instance) {
		var levelDiff = L2Hostility.DIFFICULTY.getMerged()
				.levelMap.get(level.dimensionTypeId().location());
		if (levelDiff == null) {
			levelDiff = WorldDifficultyConfig.defaultLevel();
		}
		instance.acceptConfig(levelDiff);
		Holder<Biome> biome = level.getBiome(pos);
		biome.unwrapKey().map(e -> L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location())).ifPresent(instance::acceptConfig);
		instance.acceptBonusLevel((int) Math.round(LHConfig.COMMON.distanceFactor.get() *
				Math.sqrt(pos.getX() * pos.getX() + pos.getZ() * pos.getZ())));
	}

	public boolean isCleared() {
		return stage == SectionStage.CLEARED;
	}

	public boolean setClear(ChunkDifficulty chunk, BlockPos pos) {
		if (stage == SectionStage.CLEARED) return false;
		stage = SectionStage.CLEARED;
		L2Hostility.toTrackingChunk(chunk.chunk, new TraitEffectToClient(pos, TraitEffects.CLEAR));
		chunk.chunk.setUnsaved(true);
		return true;
	}

	public boolean setUnclear(ChunkDifficulty chunk, BlockPos pos) {
		if (stage == SectionStage.INIT) return false;
		stage = SectionStage.INIT;
		L2Hostility.toTrackingChunk(chunk.chunk, new TraitEffectToClient(pos, TraitEffects.CLEAR));
		chunk.chunk.setUnsaved(true);
		return true;
	}

	public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
		difficulty.grow(1, cap);
	}

	public LevelEditor getLevelEditor(Level level, BlockPos pos) {
		MobDifficultyCollector col = new MobDifficultyCollector();
		modifyInstanceInternal(level, pos, col);
		var diff = LHConfig.COMMON.allowSectionDifficulty.get() ?
				difficulty : new DifficultyLevel();
		return new LevelEditor(diff, col.getBase());
	}

	public double getScale(Level level, BlockPos pos) {
		MobDifficultyCollector col = new MobDifficultyCollector();
		modifyInstanceInternal(level, pos, col);
		return col.scale;
	}

}
