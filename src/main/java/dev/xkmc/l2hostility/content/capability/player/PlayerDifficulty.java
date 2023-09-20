package dev.xkmc.l2hostility.content.capability.player;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.InfoRequestToServer;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.item.curio.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.Comparator;
import java.util.TreeSet;

@SerialClass
public class PlayerDifficulty extends PlayerCapabilityTemplate<PlayerDifficulty> {

	public static final Capability<PlayerDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<PlayerDifficulty> HOLDER =
			new PlayerCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "player"), CAPABILITY,
					PlayerDifficulty.class, PlayerDifficulty::new, PlayerCapabilityNetworkHandler::new);

	@SerialClass.SerialField
	private final DifficultyLevel difficulty = new DifficultyLevel();

	@SerialClass.SerialField
	public int maxRankKilled = 0, rewardCount = 0;

	@SerialClass.SerialField
	public final TreeSet<ResourceLocation> dimensions = new TreeSet<>();

	public boolean updateChunkFlag = false, pendingFlag = false;

	private int updateCooldown = 0, pendingTimeout = 0;

	public PlayerDifficulty() {
	}

	public static void register() {
	}

	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			difficulty.decay();
		}
	}

	public void tick() {
		var opt = ChunkDifficulty.at(player.level(), player.blockPosition());
		if (player.level().isClientSide()) {
			if (updateChunkFlag) {
				if (pendingFlag) {
					if (pendingTimeout > 0) {
						pendingTimeout--;
					} else {
						pendingFlag = false;
					}
				}
				if (!pendingFlag) {
					if (updateCooldown > 0) {
						updateCooldown--;
					} else {
						pendingFlag = true;
						updateChunkFlag = false;
						updateCooldown = 10;
						pendingTimeout = 100;
						opt.ifPresent(chunkDifficulty -> L2Hostility.HANDLER.toServer(new InfoRequestToServer(chunkDifficulty.chunk)));
					}
				}
			}
			return;
		}
		if (opt.isPresent()) {
			var sec = opt.get().getSection(player.blockPosition().getY());
			if (sec.activePos != null) {
				if (player.level().isLoaded(sec.activePos)) {
					if (player.level().getBlockEntity(sec.activePos) instanceof TraitSpawnerBlockEntity spawner) {
						spawner.track(player);
					}
				}
			}
		}
		if (dimensions.add(player.level().dimension().location())) {
			sync();
		}
	}

	public void sync() {
		HOLDER.network.toClientSyncAll((ServerPlayer) player);
	}

	public void apply(MobDifficultyCollector instance) {
		instance.acceptBonus(difficulty);
		instance.setTraitCap(getRankCap());
		if (CurioCompat.hasItem(player, LHItems.CURSE_PRIDE.get())) {
			instance.traitCostFactor(LHConfig.COMMON.prideTraitFactor.get());
			instance.setFullChance();
		}
	}

	public int getRankCap() {
		return TraitManager.getTraitCap(maxRankKilled, difficulty);
	}

	public void addKillCredit(MobTraitCap cap) {
		double growFactor = 1;
		for (var stack : CurseCurioItem.getFromPlayer(player)) {
			growFactor *= stack.item().getGrowFactor(stack.stack(), this, cap);
		}
		difficulty.grow(growFactor, cap);
		cap.traits.values().stream().max(Comparator.naturalOrder())
				.ifPresent(integer -> maxRankKilled = Math.max(maxRankKilled, integer));
		if (getLevel().getLevel() > rewardCount * 10) {
			rewardCount++;
			player.addItem(LHItems.HOSTILITY_ORB.asStack());
			// TODO drop reward
		}
		sync();
	}

	public int getRewardCount() {
		return rewardCount;
	}

	public DifficultyLevel getLevel() {
		return DifficultyLevel.merge(difficulty, getExtraLevel());
	}

	private int getExtraLevel() {
		int ans = 0;
		int count = Math.max(0, dimensions.size() - 1);
		ans += count * LHConfig.COMMON.dimensionFactor.get();
		for (var stack : CurseCurioItem.getFromPlayer(player)) {
			ans += stack.item().getExtraLevel(stack.stack());
		}
		return ans;
	}

	public LevelEditor getLevelEditor() {
		return new LevelEditor(difficulty, getExtraLevel());
	}
}
