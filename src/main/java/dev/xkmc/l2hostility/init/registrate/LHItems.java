package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2hostility.content.entity.ChargeType;
import dev.xkmc.l2hostility.content.item.consumable.*;
import dev.xkmc.l2hostility.content.item.curio.curse.*;
import dev.xkmc.l2hostility.content.item.curio.misc.*;
import dev.xkmc.l2hostility.content.item.curio.ring.*;
import dev.xkmc.l2hostility.content.item.tool.DetectorGlasses;
import dev.xkmc.l2hostility.content.item.tool.WitchWand;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.content.item.wand.AiConfigWand;
import dev.xkmc.l2hostility.content.item.wand.EquipmentWand;
import dev.xkmc.l2hostility.content.item.wand.TargetSelectWand;
import dev.xkmc.l2hostility.content.item.wand.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraftforge.client.model.generators.ModelFile;

@SuppressWarnings({"unsafe"})
@MethodsReturnNonnullByDefault
public class LHItems {

	public static final ItemEntry<HostilityOrb> HOSTILITY_ORB;
	public static final ItemEntry<BottleOfCurse> BOTTLE_CURSE;
	public static final ItemEntry<BottleOfSanity> BOTTLE_SANITY;
	public static final ItemEntry<Item> WITCH_DROPLET;
	public static final ItemEntry<EffectBoosterBottle> BOOSTER_POTION;
	public static final ItemEntry<HostilityChargeItem> WITCH_CHARGE, ETERNAL_WITCH_CHARGE;

	public static final ItemEntry<Item> DETECTOR;
	public static final ItemEntry<DetectorGlasses> DETECTOR_GLASSES;
	public static final ItemEntry<WitchWand> WITCH_WAND;
	public static final ItemEntry<BookCopy> BOOK_COPY;
	public static final ItemEntry<BookEverything> BOOK_OMNISCIENCE;
	public static final ItemEntry<Item> CHAOS_INGOT;
	public static final ItemEntry<SimpleFoiledItem> HOSTILITY_ESSENCE, MIRACLE_POWDER, MIRACLE_INGOT;
	public static final ItemEntry<CurseOfEnvy> CURSE_ENVY;
	public static final ItemEntry<CurseOfGluttony> CURSE_GLUTTONY;
	public static final ItemEntry<CurseOfGreed> CURSE_GREED;
	public static final ItemEntry<CurseOfLust> CURSE_LUST;
	public static final ItemEntry<CurseOfPride> CURSE_PRIDE;
	public static final ItemEntry<CurseOfSloth> CURSE_SLOTH;
	public static final ItemEntry<CurseOfWrath> CURSE_WRATH;
	public static final ItemEntry<RingOfOcean> RING_OCEAN;
	public static final ItemEntry<RingOfLife> RING_LIFE;
	public static final ItemEntry<RingOfDivinity> RING_DIVINITY;
	public static final ItemEntry<RingOfReflection> RING_REFLECTION;
	public static final ItemEntry<RingOfIncarceration> RING_INCARCERATION;
	public static final ItemEntry<RingOfCorrosion> RING_CORROSION;
	public static final ItemEntry<RingOfHealing> RING_HEALING;
	public static final ItemEntry<FlamingThorn> FLAMING_THORN;
	public static final ItemEntry<ImagineBreaker> IMAGINE_BREAKER;
	public static final ItemEntry<PlatinumStar> PLATINUM_STAR;
	public static final ItemEntry<InfinityGlove> INFINITY_GLOVE;
	public static final ItemEntry<OddeyesGlasses> ODDEYES_GLASSES;
	public static final ItemEntry<TripleStripCape> TRIPLE_STRIP_CAPE;
	public static final ItemEntry<Abrahadabra> ABRAHADABRA;
	public static final ItemEntry<GreedOfNidhoggur> NIDHOGGUR;
	public static final ItemEntry<PocketOfRestoration> RESTORATION;

	public static final ItemEntry<TraitAdderWand> ADDER;
	public static final ItemEntry<TargetSelectWand> TARGET;
	public static final ItemEntry<AiConfigWand> AI;
	public static final ItemEntry<EquipmentWand> EQUIPMENT;

	public static final ItemEntry<SealedItem> SEAL;


