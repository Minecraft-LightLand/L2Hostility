package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.ModifierManager;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.TagGen;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Supplier;

@SerialClass
public class MobModifierCap extends GeneralCapabilityTemplate<LivingEntity, MobModifierCap> {

	public static final Capability<MobModifierCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final GeneralCapabilityHolder<LivingEntity, MobModifierCap> HOLDER =
			new GeneralCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "modifiers"),
					CAPABILITY, MobModifierCap.class, MobModifierCap::new, LivingEntity.class, (e) ->
					e instanceof Enemy && !e.getType().is(TagGen.BLACKLIST));

	@SerialClass.SerialField(toClient = true)
	public final LinkedHashMap<MobModifier, Integer> modifiers = new LinkedHashMap<>();

	@SerialClass.SerialField
	public boolean initialized = false;

	@SerialClass.SerialField(toClient = true)
	private int lv;

	private final HashMap<ResourceLocation, CapStorageData> data = new HashMap<>();

	public MobModifierCap() {
	}

	public void syncToClient(LivingEntity entity) {
		L2Hostility.HANDLER.toTrackingPlayers(new CapSyncPacket(entity, this), entity);
	}

	public void syncToPlayer(LivingEntity entity, ServerPlayer player) {
		L2Hostility.HANDLER.toClientPlayer(new CapSyncPacket(entity, this), player);
	}

	public static void register() {
	}

	public void init(Level level, LivingEntity le, ChunkDifficulty difficulty) {
		MobDifficultyCollector instance = new MobDifficultyCollector();
		var diff = L2Hostility.DIFFICULTY.getMerged().entityMap.get(le.getType());
		if (diff != null) {
			instance.acceptConfig(diff);
		}
		difficulty.modifyInstance(le.blockPosition(), instance);
		Player player = level.getNearestPlayer(le, 128);
		if (player != null && PlayerDifficulty.HOLDER.isProper(player)) {
			PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
			playerDiff.apply(instance);
		}
		lv = instance.getDifficulty(le.getRandom());
		ModifierManager.fill(le, lv, modifiers, instance.getMaxModifierLevel());
		initialized = true;
		syncToClient(le);
	}

	public int getLevel() {
		return lv;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void tick(LivingEntity mob) {
		modifiers.forEach((k, v) -> k.tick(mob, v));
	}

	public <T extends CapStorageData> T getData(ResourceLocation id) {
		return Wrappers.cast(data.get(id));
	}

	public <T extends CapStorageData> T getOrCreateData(ResourceLocation id, Supplier<T> sup) {
		return Wrappers.cast(data.computeIfAbsent(id, e -> sup.get()));
	}

	public Component getTitle() {
		return Component.literal("Lv. " + lv);
	}
}
