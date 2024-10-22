package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.events.CapabilityEvents;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.List;
import java.util.Optional;

@SerialClass
public class SectionDifficulty {

	public enum SectionStage {
		INIT, CLEARED
	}

	public static Optional<SectionDifficulty> sectionAt(Level level, BlockPos pos) {
		return ChunkDifficulty.at(level, pos).map(e -> e.getSection(pos.getY()));
	}

	@SerialField
	int index;

	@SerialField
	public BlockPos activePos = null;

	@SerialField
	private final DifficultyLevel difficulty = new DifficultyLevel();

	@SerialField
	private SectionStage stage = SectionStage.INIT;

	LevelChunkSection section;

	public void modifyInstance(Level level, BlockPos pos, MobDifficultyCollector instance) {
		modifyInstanceInternal(level, pos, instance);
		if (LHConfig.SERVER.allowSectionDifficulty.get())
			instance.acceptBonusLevel(difficulty.getLevel());
		if (stage == SectionStage.CLEARED) {
			instance.setCap(0);
		}
	}

	private void modifyInstanceInternal(Level level, BlockPos pos, MobDifficultyCollector instance) {
		var levelDiff = L2Hostility.DIFFICULTY.getMerged()
				.levelMap.get(level.dimension().location());
		if (levelDiff == null) {
			levelDiff = WorldDifficultyConfig.defaultLevel();
		}
		instance.acceptConfig(levelDiff);
		Holder<Biome> biome = level.getBiome(pos);
		biome.unwrapKey().map(e -> L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location())).ifPresent(instance::acceptConfig);
		instance.acceptBonusLevel((int) Math.round(LHConfig.SERVER.distanceFactor.get() *
				Math.sqrt(1d * pos.getX() * pos.getX() + 1d * pos.getZ() * pos.getZ())));
	}

	public List<Component> getSectionDifficultyDetail(Player player) {
		if (isCleared()) return List.of();
		var levelDiff = L2Hostility.DIFFICULTY.getMerged()
				.levelMap.get(player.level().dimension().location());
		int dim = levelDiff == null ? WorldDifficultyConfig.defaultLevel().base() : levelDiff.base();
		BlockPos pos = player.blockPosition();
		Holder<Biome> biome = player.level().getBiome(pos);
		int bio = biome.unwrapKey().map(e -> L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location()))
				.map(WorldDifficultyConfig.DifficultyConfig::base).orElse(0);
		int dist = (int) Math.round(LHConfig.SERVER.distanceFactor.get() *
				Math.sqrt(pos.getX() * pos.getX() + pos.getZ() * pos.getZ()));
		int adaptive = difficulty.getLevel();
		return List.of(
				LangData.INFO_SECTION_DIM_LEVEL.get(dim).withStyle(ChatFormatting.GRAY),
				LangData.INFO_SECTION_BIOME_LEVEL.get(bio).withStyle(ChatFormatting.GRAY),
				LangData.INFO_SECTION_DISTANCE_LEVEL.get(dist).withStyle(ChatFormatting.GRAY),
				LangData.INFO_SECTION_ADAPTIVE_LEVEL.get(adaptive).withStyle(ChatFormatting.GRAY)
		);
	}

	public boolean isCleared() {
		return stage == SectionStage.CLEARED;
	}

	public boolean setClear(ChunkDifficulty chunk, BlockPos pos) {
		if (stage == SectionStage.CLEARED) return false;
		stage = SectionStage.CLEARED;
		CapabilityEvents.markDirty(chunk);
		chunk.chunk.setUnsaved(true);
		return true;
	}

	public boolean setUnclear(ChunkDifficulty chunk, BlockPos pos) {
		if (stage == SectionStage.INIT) return false;
		stage = SectionStage.INIT;
		CapabilityEvents.markDirty(chunk);
		chunk.chunk.setUnsaved(true);
		return true;
	}

	public void addKillHistory(ChunkDifficulty chunk, Player player, LivingEntity mob, MobTraitCap cap) {
		difficulty.grow(1, cap);
		CapabilityEvents.markDirty(chunk);
		chunk.chunk.setUnsaved(true);
	}

	public LevelEditor getLevelEditor(Level level, BlockPos pos) {
		MobDifficultyCollector col = new MobDifficultyCollector();
		modifyInstanceInternal(level, pos, col);
		var diff = LHConfig.SERVER.allowSectionDifficulty.get() ?
				difficulty : new DifficultyLevel();
		return new LevelEditor(diff, col.getBase());
	}

	public double getScale(Level level, BlockPos pos) {
		MobDifficultyCollector col = new MobDifficultyCollector();
		modifyInstanceInternal(level, pos, col);
		return col.scale;
	}

}
