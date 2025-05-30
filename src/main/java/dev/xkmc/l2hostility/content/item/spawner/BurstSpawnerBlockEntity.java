package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

@SerialClass
public class BurstSpawnerBlockEntity extends TraitSpawnerBlockEntity {

	private static WeightedRandomList<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel level, BlockPos pos) {
		StructureManager structure = level.structureManager();
		ChunkGenerator chunkGen = level.getChunkSource().getGenerator();
		Holder<Biome> biome = level.getBiome(pos);
		return EventHooks.getPotentialSpawns(
				level, MobCategory.MONSTER, pos, chunkGen.getMobsAt(biome, structure, MobCategory.MONSTER, pos));
	}

	public static int getSpawnGroup() {
		return LHConfig.SERVER.hostilitySpawnCount.get();
	}

	public static double getBonusFactor() {
		return LHConfig.SERVER.hostilitySpawnLevelFactor.get();
	}

	public static int getMaxTrials() {
		return 4;
	}

	public BurstSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void generate(TraitSpawnerData data) {
		if (!(level instanceof ServerLevel sl)) return;
		var cdcap = ChunkDifficulty.at(level, getBlockPos());
		if (cdcap.isEmpty()) return;
		var sec = cdcap.get().getSection(getBlockPos().getY());
		if (sec.activePos != null && level.isLoaded(sec.activePos)) {
			if (level.getBlockEntity(sec.activePos) instanceof BurstSpawnerBlockEntity other && other != this) {
				other.stop();
			}
		}
		sec.activePos = getBlockPos();
		int count = 0;
		for (int i = 0; i < getSpawnGroup() * getMaxTrials(); i++) {
			int x = level.getRandom().nextInt(16);
			int y = level.getRandom().nextInt(16);
			int z = level.getRandom().nextInt(16);
			BlockPos pos = new BlockPos(
					getBlockPos().getX() & -8 | x,
					getBlockPos().getY() & -8 | y,
					getBlockPos().getZ() & -8 | z
			);
			var e = mobsAt(sl, pos).getRandom(level.getRandom());
			if (e.isPresent()) {
				if (e.get().type.is(LHTagGen.HOSTILITY_SPAWNER_BLACKLIST)) {
					continue;
				}
				Entity entity = e.get().type.create(sl);
				if (entity == null) continue;
				if (entity instanceof Creeper) continue;
				entity.setPos(Vec3.atCenterOf(getBlockPos()));
				if (entity instanceof LivingEntity le) {
					var cap = LHMiscs.MOB.type().getOrCreate(le);
					cap.summoned = true;
					cap.noDrop = true;
					cap.pos = getBlockPos();
					cap.init(level, le, (a, b) -> {
						cdcap.get().modifyInstance(a, b);
						b.acceptBonusFactor(getBonusFactor());
						b.setFullChance();
					});
					entity.setPos(Vec3.atCenterOf(getBlockPos().above()));
					data.add(le);
					level.addFreshEntity(entity);
					count++;
					if (count >= getSpawnGroup()) {
						break;
					}
				}
			}
		}
	}

	@Override
	protected void clearStage() {
		assert level != null;
		var cdcap = ChunkDifficulty.at(level, getBlockPos());
		if (cdcap.isPresent()) {
			var section = cdcap.get().getSection(getBlockPos().getY());
			section.setClear(cdcap.get(), getBlockPos());
			section.activePos = null;
		}
	}

	@Override
	protected CustomBossEvent createBossEvent() {
		var ans = new CustomBossEvent(L2Hostility.loc("hostility_spawner"),
				LangData.BOSS_EVENT.get(0, getSpawnGroup()).withStyle(ChatFormatting.GOLD));
		ans.setColor(BossEvent.BossBarColor.PURPLE);
		ans.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
		return ans;
	}


}