	static {

		// consumables
		{
			HOSTILITY_ORB = L2Hostility.REGISTRATE.item(
					"hostility_orb", p -> new HostilityOrb(p.stacksTo(16).rarity(Rarity.EPIC))).register();
			BOTTLE_CURSE = L2Hostility.REGISTRATE.item(
					"bottle_of_curse", p -> new BottleOfCurse(p.stacksTo(16).rarity(Rarity.RARE)
							.craftRemainder(Items.GLASS_BOTTLE))).register();
			BOTTLE_SANITY = L2Hostility.REGISTRATE.item(
					"bottle_of_sanity", p -> new BottleOfSanity(p.stacksTo(16).rarity(Rarity.RARE)
							.craftRemainder(Items.GLASS_BOTTLE))).register();

			WITCH_DROPLET = L2Hostility.REGISTRATE.item("witch_droplet", Item::new).register();

			BOOSTER_POTION = L2Hostility.REGISTRATE.item("booster_potion",
					p -> new EffectBoosterBottle(p.stacksTo(16).rarity(Rarity.RARE)
							.craftRemainder(Items.GLASS_BOTTLE))).register();

			WITCH_CHARGE = L2Hostility.REGISTRATE.item("witch_charge",
					p -> new HostilityChargeItem(p, ChargeType.BOOST, () ->
							LangData.TOOLTIP_WITCH_CHARGE.get(
									LHConfig.COMMON.witchChargeMinDuration.get() / 20,
									Math.round(100 * LHConfig.COMMON.drainDuration.get()),
									LHConfig.COMMON.drainDurationMax.get() / 20
							).withStyle(ChatFormatting.GRAY))
			).register();

			ETERNAL_WITCH_CHARGE = L2Hostility.REGISTRATE.item("eternal_witch_charge",
					p -> new HostilityChargeItem(p, ChargeType.ETERNAL, () ->
							LangData.TOOLTIP_WITCH_ETERNAL.get(
									LHConfig.COMMON.witchChargeMinDuration.get() / 20
							).withStyle(ChatFormatting.GRAY))
			).register();

			BOOK_COPY = L2Hostility.REGISTRATE.item("book_of_reprint",
					BookCopy::new).register();

			BOOK_OMNISCIENCE = L2Hostility.REGISTRATE.item("book_of_omniscience",
					BookEverything::new).register();
		}

		// equipments
		{

			TagKey<Item> head = ItemTags.create(new ResourceLocation("curios", "head"));

			DETECTOR = L2Hostility.REGISTRATE.item(
					"hostility_detector", p -> new Item(p.stacksTo(1))).register();
			DETECTOR_GLASSES = L2Hostility.REGISTRATE.item(
							"detector_glasses", p -> new DetectorGlasses(p.stacksTo(1)))
					.tag(head).register();

			TagKey<Item> chaos = LHTagGen.CHAOS_CURIO;

			WITCH_WAND = L2Hostility.REGISTRATE.item("witch_wand", p -> new WitchWand(p
							.durability(300).rarity(Rarity.EPIC).fireResistant()))
					.model((ctx, pvd) -> pvd.handheld(ctx)).tag(chaos).register();


			CHAOS_INGOT = L2Hostility.REGISTRATE.item("chaos_ingot", p -> new Item(p.rarity(Rarity.EPIC).fireResistant())).register();
			HOSTILITY_ESSENCE = L2Hostility.REGISTRATE.item("hostility_essence", p -> new SimpleFoiledItem(p.rarity(Rarity.EPIC).fireResistant())).register();
			MIRACLE_POWDER = L2Hostility.REGISTRATE.item("miracle_powder", p -> new SimpleFoiledItem(p.rarity(Rarity.EPIC).fireResistant())).register();
			MIRACLE_INGOT = L2Hostility.REGISTRATE.item("miracle_ingot", p -> new SimpleFoiledItem(p.rarity(Rarity.EPIC).fireResistant())).register();

			TagKey<Item> charm = ItemTags.create(new ResourceLocation("curios", "charm"));
			TagKey<Item> curse = LHTagGen.CURSE_SLOT;
			CURSE_ENVY = curio("curse_of_envy", CurseOfEnvy::new).tag(chaos, charm, curse, LHTagGen.NO_SEAL).register();
			CURSE_GLUTTONY = curio("curse_of_gluttony", CurseOfGluttony::new).tag(chaos, charm, curse, LHTagGen.NO_SEAL).register();
			CURSE_GREED = curio("curse_of_greed", CurseOfGreed::new).tag(chaos, charm, curse, LHTagGen.NO_SEAL).register();
			CURSE_LUST = curio("curse_of_lust", CurseOfLust::new).tag(chaos, charm, curse, LHTagGen.NO_SEAL).register();
			CURSE_PRIDE = curio("curse_of_pride", CurseOfPride::new).tag(chaos, charm, curse).register();
			CURSE_SLOTH = curio("curse_of_sloth", CurseOfSloth::new).tag(chaos, charm, curse, LHTagGen.NO_SEAL).register();
			CURSE_WRATH = curio("curse_of_wrath", CurseOfWrath::new).tag(chaos, charm, curse).register();

			TagKey<Item> ring = ItemTags.create(new ResourceLocation("curios", "ring"));

			RING_OCEAN = curio("ring_of_ocean", RingOfOcean::new).tag(chaos, ring).register();
			RING_LIFE = curio("ring_of_life", RingOfLife::new).tag(chaos, ring).register();
			RING_DIVINITY = curio("ring_of_divinity", RingOfDivinity::new).tag(chaos, ring).register();
			RING_REFLECTION = curio("ring_of_reflection", RingOfReflection::new).tag(chaos, ring).register();
			RING_INCARCERATION = curio("ring_of_incarceration", RingOfIncarceration::new).tag(chaos, ring).register();
			RING_CORROSION = curio("ring_of_corrosion", RingOfCorrosion::new).tag(chaos, ring).register();
			RING_HEALING = curio("ring_of_healing", RingOfHealing::new).tag(chaos, ring).register();

			TagKey<Item> hand = ItemTags.create(new ResourceLocation("curios", "hands"));

			FLAMING_THORN = curio("flaming_thorn", FlamingThorn::new).tag(chaos, hand).register();
			IMAGINE_BREAKER = curio("imagine_breaker", ImagineBreaker::new).tag(chaos, hand, LHTagGen.NO_SEAL).register();
			PLATINUM_STAR = curio("platinum_star", PlatinumStar::new).tag(chaos, hand, charm).register();
			INFINITY_GLOVE = curio("infinity_glove", InfinityGlove::new).tag(chaos, hand).register();

			TagKey<Item> back = ItemTags.create(new ResourceLocation("curios", "back"));

			ODDEYES_GLASSES = curio("oddeyes_glasses", OddeyesGlasses::new).tag(chaos, head).register();
			TRIPLE_STRIP_CAPE = curio("triple_strip_cape", TripleStripCape::new).tag(chaos, back).register();

			ABRAHADABRA = curio("abrahadabra", Abrahadabra::new).tag(chaos, curse, LHTagGen.NO_SEAL).register();
			NIDHOGGUR = curio("greed_of_nidhoggur", GreedOfNidhoggur::new).tag(chaos, curse, LHTagGen.NO_SEAL).register();

			RESTORATION = L2Hostility.REGISTRATE.item("pocket_of_restoration", p -> new PocketOfRestoration(p, 128))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())).override()
							.predicate(new ResourceLocation(L2Hostility.MODID, "filled"), 0.5f)
							.model(pvd.getBuilder(ctx.getName() + "_full")
									.parent(new ModelFile.UncheckedModelFile("item/generated"))
									.texture("layer0", "item/curio/" + ctx.getName() + "_full")))
					.tag(charm, LHTagGen.NO_SEAL).register();

		}

		// wands
		{
			ADDER = L2Hostility.REGISTRATE.item(
							"trait_adder_wand", p -> new TraitAdderWand(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.handheld(ctx)).register();
			TARGET = L2Hostility.REGISTRATE.item(
							"target_select_wand", p -> new TargetSelectWand(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.handheld(ctx)).register();
			AI = L2Hostility.REGISTRATE.item(
							"ai_config_wand", p -> new AiConfigWand(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.handheld(ctx)).register();
			EQUIPMENT = L2Hostility.REGISTRATE.item(
							"equipment_wand", p -> new EquipmentWand(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.handheld(ctx)).register();
		}

		SEAL = L2Hostility.REGISTRATE.item("sealed_item", p -> new SealedItem(p.stacksTo(1).fireResistant()))
				.removeTab(LHBlocks.TAB.getKey()).tag(LHTagGen.NO_SEAL).register();
	}

	private static <T extends Item> ItemBuilder<T, ?> curio(String str, NonNullFunction<Item.Properties, T> factory) {
		return L2Hostility.REGISTRATE.item(str, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())));
	}

	public static void register() {
	}

}
