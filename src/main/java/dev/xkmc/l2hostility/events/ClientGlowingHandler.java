package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ClientGlowingHandler {

	public static boolean isGlowing(Entity entity) {
		if (!(entity instanceof LivingEntity le)) return false;
		if (le instanceof Mob mob) {
			if (mob.getTags().contains("HostilityGlowing") && MobTraitCap.HOLDER.isProper(mob)) {
				var cap = MobTraitCap.HOLDER.get(mob);
				if (cap.isSummoned() || cap.isMasterProtected()) {
					return true;
				}
			}
		}
		if (le.level().isClientSide()) {
			return isGlowingImpl(le);
		}
		return false;
	}

	private static int cacheTick;
	private static boolean cacheGlass;

	private static boolean playerHasGlass(Player player) {
		if (player.tickCount == cacheTick) return cacheGlass;
		cacheGlass = CurioCompat.hasItemInCurioOrSlot(player, LHItems.DETECTOR_GLASSES.get());
		cacheTick = player.tickCount;
		return cacheGlass;
	}

	@OnlyIn(Dist.CLIENT)
	private static boolean isGlowingImpl(LivingEntity entity) {
		LocalPlayer player = Proxy.getClientPlayer();
		if (player != null && playerHasGlass(player)) {
			if (LHConfig.CLIENT.glassForLevelMobsOnly.get()) {
				if (!MobTraitCap.HOLDER.shouldHaveCap(entity)) {
					return false;
				}
			}
			boolean glow = entity.isInvisible() || entity.isInvisibleTo(player);
			glow |= player.hasEffect(MobEffects.BLINDNESS);
			glow |= player.hasEffect(MobEffects.DARKNESS);
			float hidden = LHConfig.CLIENT.glowingRangeHidden.get() + entity.getBbWidth() * 2;
			float near = LHConfig.CLIENT.glowingRangeNear.get() + entity.getBbWidth() * 2;
			double distSqr = entity.distanceToSqr(player);
			return distSqr <= near * near || glow && distSqr <= hidden * hidden;
		}
		return false;
	}

	@Nullable
	public static Integer getColor(Entity entity) {
		if (entity instanceof Mob mob) {
			if (MobTraitCap.HOLDER.isProper(mob)) {
				var cap = MobTraitCap.HOLDER.get(mob);
				if (cap.isSummoned()) {
					return 0xff0000;
				}
				if (cap.isMasterProtected()) {
					return 16755200;
				}
			}
		}
		return null;
	}

}
