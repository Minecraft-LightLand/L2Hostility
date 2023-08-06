package dev.xkmc.l2hostility.content.capability.player;

import dev.xkmc.l2hostility.content.capability.chunk.InfoRequestToServer;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
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
	private int maxRankKilled = 0;

	@SerialClass.SerialField
	private final TreeSet<ResourceLocation> dimensions = new TreeSet<>();

	public boolean updateChunkFlag = false, pendingFlag = false;

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
		if (player.level().isClientSide()) {
			if (updateChunkFlag && !pendingFlag && player.tickCount % 20 == 0) {
				pendingFlag = true;
				updateChunkFlag = false;
				ChunkAccess chunk = player.level().getChunk(player.blockPosition());
				if (chunk instanceof ImposterProtoChunk c) chunk = c.getWrapped();
				if (chunk instanceof LevelChunk c) L2Hostility.HANDLER.toServer(new InfoRequestToServer(c));
			}
			return;
		}
		if (dimensions.add(player.level().dimension().location())) {
			HOLDER.network.toClientSyncAll((ServerPlayer) player);
		}
	}

	public void apply(MobDifficultyCollector instance) {
		instance.acceptBonus(difficulty);
		instance.setTraitCap(getRankCap());
	}

	public int getRankCap() {
		return TraitManager.getTraitCap(maxRankKilled, difficulty);
	}

	public void addKillCredit(MobTraitCap cap) {
		difficulty.grow(cap);
		cap.traits.values().stream().max(Comparator.naturalOrder())
				.ifPresent(integer -> maxRankKilled = Math.max(maxRankKilled, integer));
		HOLDER.network.toClientSyncAll((ServerPlayer) player);
	}

	public DifficultyLevel getLevel() {
		return DifficultyLevel.merge(difficulty, getExtraLevel());
	}

	private int getExtraLevel() {
		return (dimensions.size() - 1) * LHConfig.COMMON.dimensionFactor.get();
	}


}
