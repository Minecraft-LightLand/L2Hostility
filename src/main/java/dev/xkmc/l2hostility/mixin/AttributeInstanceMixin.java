package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.events.DebugUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(AttributeInstance.class)
public class AttributeInstanceMixin {

	@Inject(at=@At("HEAD"),method = "getModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)Ljava/util/Set;")
	public void l2hostility$debug$getModifiers(AttributeModifier.Operation p_22105_, CallbackInfoReturnable<Set<AttributeModifier>> cir){
		DebugUtils.checkThread();
	}

	@Inject(at=@At("HEAD"),method = "addModifier")
	public void l2hostility$debug$addModifier(AttributeModifier p_22134_, CallbackInfo ci){
		DebugUtils.checkThread();
	}

	@Inject(at=@At("HEAD"),method = "removeModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V")
	public void l2hostility$debug$removeModifier(AttributeModifier p_22134_, CallbackInfo ci){
		DebugUtils.checkThread();
	}

	@Inject(at=@At("HEAD"),method = "replaceFrom")
	public void l2hostility$debug$replaceFrom(AttributeInstance p_22103_, CallbackInfo ci){
		DebugUtils.checkThread();
	}


	@Inject(at=@At("HEAD"),method = "load")
	public void l2hostility$debug$load(CompoundTag p_22114_, CallbackInfo ci){
		DebugUtils.checkThread();
	}

}
