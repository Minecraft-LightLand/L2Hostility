package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.content.item.modifiers.EnchantmentDisabler;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@Inject(at = @At("HEAD"), method = "inventoryTick")
	public void l2hostility_stackTick(Level level, Entity user, int slot, boolean selected, CallbackInfo ci) {
		EnchantmentDisabler.tickStack(level, Wrappers.cast(this));
	}

}
