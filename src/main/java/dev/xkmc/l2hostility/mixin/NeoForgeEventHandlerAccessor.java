package dev.xkmc.l2hostility.mixin;

import net.neoforged.neoforge.common.NeoForgeEventHandler;
import net.neoforged.neoforge.common.loot.LootModifierManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NeoForgeEventHandler.class)
public interface NeoForgeEventHandlerAccessor {

	@Invoker(value = "getLootModifierManager", remap = false)
	static LootModifierManager callGetLootModifierManager() {
		throw new AssertionError();
	}

}
