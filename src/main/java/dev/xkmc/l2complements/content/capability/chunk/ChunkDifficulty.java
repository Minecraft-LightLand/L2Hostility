package dev.xkmc.l2complements.content.capability.chunk;

import dev.xkmc.l2complements.content.logic.DifficultyInstance;
import dev.xkmc.l2complements.init.L2Hostility;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class ChunkDifficulty {

	public enum Stage {
		PRE_INIT, INIT, CLEARED;
	}

	public static Capability<ChunkDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

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

	public void modifyInstance(BlockPos pos, DifficultyInstance instance) {
		check();
		var levelDiff = L2Hostility.WORLD.getMerged().levelMap.get(chunk.getLevel().dimensionTypeId().location());
		if (levelDiff != null) {
			instance.acceptConfig(levelDiff);
		}
		int index = chunk.getMinSection() + (pos.getY() >> 4);
		if (index >= 0 && index < sections.length) {
			sections[index].modifyInstance(chunk.getLevel().getBiome(pos), instance);
		}
		if (stage == Stage.CLEARED) {
			instance.setCap(0);
		}
	}

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
		private int difficulty = 0;

		private LevelChunkSection section;

		public void modifyInstance(Holder<Biome> biome, DifficultyInstance instance) {
			biome.unwrapKey().map(e -> L2Hostility.WORLD.getMerged().biomeMap.get(e.location())).ifPresent(instance::acceptConfig);
			instance.acceptBonus(difficulty);
		}

	}

}
