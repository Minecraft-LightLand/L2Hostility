package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

public class HostilityTriggers {

	private static final SR<CriterionTrigger<?>> REG = SR.of(L2Hostility.REG, BuiltInRegistries.TRIGGER_TYPES);

	public static final Val<KillTraitsTrigger> KILL_TRAITS = REG.reg("kill_trait", KillTraitsTrigger::new);
	public static final Val<KillTraitLevelTrigger> TRAIT_LEVEL = REG.reg("trait_level", KillTraitLevelTrigger::new);
	public static final Val<KillTraitCountTrigger> TRAIT_COUNT = REG.reg("trait_count", KillTraitCountTrigger::new);
	public static final Val<KillTraitEffectTrigger> TRAIT_EFFECT = REG.reg("trait_effect", KillTraitEffectTrigger::new);
	public static final Val<KillTraitFlameTrigger> TRAIT_FLAME = REG.reg("trait_flame", KillTraitFlameTrigger::new);

	public static void register() {

	}

}
