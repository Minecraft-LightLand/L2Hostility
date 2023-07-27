package dev.xkmc.l2complements.content.capability.mob;

import dev.xkmc.l2complements.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2complements.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2complements.content.logic.MobDifficultyCollector;
import dev.xkmc.l2complements.content.logic.ModifierManager;
import dev.xkmc.l2complements.content.modifiers.core.MobModifierInstance;
import dev.xkmc.l2complements.init.L2Hostility;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.ArrayList;

@SerialClass
public class MobModifierCap extends GeneralCapabilityTemplate<LivingEntity, MobModifierCap> {

	public static final Capability<MobModifierCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final GeneralCapabilityHolder<LivingEntity, MobModifierCap> HOLDER =
			new GeneralCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "modifiers"),
					CAPABILITY, MobModifierCap.class, MobModifierCap::new, LivingEntity.class, (e) ->
					e instanceof Enemy && !e.getType().is(TagGen.BLACKLIST));

	@SerialClass.SerialField(toClient = true)
	public final ArrayList<MobModifierInstance> modifiers = new ArrayList<>();

	@SerialClass.SerialField
	public boolean initialized = false;

	@SerialClass.SerialField(toClient = true)
	private int lv;

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
		if (!le.hasCustomName()) {
			le.setCustomName(le.getType().getDescription().copy().append(" Lv. " + lv));
		}
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
		for (var e : modifiers) {
			e.modifier().tick(mob, e.level());
		}
	}

}
