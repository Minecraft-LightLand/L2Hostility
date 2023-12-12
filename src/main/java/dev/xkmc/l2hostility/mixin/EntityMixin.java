package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.events.ClientGlowingHandler;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(at = @At("HEAD"), method = "isCurrentlyGlowing", cancellable = true)
	public void l2hostility$isGlowing(CallbackInfoReturnable<Boolean> cir) {
		if (ClientGlowingHandler.isGlowing(Wrappers.cast(this))) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "getTeamColor", cancellable = true)
	public void l2hostility$getTeamColor(CallbackInfoReturnable<Integer> cir) {
		Integer col = ClientGlowingHandler.getColor(Wrappers.cast(this));
		if (col != null) {
			cir.setReturnValue(col);
		}
	}

	@Inject(at = @At("HEAD"), method = "isInRain", cancellable = true)
	public void l2hostility$isInRain$ringOfOcean(CallbackInfoReturnable<Boolean> cir) {
		Entity self = Wrappers.cast(this);
		if (self instanceof LivingEntity le) {
			if (CurioCompat.hasItem(le, LHItems.RING_OCEAN.get())) {
				cir.setReturnValue(true);
			}
		}
	}

}
