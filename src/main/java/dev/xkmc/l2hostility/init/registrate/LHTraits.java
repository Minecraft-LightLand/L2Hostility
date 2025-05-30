package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.config.TraitExclusion;
import dev.xkmc.l2hostility.content.entity.BulletType;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.*;
import dev.xkmc.l2hostility.content.traits.goals.CounterStrikeTrait;
import dev.xkmc.l2hostility.content.traits.goals.EnderTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.*;
import dev.xkmc.l2hostility.content.traits.legendary.*;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.entries.TraitEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LHTraits {

	public static final L2Registrate.RegistryInstance<MobTrait> TRAITS =
			L2Hostility.REGISTRATE.newRegistry("trait", MobTrait.class, e -> e.sync(true));

	public static final DataMapReg<MobTrait, TraitConfig> DATA =
			L2Hostility.REG.dataMap("trait_data", LHTraits.TRAITS.key(), TraitConfig.class);

	public static final DataMapReg<MobTrait, TraitExclusion> EXCLUSION =
			L2Hostility.REG.dataMap("trait_exclusion", LHTraits.TRAITS.key(), TraitExclusion.class);

	public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<MobTrait>> TRAIT_TAGS =
			L2TagGen.getProvider(TRAITS.key(), TRAITS.reg());

	public static final TagKey<MobTrait> POTION = LHTagGen.createTraitTag("potion_trait");

	public static final TraitEntry<AttributeTrait> TANK, SPEEDY;
	public static final TraitEntry<SelfEffectTrait> PROTECTION;
	public static final TraitEntry<TargetEffectTrait>
			WEAKNESS, SLOWNESS, POISON, WITHER, BLIND, CONFUSION, LEVITATION,
			SOUL_BURNER, FREEZING, CURSED;
	public static final TraitEntry<InvisibleTrait> INVISIBLE;
	public static final TraitEntry<ShulkerTrait> SHULKER, GRENADE;
	public static final TraitEntry<FieryTrait> FIERY;
	public static final TraitEntry<RegenTrait> REGEN;
	public static final TraitEntry<DementorTrait> DEMENTOR;
	public static final TraitEntry<DispellTrait> DISPELL;
	public static final TraitEntry<AdaptingTrait> ADAPTIVE;
	public static final TraitEntry<ReflectTrait> REFLECT;
	public static final TraitEntry<UndyingTrait> UNDYING;
	public static final TraitEntry<RepellingTrait> REPELLING;
	public static final TraitEntry<PullingTrait> PULLING;
	public static final TraitEntry<EnderTrait> ENDER;
	public static final TraitEntry<CorrosionTrait> CORROSION;
	public static final TraitEntry<ErosionTrait> EROSION;
	public static final TraitEntry<KillerAuraTrait> KILLER_AURA;
	public static final TraitEntry<RagnarokTrait> RAGNAROK;
	public static final TraitEntry<GrowthTrait> GROWTH;
	public static final TraitEntry<SplitTrait> SPLIT;
	public static final TraitEntry<DrainTrait> DRAIN;
	public static final TraitEntry<ReprintTrait> REPRINT;
	public static final TraitEntry<CounterStrikeTrait> STRIKE;
	public static final TraitEntry<GravityTrait> GRAVITY;
	public static final TraitEntry<AuraEffectTrait> MOONWALK;
	public static final TraitEntry<ArenaTrait> ARENA;
	public static final TraitEntry<MasterTrait> MASTER;

	static {
		// no desc
		{
			TANK = L2Hostility.REGISTRATE.regTrait("tank", () -> new AttributeTrait(
					ChatFormatting.GREEN,
					new AttributeTrait.AttributeEntry("tank_health", Attributes.MAX_HEALTH,
							LHConfig.SERVER.tankHealth::get, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
					new AttributeTrait.AttributeEntry("tank_armor", Attributes.ARMOR,
							LHConfig.SERVER.tankArmor::get, AttributeModifier.Operation.ADD_VALUE),
					new AttributeTrait.AttributeEntry("tank_tough", Attributes.ARMOR_TOUGHNESS,
							LHConfig.SERVER.tankTough::get, AttributeModifier.Operation.ADD_VALUE)
			), new TraitConfig(20, 100, 5, 20)).lang("Tanky").register();

			SPEEDY = L2Hostility.REGISTRATE.regTrait("speedy", () -> new AttributeTrait(
					ChatFormatting.AQUA,
					new AttributeTrait.AttributeEntry("speedy", Attributes.MOVEMENT_SPEED,
							LHConfig.SERVER.speedy::get, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			), new TraitConfig(20, 100, 5, 50)).lang("Speedy").register();

			PROTECTION = L2Hostility.REGISTRATE.regTrait("protection",
					() -> new SelfEffectTrait(MobEffects.DAMAGE_RESISTANCE),
							new TraitConfig(40, 100, 4, 50))
					.addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))
					.lang("Protected").register();

			INVISIBLE = L2Hostility.REGISTRATE.regTrait("invisible", InvisibleTrait::new,
							new TraitConfig(30, 100, 1, 50))
					.addWhitelist(e -> e.add(
							EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CAVE_SPIDER,
							EntityType.ZOMBIE, EntityType.HUSK, EntityType.DROWNED,
							EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED, EntityType.WITHER))
					.lang("Invisible").register();

		}

		//common
		{
			FIERY = L2Hostility.REGISTRATE.regTrait("fiery", FieryTrait::new,
							new TraitConfig(20, 100, 1, 20))
					.desc("Ignite attacker and attack target for %s seconds. Makes mob immune to fire.")
					.lang("Fiery").register();
			REGEN = L2Hostility.REGISTRATE.regTrait("regenerate", () -> new RegenTrait(ChatFormatting.RED),
							new TraitConfig(30, 100, 5, 50))
					.desc("Heals %s%% of full health every second.")
					.lang("Regenerating").register();
			ADAPTIVE = L2Hostility.REGISTRATE.regTrait("adaptive", () -> new AdaptingTrait(ChatFormatting.GOLD),
							new TraitConfig(80, 50, 5, 100))
					.desc("Memorize damage types taken and stack %s%% damage reduction for those damage every time. Memorizes last %s different damage types.")
					.lang("Adaptive").register();
			REFLECT = L2Hostility.REGISTRATE.regTrait("reflect", () -> new ReflectTrait(ChatFormatting.DARK_RED),
							new TraitConfig(80, 50, 5, 100))
					.desc("Reflect direct physical damage as %s%% magical damage")
					.lang("Reflect").register();

			SHULKER = L2Hostility.REGISTRATE.regTrait("shulker", () -> new ShulkerTrait(ChatFormatting.LIGHT_PURPLE,
									LHConfig.SERVER.shulkerInterval::get, BulletType.PLAIN, 0),
							new TraitConfig(50, 100, 1, 70))
					.addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))
					.desc("Shoot bullets every %s seconds after the previous bullet disappears.")
					.lang("Shulker").register();

			GRENADE = L2Hostility.REGISTRATE.regTrait("grenade", () -> new ShulkerTrait(ChatFormatting.RED,
									LHConfig.SERVER.grenadeInterval::get, BulletType.EXPLODE, 15),
							new TraitConfig(100, 100, 5, 100))
					.addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))
					.dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(SHULKER, 1).build())
					.desc("Shoot explosive bullets every %s seconds after the previous bullet disappears.")
					.lang("Grenade").register();

			CORROSION = L2Hostility.REGISTRATE.regTrait("corrosion", () -> new CorrosionTrait(ChatFormatting.DARK_RED),
							new TraitConfig(120, 50, 3, 200))
					.desc("When hit target, randomly picks %s equipments and increase their durability loss by %s. When there aren't enough equipments, increase damage by %s per piece")
					.lang("Corrosion").register();

			EROSION = L2Hostility.REGISTRATE.regTrait("erosion", () -> new ErosionTrait(ChatFormatting.DARK_BLUE),
							new TraitConfig(120, 50, 3, 200))
					.dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(CORROSION, 1).build())
					.desc("When hit target, randomly picks %s equipments and reduce their durability by %s. When there aren't enough equipments, increase damage by %s per piece")
					.lang("Erosion").register();

			GROWTH = L2Hostility.REGISTRATE.regTrait("growth", () -> new GrowthTrait(ChatFormatting.DARK_GREEN),
							new TraitConfig(60, 300, 3, 100))
					.desc("Slime will grow larger when at full health. Automatically gain Regenerate trait.")
					.lang("Growth").register();

			SPLIT = L2Hostility.REGISTRATE.regTrait("split", () -> new SplitTrait(ChatFormatting.GREEN),
							new TraitConfig(70, 100, 3, 120))
					.addWhitelist(e -> e.add(
							EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER,
							EntityType.ZOMBIFIED_PIGLIN, EntityType.DROWNED, EntityType.HUSK,
							EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.STRAY,
							EntityType.SPIDER, EntityType.CAVE_SPIDER,
							EntityType.CREEPER, EntityType.VEX,
							EntityType.SILVERFISH, EntityType.ENDERMITE
					))
					.desc("When mob dies, it will split into 2 of itself with half levels but same trait. This trait reduce by 1 when split.")
					.lang("Split").register();

			DRAIN = L2Hostility.REGISTRATE.regTrait("drain", () -> new DrainTrait(ChatFormatting.LIGHT_PURPLE),
							new TraitConfig(80, 100, 3, 100))
					.desc("Grants a random potion trait with same level. When hit target, remove %s beneficial effects, deal %s more damage for every harmful effects, and increase their duration by %s. At most increase to %ss.")
					.lang("Drain").register();

			STRIKE = L2Hostility.REGISTRATE.regTrait("counter_strike", () -> new CounterStrikeTrait(ChatFormatting.WHITE),
							new TraitConfig(50, 100, 1, 60))
					.addWhitelist(e -> e.addTag(LHTagGen.MELEE_WEAPON_TARGET)
							.add(EntityType.WARDEN))
					.desc("After attacked, it will attempt to perform a counter strike.")
					.lang("Counter Strike").register();

			GRAVITY = L2Hostility.REGISTRATE.regTrait("gravity", () -> new GravityTrait(LHEffects.GRAVITY),
							new TraitConfig(50, 25, 3, 80))
					.desc("Increase gravity for mobs around it. Knock attackers downward when damaged.").lang("Gravity").register();

			MOONWALK = L2Hostility.REGISTRATE.regTrait("moonwalk", () -> new AuraEffectTrait(LHEffects.MOONWALK),
							new TraitConfig(50, 25, 3, 80))
					.dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(GRAVITY, 1).build())
					.desc("Decrease gravity for mobs around it").lang("Moonwalk").register();

			ARENA = L2Hostility.REGISTRATE.regTrait("arena", ArenaTrait::new,
							new TraitConfig(1000, 1, 1, 50))
					.addWhitelist(pvd -> pvd.addTag(LHTagGen.SEMIBOSS))
					.desc("Players around it cannot place or break blocks. Immune damage from entities not affected by this.")
					.lang("Arena").register();

		}

		//legendary
		{
			DEMENTOR = L2Hostility.REGISTRATE.regTrait("dementor", () -> new DementorTrait(ChatFormatting.DARK_GRAY),
							new TraitConfig(120, 50, 1, 150))
					.dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(ADAPTIVE, 0.5).build())
					.desc("Immune to physical damage. Damage bypass armor.")
					.lang("Dementor").register();

			DISPELL = L2Hostility.REGISTRATE.regTrait("dispell", () -> new DispellTrait(ChatFormatting.DARK_PURPLE),
							new TraitConfig(100, 50, 3, 150))
					.dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(DEMENTOR, 0.75).of(ADAPTIVE, 0.5).build())
					.desc("Immune to magic damage. Damage bypass magical protections. Randomly picks %s enchanted equipment and disable enchantments on them for %s seconds.")
					.lang("Dispell").register();
			UNDYING = L2Hostility.REGISTRATE.regTrait("undying", () -> new UndyingTrait(ChatFormatting.DARK_BLUE),
							new TraitConfig(150, 100, 1, 150))
					.addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))
					.desc("Mob will heal to full health every time it dies.")
					.lang("Undying").register();
			ENDER = L2Hostility.REGISTRATE.regTrait("teleport", () -> new EnderTrait(ChatFormatting.DARK_PURPLE),
							new TraitConfig(120, 100, 1, 150))
					.addBlacklist(pvd -> pvd.addTag(LHTagGen.SEMIBOSS))
					.desc("Mob will attempt to teleport to avoid physical damage and track targets.")
					.lang("Teleport").register();
			REPELLING = L2Hostility.REGISTRATE.regTrait("repelling", () -> new RepellingTrait(ChatFormatting.DARK_GREEN),
							new TraitConfig(80, 50, 1, 100))
					.addWhitelist(e -> e.add(
							EntityType.SKELETON, EntityType.STRAY,
							EntityType.PILLAGER, EntityType.EVOKER, EntityType.WITCH,
							EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN,
							EntityType.WITHER))
					.desc("Mob will push away entities hostile to it within %s blocks, and immune to projectiles.")
					.lang("Repelling").register();

			PULLING = L2Hostility.REGISTRATE.regTrait("pulling", () -> new PullingTrait(ChatFormatting.DARK_BLUE),
							new TraitConfig(80, 50, 1, 100))
					.addWhitelist(e -> e.addTag(LHTagGen.MELEE_WEAPON_TARGET))
					.dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(REPELLING, 1).build())
					.desc("Mob will pull entities hostile to it within %s blocks.")
					.lang("Pulling").register();

			REPRINT = L2Hostility.REGISTRATE.regTrait("reprint", () -> new ReprintTrait(ChatFormatting.LIGHT_PURPLE),
							new TraitConfig(100, 100, 1, 100))
					.desc("Mob will copy target enchantments, and deal %s more damage per enchantment point")
					.lang("Reprint").register();

			KILLER_AURA = L2Hostility.REGISTRATE.regTrait("killer_aura", () -> new KillerAuraTrait(ChatFormatting.DARK_RED),
							new TraitConfig(100, 50, 3, 300))
					.desc("Deal %s magic damage to players and entities targeting it within %s blocks and apply trait effects for every %ss")
					.lang("Killer Aura").register();

			RAGNAROK = L2Hostility.REGISTRATE.regTrait("ragnarok", () -> new RagnarokTrait(ChatFormatting.DARK_BLUE),
							new TraitConfig(300, 100, 3, 600))
					.desc("When hit target, randomly picks %s equipments and seal them, which takes %ss to unseal.")
					.lang("Ragnarok").register();

			MASTER = L2Hostility.REGISTRATE.regTrait("master", () -> new MasterTrait(ChatFormatting.GOLD),
							new TraitConfig(200, 50, 1, 200))
					.desc("Summons minions around the mob. Some minions will protect master.")
					.lang("Master").register();

		}

		// effects
		{
			WEAKNESS = L2Hostility.REGISTRATE.regTrait("weakness", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.WEAKNESS, LHConfig.SERVER.weakTime.get(), lv - 1)),
					new TraitConfig(30, 50, 5, 40)
			).tag(TRAIT_TAGS, POTION).lang("Weakener").register();
			SLOWNESS = L2Hostility.REGISTRATE.regTrait("slowness", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LHConfig.SERVER.slowTime.get(), lv)),
					new TraitConfig(20, 50, 5, 20)
			).tag(TRAIT_TAGS, POTION).lang("Stray").register();
			POISON = L2Hostility.REGISTRATE.regTrait("poison", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.POISON, LHConfig.SERVER.poisonTime.get() * lv)),
					new TraitConfig(20, 75, 3, 20)
			).tag(TRAIT_TAGS, POTION).lang("Poisonous").register();
			WITHER = L2Hostility.REGISTRATE.regTrait("wither", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.WITHER, LHConfig.SERVER.witherTime.get(), lv - 1)),
					new TraitConfig(20, 50, 3, 20)
					).dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(POISON, 0.5).build())
					.tag(TRAIT_TAGS, POTION).lang("Withering").register();
			LEVITATION = L2Hostility.REGISTRATE.regTrait("levitation", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.LEVITATION, LHConfig.SERVER.levitationTime.get() * lv)),
					new TraitConfig(50, 50, 3, 50)
					).dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(SLOWNESS, 0.5).build())
					.tag(TRAIT_TAGS, POTION).lang("Levitater").register();
			BLIND = L2Hostility.REGISTRATE.regTrait("blindness", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.BLINDNESS, LHConfig.SERVER.blindTime.get() * lv)),
					new TraitConfig(30, 25, 3, 40)
			).tag(TRAIT_TAGS, POTION).lang("Blinder").register();
			CONFUSION = L2Hostility.REGISTRATE.regTrait("nausea", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(MobEffects.CONFUSION, LHConfig.SERVER.confusionTime.get() * lv)),
					new TraitConfig(30, 25, 3, 40)
					).dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(BLIND, 0.5).build())
					.tag(TRAIT_TAGS, POTION).lang("Distorter").register();
			SOUL_BURNER = L2Hostility.REGISTRATE.regTrait("soul_burner", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(LCEffects.FLAME, LHConfig.SERVER.soulBurnerTime.get(), lv - 1)),
					new TraitConfig(50, 50, 3, 70)
					).dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(POISON, 0.5).of(WITHER, 0.5).of(FIERY, 1).build())
					.tag(TRAIT_TAGS, POTION).lang("Soul Burner").register();
			FREEZING = L2Hostility.REGISTRATE.regTrait("freezing", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(LCEffects.ICE, LHConfig.SERVER.freezingTime.get() * lv)),
					new TraitConfig(30, 50, 3, 50)
					).dataMap(EXCLUSION.reg(), TraitExclusion.builder().of(SLOWNESS, 0.5).of(LEVITATION, 0.5).of(BLIND, 0.5).of(CONFUSION, 0.5).build())
					.tag(TRAIT_TAGS, POTION).lang("Freezing").register();
			CURSED = L2Hostility.REGISTRATE.regTrait("cursed", () -> new TargetEffectTrait(
							lv -> new MobEffectInstance(LCEffects.CURSE, LHConfig.SERVER.curseTime.get() * lv)),
					new TraitConfig(20, 100, 3, 20)
			).tag(TRAIT_TAGS, POTION).lang("Cursed").register();
		}
	}


	public static void register() {
	}

}
