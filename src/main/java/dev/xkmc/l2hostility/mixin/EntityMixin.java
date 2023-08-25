package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(at = @At("HEAD"), method = "isCurrentlyGlowing", cancellable = true)
	public void l2hostility$isGlowing(CallbackInfoReturnable<Boolean> cir) {
		Entity self = Wrappers.cast(this);
		if (self instanceof Mob mob) {
			if (MobTraitCap.HOLDER.isProper(mob)) {
				if (MobTraitCap.HOLDER.get(mob).summoned) {
					cir.setReturnValue(true);
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "getTeamColor", cancellable = true)
	public void l2hostility$getTeamColor(CallbackInfoReturnable<Integer> cir) {
		Entity self = Wrappers.cast(this);
		if (self instanceof Mob mob) {
			if (MobTraitCap.HOLDER.isProper(mob)) {
				if (MobTraitCap.HOLDER.get(mob).summoned) {
					cir.setReturnValue(0xff0000);
				}
			}
		}
	}

}
