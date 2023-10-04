package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

@SerialClass
public class MobTraitCap extends GeneralCapabilityTemplate<LivingEntity, MobTraitCap> {

	public static final Capability<MobTraitCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final GeneralCapabilityHolder<LivingEntity, MobTraitCap> HOLDER =
			new GeneralCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "traits"),
					CAPABILITY, MobTraitCap.class, MobTraitCap::new, LivingEntity.class, (e) ->
					e instanceof Enemy && !e.getType().is(TagGen.BLACKLIST));

	public enum Stage {
		PRE_INIT, INIT, POST_INIT
	}

	@SerialClass.SerialField(toClient = true)
	public final LinkedHashMap<MobTrait, Integer> traits = new LinkedHashMap<>();

	@SerialClass.SerialField(toClient = true)
	private Stage stage = Stage.PRE_INIT;

	@SerialClass.SerialField(toClient = true)
	private int lv;

	@SerialClass.SerialField
	private final HashMap<ResourceLocation, CapStorageData> data = new HashMap<>();

	@SerialClass.SerialField(toClient = true)
	public boolean summoned = false;

	@Nullable
	@SerialClass.SerialField
	public BlockPos pos = null;

	@Nullable
	private TraitSpawnerBlockEntity summoner = null;

	public MobTraitCap() {
	}

	public void syncToClient(LivingEntity entity) {
		L2Hostility.HANDLER.toTrackingPlayers(new CapSyncPacket(entity, this), entity);
	}

	public void syncToPlayer(LivingEntity entity, ServerPlayer player) {
		L2Hostility.HANDLER.toClientPlayer(new CapSyncPacket(entity, this), player);
	}

	public static void register() {
	}

	public void init(Level level, LivingEntity le, RegionalDifficultyModifier difficulty) {
		boolean skip = false;
		MobDifficultyCollector instance = new MobDifficultyCollector();
		var diff = L2Hostility.ENTITY.getMerged().get(le.getType());
		if (diff != null) {
			instance.acceptConfig(diff.difficulty());
		}
		difficulty.modifyInstance(le.blockPosition(), instance);
		Player player = level.getNearestPlayer(le, 128);
		if (player != null && PlayerDifficulty.HOLDER.isProper(player)) {
			PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
			playerDiff.apply(instance);
			if (le instanceof OwnableEntity own && own.getOwner() == player) {
				skip = true;
			}
		}
		lv = skip ? 0 : TraitManager.fill(le, traits, instance);
		stage = Stage.INIT;
		syncToClient(le);
	}

	public int getEnchantBonus() {
		return (int) (lv * LHConfig.COMMON.enchantmentFactor.get());
	}

	public int getLevel() {
		return lv;
	}

	public boolean isInitialized() {
		return stage != Stage.PRE_INIT;
	}

	public void tick(LivingEntity mob) {
		if (!mob.level().isClientSide()) {
			if (!isInitialized()) {
				var opt = ChunkDifficulty.at(mob.level(), mob.blockPosition());
				opt.ifPresent(chunkDifficulty -> init(mob.level(), mob, chunkDifficulty));
			}
			if (stage == Stage.INIT) {
				stage = Stage.POST_INIT;
				TraitManager.postFill(this, mob);
				traits.forEach((k, v) -> k.postInit(mob, v));
				mob.setHealth(mob.getMaxHealth());
				syncToClient(mob);
			}
		}
		if (isInitialized()) {
			traits.forEach((k, v) -> k.tick(mob, v));
		}
		if (!mob.level().isClientSide() && pos != null) {
			if (summoner == null) {
				if (mob.level().getBlockEntity(pos) instanceof TraitSpawnerBlockEntity be) {
					summoner = be;
				}
			}
			if (summoner == null || summoner.isRemoved()) {
				mob.discard();
			}
		}
	}

	public void onKilled(LivingEntity mob) {
		if (summoner != null && !summoner.isRemoved()) {
			summoner.data.onDeath(mob);
		}
	}

	@Nullable
	public <T extends CapStorageData> T getData(ResourceLocation id) {
		return Wrappers.cast(data.get(id));
	}

	public <T extends CapStorageData> T getOrCreateData(ResourceLocation id, Supplier<T> sup) {
		return Wrappers.cast(data.computeIfAbsent(id, e -> sup.get()));
	}

	public List<Component> getTitle(boolean showLevel, boolean showTrait) {
		List<Component> ans = new ArrayList<>();
		if (showLevel && lv > 0) {
			ans.add(Component.literal("Lv." + lv).withStyle(ChatFormatting.GRAY));
		}
		if (!showTrait) return ans;
		MutableComponent temp = null;
		int count = 0;
		for (var e : traits.entrySet()) {
			var comp = e.getKey().getFullDesc(e.getValue());
			if (temp == null) {
				temp = comp;
				count = 1;
			} else {
				temp.append(Component.literal(" / ").withStyle(ChatFormatting.WHITE)).append(comp);
				count++;
				if (count >= 3) {
					ans.add(temp);
					count = 0;
					temp = null;
				}
			}
		}
		if (count > 0) {
			ans.add(temp);
		}
		return ans;
	}
}
