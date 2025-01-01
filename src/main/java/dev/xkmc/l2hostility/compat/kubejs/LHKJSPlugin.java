package dev.xkmc.l2hostility.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import dev.xkmc.l2damagetracker.compat.CustomAttackListener;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;

public class LHKJSPlugin extends KubeJSPlugin {

	public static final RegistryInfo<MobTrait> TRAITS = RegistryInfo.of(LHTraits.TRAITS.key(), MobTrait.class);

	@Override
	public void init() {
		TRAITS.addType("basic", BasicTraitBuilder.class, BasicTraitBuilder::new);
		TRAITS.addType("attribute", AttributeTraitBuilder.class, AttributeTraitBuilder::new);
		TRAITS.addType("effect", TargetEffectTraitBuilder.class, TargetEffectTraitBuilder::new);

		RegistryInfo.ITEM.addType("trait", TraitItemBuilder.class, TraitItemBuilder::new);
	}

	@Override
	public void registerBindings(BindingsEvent event) {
		event.add("L2Hostility", L2HHelper.class);
		event.add("DamageModifier", DamageModifier.class);
	}

	@Override
	public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
		super.registerTypeWrappers(type, typeWrappers);
	}

	@Override
	public void clearCaches() {
		AttackEventHandler.getListeners().removeIf(e -> e instanceof CustomAttackListener);
	}

}
