package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.*;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LHTraits {

	public static final L2Registrate.RegistryInstance<MobTrait> TRAITS = L2Hostility.REGISTRATE.newRegistry("trait", MobTrait.class);

	public static final RegistryEntry<AttributeTrait> TANK, SPEEDY;
	public static final RegistryEntry<SelfEffectTrait> PROTECTION;
	public static final RegistryEntry<TargetEffectTrait>
			WEAKNESS, SLOWNESS, POISON, WITHER, BLIND, CONFUSION, LEVITATION,
			SOUL_BURNER, FREEZING, CURSED;
	public static final RegistryEntry<InvisibleTrait> INVISIBLE;
	public static final RegistryEntry<FieryTrait> FIERY;
	public static final RegistryEntry<RegenTrait> REGEN;
	public static final RegistryEntry<DementorTrait> DEMENTOR;
	public static final RegistryEntry<DispellTrait> DISPELL;
	public static final RegistryEntry<AdaptingTrait> ADAPTIVE;
	public static final RegistryEntry<ReflectTrait> REFLECT;

	static {
		TANK = L2Hostility.REGISTRATE.regTrait("tank", () -> new AttributeTrait(
				ChatFormatting.GREEN,
				new AttributeTrait.AttributeEntry("tank_health", () -> Attributes.MAX_HEALTH,
						LHConfig.COMMON.tankHealth::get, AttributeModifier.Operation.MULTIPLY_TOTAL),
				new AttributeTrait.AttributeEntry("tank_armor", () -> Attributes.ARMOR,
						LHConfig.COMMON.tankArmor::get, AttributeModifier.Operation.ADDITION),
				new AttributeTrait.AttributeEntry("tank_tough", () -> Attributes.ARMOR,
						LHConfig.COMMON.tankTough::get, AttributeModifier.Operation.ADDITION)
		), new TraitConfig(10, 1, 5)).lang("Tanky").register();

		SPEEDY = L2Hostility.REGISTRATE.regTrait("speedy", () -> new AttributeTrait(
				ChatFormatting.AQUA,
				new AttributeTrait.AttributeEntry("speedy", () -> Attributes.MOVEMENT_SPEED,
						LHConfig.COMMON.speedy::get, AttributeModifier.Operation.MULTIPLY_TOTAL)
		), new TraitConfig(10, 1, 5)).lang("Speedy").register();

		PROTECTION = L2Hostility.REGISTRATE.regTrait("protection",
				() -> new SelfEffectTrait(() -> MobEffects.DAMAGE_RESISTANCE),
				new TraitConfig(10, 1, 5)).lang("Protected").register();
		FIERY = L2Hostility.REGISTRATE.regTrait("fiery", FieryTrait::new,
				new TraitConfig(10, 1, 1)).lang("Fiery").register();
		INVISIBLE = L2Hostility.REGISTRATE.regTrait("invisible", InvisibleTrait::new,
				new TraitConfig(10, 1, 1)).lang("Invisible").register();
		REGEN = L2Hostility.REGISTRATE.regTrait("regenerate", () -> new RegenTrait(ChatFormatting.RED),
				new TraitConfig(10, 1, 5)).lang("Regenerating").register();
		DEMENTOR = L2Hostility.REGISTRATE.regTrait("dementor", () -> new DementorTrait(ChatFormatting.DARK_GRAY),
				new TraitConfig(50, 0.5, 1)).lang("Dementor").register();
		DISPELL = L2Hostility.REGISTRATE.regTrait("dispell", () -> new DispellTrait(ChatFormatting.DARK_PURPLE),
				new TraitConfig(50, 0.5, 3)).lang("Dispell").register();
		ADAPTIVE = L2Hostility.REGISTRATE.regTrait("adaptive", () -> new AdaptingTrait(ChatFormatting.GOLD),
				new TraitConfig(20, 1, 5)).lang("Adaptive").register();
		REFLECT = L2Hostility.REGISTRATE.regTrait("reflect", () -> new ReflectTrait(ChatFormatting.DARK_RED),
				new TraitConfig(20, 1, 5)).lang("Reflect").register();

		WEAKNESS = L2Hostility.REGISTRATE.regTrait("weakness", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.WEAKNESS, LHConfig.COMMON.weakTime.get(), lv - 1)),
				new TraitConfig(10, 1, 5)).lang("Weakener").register();
		SLOWNESS = L2Hostility.REGISTRATE.regTrait("slowness", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LHConfig.COMMON.slowTime.get(), lv)),
				new TraitConfig(10, 1, 5)).lang("Stray").register();
		POISON = L2Hostility.REGISTRATE.regTrait("poison", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.POISON, LHConfig.COMMON.poisonTime.get() * lv)),
				new TraitConfig(10, 1, 3)).lang("Poisonous").register();
		WITHER = L2Hostility.REGISTRATE.regTrait("wither", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.WITHER, LHConfig.COMMON.witherTime.get(), lv - 1)),
				new TraitConfig(10, 0.5, 3)).lang("Withering").register();
		LEVITATION = L2Hostility.REGISTRATE.regTrait("levitation", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.LEVITATION, LHConfig.COMMON.levitationTime.get() * lv)),
				new TraitConfig(10, 0.25, 3)).lang("Levitater").register();
		BLIND = L2Hostility.REGISTRATE.regTrait("blindness", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.BLINDNESS, LHConfig.COMMON.blindTime.get() * lv)),
				new TraitConfig(10, 0.5, 3)).lang("Blinder").register();
		CONFUSION = L2Hostility.REGISTRATE.regTrait("nausea", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(MobEffects.CONFUSION, LHConfig.COMMON.confusionTime.get() * lv)),
				new TraitConfig(10, 0.25, 3)).lang("Distorter").register();
		SOUL_BURNER = L2Hostility.REGISTRATE.regTrait("soul_burner", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(LCEffects.FLAME.get(), LHConfig.COMMON.soulBurnerTime.get(), lv - 1)),
				new TraitConfig(15, 0.25, 3)).lang("Soul Burner").register();
		FREEZING = L2Hostility.REGISTRATE.regTrait("freezing", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(LCEffects.ICE.get(), LHConfig.COMMON.freezingTime.get() * lv)),
				new TraitConfig(15, 0.25, 3)).lang("Freezing").register();
		CURSED = L2Hostility.REGISTRATE.regTrait("cursed", () -> new TargetEffectTrait(
						lv -> new MobEffectInstance(LCEffects.CURSE.get(), LHConfig.COMMON.curseTime.get() * lv)),
				new TraitConfig(15, 0.25, 3)).lang("Cursed").register();
	}


	public static void register() {
	}

}
