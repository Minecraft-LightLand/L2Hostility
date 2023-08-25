package dev.xkmc.l2hostility.content.item.spawner.tile;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

@SerialClass
public class BurstSpawnerBlockEntity extends TraitSpawnerBlockEntity {

	public BurstSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static int getSpawnGroup() {
		return LHConfig.COMMON.hostilitySpawnCount.get();
	}

	public static int getBonusLevel() {
		return LHConfig.COMMON.hostilitySpawnLevelBonus.get();
	}

	@Override
	protected void generate(TraitSpawnerData data) {
		if (!(level instanceof ServerLevel sl)) return;
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
			var e = mobsAt(sl, MobCategory.MONSTER, pos).getRandom(level.getRandom());
			if (e.isPresent()) {
				Entity entity = e.get().type.create(sl);
				if (entity == null) continue;
				entity.setPos(Vec3.atCenterOf(getBlockPos()));
				if (entity instanceof LivingEntity le) {
					if (MobTraitCap.HOLDER.isProper(le)) {
						MobTraitCap cap = MobTraitCap.HOLDER.get(le);
						cap.summoned = true;
						cap.pos = getBlockPos();
						cap.init(level, le, (a, b) -> {
							cdcap.get().modifyInstance(a, b);
							b.acceptBonusLevel(getBonusLevel());
						});
					}
					entity.setPos(Vec3.atCenterOf(getBlockPos().above()));
					data.add(le);
					level.addFreshEntity(entity);
				}
			}

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
