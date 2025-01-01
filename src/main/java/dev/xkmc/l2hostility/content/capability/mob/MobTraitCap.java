package dev.xkmc.l2hostility.content.capability.mob;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.logic.*;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.ClientEvents;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.advancements.HostilityTriggers;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SerialClass
public class MobTraitCap extends GeneralCapabilityTemplate<LivingEntity, MobTraitCap> {

	public static final Capability<MobTraitCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final GeneralCapabilityHolder<LivingEntity, MobTraitCap> HOLDER =
			new GeneralCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "traits"),
					CAPABILITY, MobTraitCap.class, MobTraitCap::new, LivingEntity.class, (e) ->
					e.getType().is(LHTagGen.WHITELIST) ||
							e instanceof Enemy && !e.getType().is(LHTagGen.BLACKLIST));

	public enum Stage {
		PRE_INIT, INIT, POST_INIT
	}

	@SerialClass.SerialField(toClient = true)
	public final LinkedHashMap<MobTrait, Integer> traits = new LinkedHashMap<>();

	@SerialClass.SerialField(toClient = true)
	private Stage stage = Stage.PRE_INIT;

	@SerialClass.SerialField(toClient = true)
	public int lv;

	@SerialClass.SerialField
	private final HashMap<ResourceLocation, CapStorageData> data = new HashMap<>();

	@SerialClass.SerialField(toClient = true)
	public boolean summoned = false, minion = false, noDrop = false, fullDrop = false;

	@SerialClass.SerialField
	public double dropRate = 1;

	@Nullable
	@SerialClass.SerialField
	public BlockPos pos = null;

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public MinionData asMinion = null;

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public MasterData asMaster = null;

	@Nullable
	private TraitSpawnerBlockEntity summoner = null;

	private boolean inherited = false;
	private boolean ticking = false;
	private EntityConfig.Config configCache = null;

	private final ArrayList<Pair<MobTrait, Integer>> pending = new ArrayList<>();

	public MobTraitCap() {
	}

	public void syncToClient(LivingEntity entity) {
		L2Hostility.HANDLER.toTrackingPlayers(new MobCapSyncToClient(entity, this), entity);
	}

	public void syncToPlayer(LivingEntity entity, ServerPlayer player) {
		L2Hostility.HANDLER.toClientPlayer(new MobCapSyncToClient(entity, this), player);
	}

	public static void register() {
	}

	public void deinit() {
		traits.clear();
		lv = 0;
		stage = Stage.PRE_INIT;
	}

	public boolean reinit(LivingEntity mob, int level, boolean max) {
		deinit();
		init(mob.level(), mob, (pos, ins) -> {
			ins.base = level;
			if (max) ins.setFullChance();
		});
		return true;
	}

	@Nullable
	public EntityConfig.Config getConfigCache(LivingEntity le) {
		if (configCache == null) {
			configCache = L2Hostility.ENTITY.getMerged().get(le.getType());
		}
		if (configCache == null && le.level() instanceof ServerLevel sl) {
			configCache = L2Hostility.DIFFICULTY.getMerged().get(sl, le.blockPosition(), le.getType());
		}
		if (configCache == null) {
			configCache = L2Hostility.DIFFICULTY.getMerged().get(le.level().dimension().location(), le.getType());
		}
		return configCache;
	}

	public void setConfigCache(EntityConfig.Config config) {
		configCache = config;
	}

	public void init(Level level, LivingEntity le, RegionalDifficultyModifier difficulty) {
		boolean skip = !LHConfig.COMMON.allowNoAI.get() && le instanceof Mob mob && mob.isNoAi();
		MobDifficultyCollector instance = new MobDifficultyCollector();
		var diff = getConfigCache(le);
		if (diff != null) {
			instance.acceptConfig(diff.difficulty());
		}
		difficulty.modifyInstance(le.blockPosition(), instance);
		Player player = PlayerFinder.getNearestPlayer(level, le);
		if (player != null && PlayerDifficulty.HOLDER.isProper(player)) {
			PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
			playerDiff.apply(instance);
			if (!LHConfig.COMMON.allowPlayerAllies.get() && le.isAlliedTo(player)) {
				skip = true;
			}
		}
		lv = skip ? 0 : TraitManager.fill(this, le, traits, instance);
		fullDrop = instance.isFullDrop();
		stage = Stage.INIT;
		syncToClient(le);
	}

	public void copyFrom(LivingEntity par, LivingEntity child, MobTraitCap parent) {
		InheritContext ctx = new InheritContext(par, parent, child, this, !parent.inherited);
		parent.inherited = true;
		lv = parent.lv;
		summoned = parent.summoned;
		minion = parent.minion;
		noDrop = parent.noDrop;
		dropRate = parent.dropRate * LHConfig.COMMON.splitDropRateFactor.get();
		for (var ent : parent.traits.entrySet()) {
			int rank = ent.getKey().inherited(this, ent.getValue(), ctx);
			if (rank > 0) {
				traits.put(ent.getKey(), rank);
			}
		}
		TraitManager.fill(this, child, traits, MobDifficultyCollector.noTrait(lv));
		stage = Stage.INIT;
	}

	public int getEnchantBonus() {
		return (int) (lv * LHConfig.COMMON.enchantmentFactor.get());
	}

	public int getLevel() {
		return lv;
	}

	public void setLevel(LivingEntity le, int level) {
		lv = clampLevel(le, level);
		TraitManager.scale(le, lv);
	}

	public int clampLevel(LivingEntity le, int lv) {
		int cap = LHConfig.COMMON.maxMobLevel.get();
		var config = getConfigCache(le);
		if (config != null && config.maxLevel > 0) {
			cap = Math.min(config.maxLevel, cap);
		}
		return Math.min(cap, lv);
	}

	public boolean isInitialized() {
		return stage != Stage.PRE_INIT;
	}

	public int getTraitLevel(MobTrait trait) {
		return traits.getOrDefault(trait, 0);
	}

	public boolean hasTrait(MobTrait trait) {
		return getTraitLevel(trait) > 0;
	}

	public void traitEvent(BiConsumer<MobTrait, Integer> cons) {
		traits.forEach(cons);
	}

	public void setTrait(MobTrait trait, int lv) {
		pending.add(Pair.of(trait, lv));
	}

	public void removeTrait(MobTrait trait) {
		if (!traits.containsKey(trait)) return;
		if (ticking) {
			setTrait(trait, 0);
		} else {
			traits.remove(trait);
		}

	}

	private boolean clearPending(LivingEntity mob) {
		if (pending.isEmpty()) return false;
		while (!pending.isEmpty()) {
			var temp = new ArrayList<>(pending);
			for (var pair : pending) {
				traits.put(pair.getFirst(), pair.getSecond());
			}
			pending.clear();
			for (var pair : temp) {
				pair.getFirst().initialize(mob, pair.getSecond());
				pair.getFirst().postInit(mob, pair.getSecond());
			}
			for (var pair : temp) {
				if (pair.getSecond() == 0) {
					traits.remove(pair.getFirst());
				}
			}
		}
		return true;
	}

	public void tick(LivingEntity mob) {
		boolean sync = false;
		ticking = true;
		if (!mob.level().isClientSide()) {
			if (!isInitialized()) {
				var opt = ChunkDifficulty.at(mob.level(), mob.blockPosition());
				opt.ifPresent(chunkDifficulty -> init(mob.level(), mob, chunkDifficulty));
			}
			if (stage == Stage.INIT) {
				stage = Stage.POST_INIT;
				ItemPopulator.postFill(this, mob);
				traits.forEach((k, v) -> k.postInit(mob, v));
				clearPending(mob);
				mob.setHealth(mob.getMaxHealth());
				sync = true;
			}
			if (!traits.isEmpty() &&
					!LHConfig.COMMON.allowTraitOnOwnable.get() &&
					mob instanceof OwnableEntity own &&
					own.getOwner() instanceof Player) {
				traits.clear();
				sync = true;
			}
		}
		if (isInitialized()) {
			if (!traits.isEmpty()) {
				if (mob.tickCount % PerformanceConstants.removeTraitInterval() == 0) {
					sync |= traits.keySet().removeIf(Objects::isNull);
					sync |= traits.keySet().removeIf(MobTrait::isBanned);
				}
				traits.forEach((k, v) -> k.tick(mob, v));
			}
			sync |= clearPending(mob);
		}
		if (!mob.level().isClientSide()) {
			if (pos != null) {
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
		if (asMinion != null) {
			sync |= asMinion.tick(mob);
		}
		if (hasTrait(LHTraits.MASTER.get())) {
			if (!mob.level().isClientSide() && asMaster == null) {
				asMaster = new MasterData();
				sync = true;
			}
			if (mob instanceof Mob master && asMaster != null) {
				sync |= asMaster.tick(this, master);
			}
			if (mob.level().isClientSide()) {
				if (mob instanceof Mob m)
					ClientEvents.MASTERS.add(m);
				mob.addTag("HostilityGlowing");
			}
		}
		if (summoned && mob.level().isClientSide()) {
			mob.addTag("HostilityGlowing");
		}
		if (!mob.level().isClientSide() && sync && !mob.isRemoved()) syncToClient(mob);
		ticking = false;
	}

	public boolean shouldDiscard(LivingEntity mob) {
		var config = getConfigCache(mob);
		if (config == null || config.minSpawnLevel <= 0) return false;
		return lv < config.minSpawnLevel;
	}

	public void onKilled(LivingEntity mob, @Nullable Player player) {
		if (summoner != null && !summoner.isRemoved()) {
			summoner.data.onDeath(mob);
		}
		if (player instanceof ServerPlayer sp) {
			HostilityTriggers.TRAIT_LEVEL.trigger(sp, this);
			HostilityTriggers.TRAIT_COUNT.trigger(sp, this);
			HostilityTriggers.KILL_TRAITS.trigger(sp, this);
			HostilityTriggers.TRAIT_FLAME.trigger(sp, mob, this);
			HostilityTriggers.TRAIT_EFFECT.trigger(sp, mob, this);
		}
	}

	public boolean isSummoned() {
		return summoned || minion;
	}

	public boolean isMasterProtected() {
		if (asMaster != null) {
			for (var e : asMaster.data) {
				if (e.minion != null && MobTraitCap.HOLDER.isProper(e.minion)) {
					var scap = MobTraitCap.HOLDER.get(e.minion);
					if (scap.asMinion != null && scap.asMinion.protectMaster)
						return true;
				}
			}
		}
		return false;
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
			ans.add(LangData.LV.get(lv).withStyle(Style.EMPTY
					.withColor(fullDrop ? LHConfig.CLIENT.overHeadLevelColorAbyss.get() :
							LHConfig.CLIENT.overHeadLevelColor.get())));
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
