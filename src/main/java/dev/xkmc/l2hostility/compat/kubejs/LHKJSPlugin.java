package dev.xkmc.l2hostility.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class LHKJSPlugin implements KubeJSPlugin {

	private static final ResourceKey<Registry<MobTrait>> TRAITS = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(L2Hostility.MODID, "trait"));

	@Override
	public void registerBuilderTypes(BuilderTypeRegistry registry) {
		registry.of(TRAITS, reg -> {
			reg.add("basic", BasicTraitBuilder.class, BasicTraitBuilder::new);
			reg.add("legendary", LegendaryTraitBuilder.class, LegendaryTraitBuilder::new);
			reg.add("attribute", AttributeTraitBuilder.class, AttributeTraitBuilder::new);
			reg.add("effect", TargetEffectTraitBuilder.class, TargetEffectTraitBuilder::new);
		});
		registry.of(Registries.ITEM, reg -> {
			reg.add("trait", TraitItemBuilder.class, TraitItemBuilder::new);
		});
	}

	@Override
	public void registerTypeWrappers(TypeWrapperRegistry registry) {
		registry.register(MobTrait.class, LHKJSPlugin::parseMobTrait);
	}

	@Override
	public void registerBindings(BindingRegistry event) {
		event.add("L2Hostility", L2HHelper.class);
		event.add("DamageModifier", DamageModifier.class);
	}

	private static MobTrait parseMobTrait(RegistryAccessContainer ctx, Object o) {
		ResourceLocation id = null;
		if (o instanceof String str) {
			id = ResourceLocation.parse(str);
		}
		if (o instanceof ResourceLocation rl) {
			id = rl;
		}
		if (id == null) throw new IllegalStateException("Cannot parse " + o + " into MobTrait");
		return LHTraits.TRAITS.reg().get(id);
	}


}
