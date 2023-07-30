package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.Optional;

@SerialClass
public class ChunkDifficulty {

	public enum Stage {
		PRE_INIT, INIT, CLEARED
	}

	public static Capability<ChunkDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static Optional<ChunkDifficulty> at(Level level, BlockPos pos) {
		return at(level, pos.getX() >> 4, pos.getZ() >> 4);
	}

	public static Optional<ChunkDifficulty> at(Level level, int x, int z) {
		ChunkAccess chunk = level.getChunk(x, z, ChunkStatus.CARVERS, false);
		if (chunk instanceof ImposterProtoChunk im) {
			chunk = im.getWrapped();
		}
		if (chunk instanceof LevelChunk c) {
			return c.getCapability(CAPABILITY).resolve();
		}
		return Optional.empty();
	}

	public final LevelChunk chunk;

	@SerialClass.SerialField
	private Stage stage = Stage.PRE_INIT;

	@SerialClass.SerialField
	private Section[] sections;

	protected ChunkDifficulty(LevelChunk chunk) {
		this.chunk = chunk;
	}

	private void check() {
		if (stage != Stage.PRE_INIT) return;
		stage = Stage.INIT;
	}

	public Section getSection(int y) {
		int index = (y >> 4) - chunk.getMinSection();
		index = Mth.clamp(index, 0, sections.length);
		return sections[index];
	}

	public void modifyInstance(BlockPos pos, MobDifficultyCollector instance) {
		check();
		var levelDiff = L2Hostility.DIFFICULTY.getMerged().levelMap.get(chunk.getLevel().dimensionTypeId().location());
		if (levelDiff != null) {
			instance.acceptConfig(levelDiff);
		}
		getSection(pos.getY()).modifyInstance(chunk.getLevel().getBiome(pos), instance);
		if (stage == Stage.CLEARED) {
			instance.setCap(0);
		}
	}

	public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
		BlockPos pos = mob.blockPosition();
		int index = -chunk.getMinSection() + (pos.getY() >> 4);
		if (index >= 0 && index < sections.length) {
			sections[index].addKillHistory(player, mob, cap);
		}
	}

	@SerialClass.OnInject
	public void init() {
		int size = chunk.getLevel().getSectionsCount();
		if (sections == null || sections.length != size) {
			sections = new Section[size];
			for (int i = 0; i < size; i++) {
				sections[i] = new Section();
				sections[i].index = chunk.getMinSection() + i;
			}
		}
		for (int i = 0; i < size; i++) {
			sections[i].section = chunk.getSection(i);
		}
	}

	public static void register() {
	}

	@SerialClass
	public static class Section {

		@SerialClass.SerialField
		private int index;

		@SerialClass.SerialField
		private final DifficultyLevel difficulty = new DifficultyLevel();

		private LevelChunkSection section;

		public void modifyInstance(Holder<Biome> biome, MobDifficultyCollector instance) {
			biome.unwrapKey().map(e -> L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location())).ifPresent(instance::acceptConfig);
			instance.acceptBonus(difficulty);
			//instance.setTraitCap(TraitManager.getTraitCap(0, difficulty));
		}

		public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
			difficulty.grow(cap);
		}

	}

}
