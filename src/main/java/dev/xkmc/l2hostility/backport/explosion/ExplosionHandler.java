package dev.xkmc.l2hostility.backport.explosion;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class ExplosionHandler {

	public static void explode(BaseExplosion exp) {
		if (exp.base.level().isClientSide()) return;
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(exp.base.level(), exp)) return;
		exp.explode();
		Level level = exp.base.level();
		exp.finalizeExplosion(level.isClientSide());
		double x = exp.base.x();
		double y = exp.base.y();
		double z = exp.base.z();
		float r = exp.base.r();
		boolean flag = exp.mc.type() == Explosion.BlockInteraction.NONE;
		if (flag) {
			exp.clearToBlow();
		}
		for (Player player : level.players()) {
			if (player instanceof ServerPlayer serverplayer) {
				if (serverplayer.distanceToSqr(x, y, z) < 4096.0D) {
					serverplayer.connection.send(new ClientboundExplodePacket(x, y, z, r, exp.getToBlow(), exp.getHitPlayers().get(serverplayer)));
				}
			}
		}
	}

}
