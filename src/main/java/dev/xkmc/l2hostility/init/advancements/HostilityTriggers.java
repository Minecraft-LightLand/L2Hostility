package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.resources.ResourceLocation;

public class HostilityTriggers {

	public static final KillTraitsTrigger KILL_TRAITS = new KillTraitsTrigger(new ResourceLocation(L2Hostility.MODID, "kill_trait"));
	public static final KillTraitLevelTrigger TRAIT_LEVEL = new KillTraitLevelTrigger(new ResourceLocation(L2Hostility.MODID, "trait_level"));
	public static final KillTraitCountTrigger TRAIT_COUNT = new KillTraitCountTrigger(new ResourceLocation(L2Hostility.MODID, "trait_count"));
	public static final KillTraitEffectTrigger TRAIT_EFFECT = new KillTraitEffectTrigger(new ResourceLocation(L2Hostility.MODID, "trait_effect"));
	public static final KillTraitFlameTrigger TRAIT_FLAME = new KillTraitFlameTrigger(new ResourceLocation(L2Hostility.MODID, "trait_flame"));

	public static void register() {

	}

}
