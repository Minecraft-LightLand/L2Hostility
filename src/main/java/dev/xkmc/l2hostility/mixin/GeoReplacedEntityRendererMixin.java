package dev.xkmc.l2hostility.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

@Mixin(GeoReplacedEntityRenderer.class)
public class GeoReplacedEntityRendererMixin {

	@WrapOperation(remap = false, at = @At(remap = true, value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisibleTo(Lnet/minecraft/world/entity/player/Player;)Z"), method = "actuallyRender")
	public boolean l2hostility$isInvisibleTo$geckoFix(Entity instance, Player player, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 4) LocalFloatRef alpha) {
		boolean ans = original.call(instance, player);
		if (ans && instance.isCurrentlyGlowing()) {
			alpha.set(0);
			return false;
		}
		return ans;
	}

}
