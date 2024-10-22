package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record TraitEffectToClient(
		int id, @Nullable MobTrait trait, TraitEffects effect, BlockPos pos
) implements SerialPacketBase<TraitEffectToClient> {

	public static TraitEffectToClient of(LivingEntity e, MobTrait trait, TraitEffects effect) {
		return new TraitEffectToClient(e.getId(), trait, effect, BlockPos.ZERO);
	}

	public static TraitEffectToClient of(BlockPos pos, TraitEffects effect) {
		return new TraitEffectToClient(-1, null, effect, pos);
	}

	@Override
	public void handle(Player player) {
		ClientSyncHandler.handleEffect(this);
	}

}
