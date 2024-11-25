package dev.xkmc.l2hostility.content.capability.player;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkCapSyncToClient;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
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

	@Nullable
	public ChunkDifficulty prevChunk;

	public PlayerDifficulty() {
	}

	public static void register() {
	}

	public void onClone(boolean isWasDeath) {
		if (!isWasDeath) return;
		if (LHConfig.COMMON.keepInventoryRuleKeepDifficulty.get() &&
				world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
			return;
		}
		if (LHConfig.COMMON.deathDecayDimension.get())
			dimensions.clear();
		if (LHConfig.COMMON.deathDecayTraitCap.get())
			if (maxRankKilled > 0) maxRankKilled--;
		difficulty.decay();
	}

	public void tick() {
		if (!(player instanceof ServerPlayer sp)) {
			return;
		}
		var opt = ChunkDifficulty.at(player.level(), player.blockPosition());
		if (opt.isPresent()) {
			var currentChunk = opt.get();
			if (prevChunk != currentChunk) {
				L2Hostility.HANDLER.toClientPlayer(new ChunkCapSyncToClient(currentChunk), sp);
				prevChunk = currentChunk;
			}
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
		instance.setPlayer(player);
		instance.acceptBonus(getLevel());
		instance.setTraitCap(getRankCap());
		if (CurioCompat.hasItemInCurio(player, LHItems.CURSE_PRIDE.get())) {
			instance.traitCostFactor(LHConfig.COMMON.prideTraitFactor.get());
			instance.setFullChance();
		}
		if (CurioCompat.hasItemInCurio(player, LHItems.ABYSSAL_THORN.get())) {
			instance.traitCostFactor(0);
			instance.setFullChance();
			instance.setFullDrop();
		}
		instance.scale += player.getAttributeValue(LHMiscs.ADD_SCALE.get());
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
		if (getLevel().getLevel() > rewardCount * 10 && LHConfig.COMMON.enableHostilityOrbDrop.get()) {
			rewardCount++;
			player.getInventory().add(LHItems.HOSTILITY_ORB.asStack());
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

	private int getDimCount() {
		return Math.max(0, dimensions.size() - 1);
	}

	private int getExtraLevel() {
		int ans = 0;
		ans += getDimCount() * LHConfig.COMMON.dimensionFactor.get();
		ans += (int) player.getAttributeValue(LHMiscs.ADD_LEVEL.get());
		return ans;
	}

	public List<Component> getPlayerDifficultyDetail() {
		int item = (int) player.getAttributeValue(LHMiscs.ADD_LEVEL.get());
		int dim = getDimCount() * LHConfig.COMMON.dimensionFactor.get();
		return List.of(
				LangData.INFO_PLAYER_ADAPTIVE_LEVEL.get(difficulty.level).withStyle(ChatFormatting.GRAY),
				LangData.INFO_PLAYER_ITEM_LEVEL.get(item).withStyle(ChatFormatting.GRAY),
				LangData.INFO_PLAYER_DIM_LEVEL.get(dim).withStyle(ChatFormatting.GRAY),
				LangData.INFO_PLAYER_EXT_LEVEL.get(difficulty.extraLevel).withStyle(ChatFormatting.GRAY)
		);
	}

	public LevelEditor getLevelEditor() {
		return new LevelEditor(difficulty, getExtraLevel());
	}
}
