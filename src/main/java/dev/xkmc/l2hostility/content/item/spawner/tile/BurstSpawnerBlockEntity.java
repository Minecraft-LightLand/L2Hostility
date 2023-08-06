package dev.xkmc.l2hostility.content.item.spawner.tile;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.event.ForgeEventFactory;

@SerialClass
public class BurstSpawnerBlockEntity extends TraitSpawnerBlockEntity {

	public BurstSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static int getSpawnGroup() {
		return 32;//TODO config
	}

	@Override
	protected void generate(TraitSpawnerData data) {
		assert level != null;
		var cdcap = ChunkDifficulty.at(level, getBlockPos());
		if (cdcap.isEmpty()) return;
		for (int i = 0; i < getSpawnGroup(); i++) {
			int x = level.getRandom().nextInt(16);
			int y = level.getRandom().nextInt(16);
			int z = level.getRandom().nextInt(16);
			BlockPos pos = new BlockPos(
					getBlockPos().getX() & -8 | x,
					getBlockPos().getY() & -8 | y,
					getBlockPos().getZ() & -8 | z
			);

			MobDifficultyCollector coll = new MobDifficultyCollector();
			cdcap.get().modifyInstance(pos, coll);

		}
	}

	private static WeightedRandomList<MobSpawnSettings.SpawnerData> mobsAt(
			ServerLevel level,
			MobCategory category,
			BlockPos pos) {
		StructureManager structure = level.structureManager();
		ChunkGenerator chunkGen = level.getChunkSource().getGenerator();
		Holder<Biome> biome = level.getBiome(pos);
		return ForgeEventFactory.getPotentialSpawns(
				level, category, pos, chunkGen.getMobsAt(biome, structure, category, pos));
	}

}
