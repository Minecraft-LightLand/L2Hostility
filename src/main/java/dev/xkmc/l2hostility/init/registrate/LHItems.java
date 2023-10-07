package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2hostility.content.item.consumable.BottleOfCurse;
import dev.xkmc.l2hostility.content.item.consumable.BottleOfSanity;
import dev.xkmc.l2hostility.content.item.consumable.HostilityOrb;
import dev.xkmc.l2hostility.content.item.curio.*;
import dev.xkmc.l2hostility.content.item.tool.DetectorGlasses;
import dev.xkmc.l2hostility.content.item.tool.WitchWand;
import dev.xkmc.l2hostility.content.item.wand.AiConfigWand;
import dev.xkmc.l2hostility.content.item.wand.EquipmentWand;
import dev.xkmc.l2hostility.content.item.wand.TargetSelectWand;
import dev.xkmc.l2hostility.content.item.wand.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.TagGen;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

@SuppressWarnings({"unsafe"})
@MethodsReturnNonnullByDefault
public class LHItems {

	public static final ItemEntry<HostilityOrb> HOSTILITY_ORB;
	public static final ItemEntry<BottleOfCurse> BOTTLE_CURSE;
	public static final ItemEntry<BottleOfSanity> BOTTLE_SANITY;

	public static final ItemEntry<Item> DETECTOR;
	public static final ItemEntry<DetectorGlasses> DETECTOR_GLASSES;
	public static final ItemEntry<WitchWand> WITCH_WAND;
	public static final ItemEntry<Item> CHAOS_INGOT;
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
	public static final ItemEntry<FlamingThorn> FLAMING_THORN;
	public static final ItemEntry<ImagineBreaker> IMAGINE_BREAKER;

	public static final ItemEntry<TraitAdderWand> ADDER;
	public static final ItemEntry<TargetSelectWand> TARGET;
	public static final ItemEntry<AiConfigWand> AI;
	public static final ItemEntry<EquipmentWand> EQUIPMENT;


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
		}

		// equipments
		{
			DETECTOR = L2Hostility.REGISTRATE.item(
					"hostility_detector", p -> new Item(p.stacksTo(1))).register();
			DETECTOR_GLASSES = L2Hostility.REGISTRATE.item(
							"detector_glasses", p -> new DetectorGlasses(p.stacksTo(1)))
					.tag(ItemTags.create(new ResourceLocation("curios", "head")))
					.register();

			TagKey<Item> chaos = TagGen.CHAOS_CURIO;

			WITCH_WAND = L2Hostility.REGISTRATE.item("witch_wand", p -> new WitchWand(p
							.durability(300).rarity(Rarity.EPIC).fireResistant()))
					.model((ctx, pvd) -> pvd.handheld(ctx)).tag(chaos).register();


			CHAOS_INGOT = L2Hostility.REGISTRATE.item("chaos_ingot", p -> new Item(p.rarity(Rarity.EPIC).fireResistant())).register();

			TagKey<Item> charm = ItemTags.create(new ResourceLocation("curios", "charm"));

			CURSE_ENVY = L2Hostility.REGISTRATE.item("curse_of_envy", CurseOfEnvy::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();
			CURSE_GLUTTONY = L2Hostility.REGISTRATE.item("curse_of_gluttony", CurseOfGluttony::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();
			CURSE_GREED = L2Hostility.REGISTRATE.item("curse_of_greed", CurseOfGreed::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();
			CURSE_LUST = L2Hostility.REGISTRATE.item("curse_of_lust", CurseOfLust::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();
			CURSE_PRIDE = L2Hostility.REGISTRATE.item("curse_of_pride", CurseOfPride::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();
			CURSE_SLOTH = L2Hostility.REGISTRATE.item("curse_of_sloth", CurseOfSloth::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();
			CURSE_WRATH = L2Hostility.REGISTRATE.item("curse_of_wrath", CurseOfWrath::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, charm).register();

			TagKey<Item> ring = ItemTags.create(new ResourceLocation("curios", "ring"));

			RING_OCEAN = L2Hostility.REGISTRATE.item("ring_of_ocean", RingOfOcean::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, ring).register();
			RING_LIFE = L2Hostility.REGISTRATE.item("ring_of_life", RingOfLife::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, ring).register();
			RING_DIVINITY = L2Hostility.REGISTRATE.item("ring_of_divinity", RingOfDivinity::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, ring).register();
			RING_REFLECTION = L2Hostility.REGISTRATE.item("ring_of_reflection", RingOfReflection::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, ring).register();

			TagKey<Item> hand = ItemTags.create(new ResourceLocation("curios", "hands"));

			FLAMING_THORN = L2Hostility.REGISTRATE.item("flaming_thorn", FlamingThorn::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, hand).register();

			IMAGINE_BREAKER = L2Hostility.REGISTRATE.item("imagine_breaker", ImagineBreaker::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(chaos, hand).register();
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
	}

	public static void register() {
	}

}
