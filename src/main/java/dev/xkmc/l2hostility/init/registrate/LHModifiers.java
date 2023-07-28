package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.config.ModifierConfig;
import dev.xkmc.l2hostility.content.modifiers.base.AttributeMobModifier;
import dev.xkmc.l2hostility.content.modifiers.base.SelfEffectModifier;
import dev.xkmc.l2hostility.content.modifiers.common.*;
import dev.xkmc.l2hostility.content.modifiers.core.MobModifier;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LHModifiers {

	public static final L2Registrate.RegistryInstance<MobModifier> MODIFIERS = L2Hostility.REGISTRATE.newRegistry("modifier", MobModifier.class);

	public static final RegistryEntry<AttributeMobModifier> TANK, SPEEDY;
	public static final RegistryEntry<SelfEffectModifier> PROTECTION, INVISIBLE;
	public static final RegistryEntry<RegenModifier> REGEN;
	public static final RegistryEntry<DementorModifier> DEMENTOR;
	public static final RegistryEntry<DispellModifier> DISPELL;
	public static final RegistryEntry<AdaptingModifier> ADAPTIVE;
	public static final RegistryEntry<ReflectModifier> REFLECT;

	static {
		TANK = L2Hostility.REGISTRATE.regModifier("tank", () -> new AttributeMobModifier(
				new AttributeMobModifier.AttributeEntry("tank_health", () -> Attributes.MAX_HEALTH,
						LHConfig.COMMON.tankHealth::get, AttributeModifier.Operation.MULTIPLY_TOTAL),
				new AttributeMobModifier.AttributeEntry("tank_armor", () -> Attributes.ARMOR,
						LHConfig.COMMON.tankArmor::get, AttributeModifier.Operation.ADDITION),
				new AttributeMobModifier.AttributeEntry("tank_tough", () -> Attributes.ARMOR,
						LHConfig.COMMON.tankTough::get, AttributeModifier.Operation.ADDITION)
		), new ModifierConfig(10, 1, 5)).lang("Tanky").register();

		SPEEDY = L2Hostility.REGISTRATE.regModifier("speedy", () -> new AttributeMobModifier(
				new AttributeMobModifier.AttributeEntry("speedy", () -> Attributes.MOVEMENT_SPEED,
						LHConfig.COMMON.speedy::get, AttributeModifier.Operation.MULTIPLY_TOTAL)
		), new ModifierConfig(10, 1, 5)).lang("Speedy").register();

		PROTECTION = L2Hostility.REGISTRATE.regModifier("protection",
				() -> new SelfEffectModifier(() -> MobEffects.DAMAGE_RESISTANCE),
				new ModifierConfig(10, 1, 5)).lang("Protected").register();
		INVISIBLE = L2Hostility.REGISTRATE.regModifier("invisible", () -> new SelfEffectModifier(() -> MobEffects.INVISIBILITY),
				new ModifierConfig(10, 1, 1)).lang("Invisible").register();
		REGEN = L2Hostility.REGISTRATE.regModifier("regenerate", RegenModifier::new,
				new ModifierConfig(10, 1, 5)).lang("Regenerating").register();
		DEMENTOR = L2Hostility.REGISTRATE.regModifier("dementor", DementorModifier::new,
				new ModifierConfig(50, 0.5, 1)).lang("Dementor").register();
		DISPELL = L2Hostility.REGISTRATE.regModifier("dispell", DispellModifier::new,
				new ModifierConfig(50, 0.5, 1)).lang("Dispell").register();
		ADAPTIVE = L2Hostility.REGISTRATE.regModifier("adaptive", AdaptingModifier::new,
				new ModifierConfig(20, 1, 1)).lang("Adaptive").register();
		REFLECT = L2Hostility.REGISTRATE.regModifier("reflect", ReflectModifier::new,
				new ModifierConfig(20, 1, 5)).lang("Reflect").register();

	}


	public static void register() {
	}

}
