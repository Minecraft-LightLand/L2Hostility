package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficultyCap;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.ICapItem;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {

	@SubscribeEvent
	public static void onAttachItemCap(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		if (!(stack.getItem() instanceof ICapItem<?> item))
			return;
		final LazyOptional<ICurio> stats = LazyOptional.of(() -> item.create(stack));
		event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
			@Nonnull
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
				return CuriosCapability.ITEM.orEmpty(cap, stats);
			}
		});
		Objects.requireNonNull(stats);
		event.addListener(stats::invalidate);
	}

	@SubscribeEvent
	public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
		event.addCapability(new ResourceLocation(L2Hostility.MODID, "difficulty"),
				new ChunkDifficultyCap(event.getObject()));
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof LivingEntity entity && event.getEntity() instanceof ServerPlayer player) {
			if (MobTraitCap.HOLDER.isProper(entity)) {
				MobTraitCap.HOLDER.get(entity).syncToPlayer(entity, player);
			}
		}
	}

	private static void initMob(LivingEntity mob) {
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (!mob.level().isClientSide() && !cap.isInitialized()) {
				var opt = ChunkDifficulty.at(mob.level(), mob.blockPosition());
				opt.ifPresent(chunkDifficulty -> cap.init(mob.level(), mob, chunkDifficulty));
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
		LivingEntity mob = event.getEntity();
		initMob(mob);
	}


	@SubscribeEvent
	public static void livingTickEvent(LivingEvent.LivingTickEvent event) {
		LivingEntity mob = event.getEntity();
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			cap.tick(mob);
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		LivingEntity mob = event.getEntity();
		if (mob.level().isClientSide()) return;
		LivingEntity killer = event.getEntity().getKillCredit();
		Player player = null;
		if (killer instanceof Player pl) {
			player = pl;
		} else if (killer instanceof OwnableEntity own && own.getOwner() instanceof Player pl) {
			player = pl;
		}
		if (MobTraitCap.HOLDER.isProper(mob)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
			if (killer != null) {
				cap.onKilled(mob, player);
			}
			if (player != null) {
				PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
				playerDiff.addKillCredit(cap);
				LevelChunk chunk = mob.level().getChunkAt(mob.blockPosition());
				var opt = chunk.getCapability(ChunkDifficulty.CAPABILITY);
				if (opt.resolve().isPresent()) {
					opt.resolve().get().addKillHistory(player, mob, cap);
				}
			}
		}
	}

}
