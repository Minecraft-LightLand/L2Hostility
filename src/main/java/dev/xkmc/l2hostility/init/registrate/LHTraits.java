package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.*;
import dev.xkmc.l2hostility.content.traits.goals.EnderTrait;
import dev.xkmc.l2hostility.content.traits.legendary.DementorTrait;
import dev.xkmc.l2hostility.content.traits.legendary.DispellTrait;
import dev.xkmc.l2hostility.content.traits.legendary.RepellingTrait;
import dev.xkmc.l2hostility.content.traits.legendary.UndyingTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
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
	public static final RegistryEntry<UndyingTrait> UNDYING;
	public static final RegistryEntry<RepellingTrait> REPELLING;
	public static final RegistryEntry<EnderTrait> ENDER;

	static {
		// no desc
		{
			TANK = L2Hostility.REGISTRATE.regTrait("tank", () -> new AttributeTrait(
					ChatFormatting.GREEN,
					new AttributeTrait.AttributeEntry("tank_health", () -> Attributes.MAX_HEALTH,
							LHConfig.COMMON.tankHealth::get, AttributeModifier.Operation.MULTIPLY_TOTAL),
					new AttributeTrait.AttributeEntry("tank_armor", () -> Attributes.ARMOR,
							LHConfig.COMMON.tankArmor::get, AttributeModifier.Operation.ADDITION),
					new AttributeTrait.AttributeEntry("tank_tough", () -> Attributes.ARMOR_TOUGHNESS,
							LHConfig.COMMON.tankTough::get, AttributeModifier.Operation.ADDITION)
			), () -> new TraitConfig(10, 1, 5)).lang("Tanky").register();

			SPEEDY = L2Hostility.REGISTRATE.regTrait("speedy", () -> new AttributeTrait(
					ChatFormatting.AQUA,
					new AttributeTrait.AttributeEntry("speedy", () -> Attributes.MOVEMENT_SPEED,
							LHConfig.COMMON.speedy::get, AttributeModifier.Operation.MULTIPLY_TOTAL)
			), () -> new TraitConfig(10, 1, 5)).lang("Speedy").register();

			PROTECTION = L2Hostility.REGISTRATE.regTrait("protection",
					() -> new SelfEffectTrait(() -> MobEffects.DAMAGE_RESISTANCE),
					() -> new TraitConfig(10, 1, 4)).lang("Protected").register();
			INVISIBLE = L2Hostility.REGISTRATE.regTrait("invisible", InvisibleTrait::new,
					() -> new TraitConfig(10, 1, 1)).lang("Invisible").register();

		}

		//common
		{
			FIERY = L2Hostility.REGISTRATE.regTrait("fiery", FieryTrait::new,
							() -> new TraitConfig(10, 1, 1))
					.desc("Ignite attacker and attack target for %s seconds.")
					.lang("Fiery").register();
			REGEN = L2Hostility.REGISTRATE.regTrait("regenerate", () -> new RegenTrait(ChatFormatting.RED),
							() -> new TraitConfig(10, 1, 5))
					.desc("Heals %s%% of full health every second.")
					.lang("Regenerating").register();
			ADAPTIVE = L2Hostility.REGISTRATE.regTrait("adaptive", () -> new AdaptingTrait(ChatFormatting.GOLD),
							() -> new TraitConfig(20, 1, 5))
					.desc("Memorize damage types taken and stack %s%% damage reduction for those damage every time. Memorizes last %s different damage types.")
					.lang("Adaptive").register();
			REFLECT = L2Hostility.REGISTRATE.regTrait("reflect", () -> new ReflectTrait(ChatFormatting.DARK_RED),
							() -> new TraitConfig(20, 1, 5))
					.desc("Reflect direct physical damage as %s%% magical damage")
					.lang("Reflect").register();

		}

		//legendary
		{
			DEMENTOR = L2Hostility.REGISTRATE.regTrait("dementor", () -> new DementorTrait(ChatFormatting.DARK_GRAY),
							() -> new TraitConfig(80, 0.5, 1))
					.desc("Immune to physical damage. Damage bypass armor.")
					.lang("Dementor").register();
			DISPELL = L2Hostility.REGISTRATE.regTrait("dispell", () -> new DispellTrait(ChatFormatting.DARK_PURPLE),
							() -> new TraitConfig(50, 0.5, 3))
					.desc("Immune to magic damage. Damage bypass magical protections. Randomly picks an equipment slot and disable enchantments on them for %s seconds.")
					.lang("Dispell").register();
			UNDYING = L2Hostility.REGISTRATE.regTrait("undying", () -> new UndyingTrait(ChatFormatting.DARK_BLUE),
							() -> new TraitConfig(80, 1, 1))
					.desc("Mob will heal to full health every time it dies.")
					.lang("Undying").register();
			ENDER = L2Hostility.REGISTRATE.regTrait("teleport", () -> new EnderTrait(ChatFormatting.DARK_PURPLE),
							() -> new TraitConfig(80, 1, 1))
					.desc("Mob will attempt to teleport to avoid physical damage and track targets.")
					.lang("Teleport").register();
			REPELLING = L2Hostility.REGISTRATE.regTrait("repelling", () -> new RepellingTrait(ChatFormatting.DARK_GREEN),
							() -> new TraitConfig(30, 1, 1).whitelist(
									EntityType.SKELETON, EntityType.STRAY,
									EntityType.PILLAGER, EntityType.EVOKER, EntityType.WITCH,
									EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN,
									EntityType.WITHER
							))
					.desc("Mob will push away entities hostile to it within %s blocks, and immune to projectiles.")
					.lang("Repelling").register();
		}

		// effects
		{
			WEAKNESS = L2Hostility.REGISTRATE.regTrait("weakness", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.WEAKNESS, LHConfig.COMMON.weakTime.get(), lv - 1)),
					() -> new TraitConfig(10, 1, 5)).lang("Weakener").register();
			SLOWNESS = L2Hostility.REGISTRATE.regTrait("slowness", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LHConfig.COMMON.slowTime.get(), lv)),
					() -> new TraitConfig(10, 1, 5)).lang("Stray").register();
			POISON = L2Hostility.REGISTRATE.regTrait("poison", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.POISON, LHConfig.COMMON.poisonTime.get() * lv)),
					() -> new TraitConfig(10, 1, 3)).lang("Poisonous").register();
			WITHER = L2Hostility.REGISTRATE.regTrait("wither", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.WITHER, LHConfig.COMMON.witherTime.get(), lv - 1)),
					() -> new TraitConfig(10, 0.5, 3)).lang("Withering").register();
			LEVITATION = L2Hostility.REGISTRATE.regTrait("levitation", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.LEVITATION, LHConfig.COMMON.levitationTime.get() * lv)),
					() -> new TraitConfig(10, 0.25, 3)).lang("Levitater").register();
			BLIND = L2Hostility.REGISTRATE.regTrait("blindness", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.BLINDNESS, LHConfig.COMMON.blindTime.get() * lv)),
					() -> new TraitConfig(10, 0.5, 3)).lang("Blinder").register();
			CONFUSION = L2Hostility.REGISTRATE.regTrait("nausea", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.CONFUSION, LHConfig.COMMON.confusionTime.get() * lv)),
					() -> new TraitConfig(10, 0.25, 3)).lang("Distorter").register();
			SOUL_BURNER = L2Hostility.REGISTRATE.regTrait("soul_burner", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(LCEffects.FLAME.get(), LHConfig.COMMON.soulBurnerTime.get(), lv - 1)),
					() -> new TraitConfig(15, 0.25, 3)).lang("Soul Burner").register();
			FREEZING = L2Hostility.REGISTRATE.regTrait("freezing", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(LCEffects.ICE.get(), LHConfig.COMMON.freezingTime.get() * lv)),
					() -> new TraitConfig(15, 0.25, 3)).lang("Freezing").register();
			CURSED = L2Hostility.REGISTRATE.regTrait("cursed", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(LCEffects.CURSE.get(), LHConfig.COMMON.curseTime.get() * lv)),
					() -> new TraitConfig(15, 0.25, 3)).lang("Cursed").register();
		}
	}


	public static void register() {
	}

}
