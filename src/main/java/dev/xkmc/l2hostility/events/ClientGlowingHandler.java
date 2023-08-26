package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

public class ClientGlowingHandler {

	public static boolean isGlowing(Entity entity) {
		if (entity instanceof Mob mob) {
			if (MobTraitCap.HOLDER.isProper(mob)) {
				var cap = MobTraitCap.HOLDER.get(mob);
				if (cap.summoned) {
					return true;
				}
			}
		}
		LocalPlayer player = Proxy.getClientPlayer();
		if (player != null && CurioCompat.hasItem(player, LHItems.DETECTOR_GLASSES.get())) {
			boolean glow = entity.isInvisible() || entity.isInvisibleTo(player);
			glow |= player.hasEffect(MobEffects.BLINDNESS);
			glow |= player.hasEffect(MobEffects.DARKNESS);
			int hidden = LHConfig.CLIENT.glowingRangeHidden.get();
			int near = LHConfig.CLIENT.glowingRangeNear.get();
			double distSqr = entity.distanceToSqr(player);
			return distSqr <= near * near || glow && distSqr <= hidden * hidden;
		}
		return false;
	}

	@Nullable
	public static Integer getColor(Entity entity) {
		if (entity instanceof Mob mob) {
			if (MobTraitCap.HOLDER.isProper(mob)) {
				if (MobTraitCap.HOLDER.get(mob).summoned) {
					return 0xff0000;
				}
			}
		}
		return null;
	}

}
