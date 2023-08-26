package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2hostility.content.item.tools.consumable.HostilityOrb;
import dev.xkmc.l2hostility.content.item.tools.equipment.DetectorGlasses;
import dev.xkmc.l2hostility.content.item.tools.wand.AiConfigWand;
import dev.xkmc.l2hostility.content.item.tools.wand.EquipmentWand;
import dev.xkmc.l2hostility.content.item.tools.wand.TargetSelectWand;
import dev.xkmc.l2hostility.content.item.tools.wand.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

@SuppressWarnings({"unsafe"})
@MethodsReturnNonnullByDefault
public class LHItems {

	public static final ItemEntry<TraitAdderWand> ADDER = L2Hostility.REGISTRATE.item(
					"trait_adder_wand", p -> new TraitAdderWand(p.stacksTo(1)))
			.model((ctx, pvd) -> pvd.handheld(ctx)).register();

	public static final ItemEntry<TargetSelectWand> TARGET = L2Hostility.REGISTRATE.item(
					"target_select_wand", p -> new TargetSelectWand(p.stacksTo(1)))
			.model((ctx, pvd) -> pvd.handheld(ctx)).register();

	public static final ItemEntry<AiConfigWand> AI = L2Hostility.REGISTRATE.item(
					"ai_config_wand", p -> new AiConfigWand(p.stacksTo(1)))
			.model((ctx, pvd) -> pvd.handheld(ctx)).register();

	public static final ItemEntry<EquipmentWand> EQUIPMENT = L2Hostility.REGISTRATE.item(
					"equipment_wand", p -> new EquipmentWand(p.stacksTo(1)))
			.model((ctx, pvd) -> pvd.handheld(ctx)).register();

	public static final ItemEntry<Item> DETECTOR = L2Hostility.REGISTRATE.item(
			"hostility_detector", p -> new Item(p.stacksTo(1))).register();

	public static final ItemEntry<HostilityOrb> HOSTILITY_ORB = L2Hostility.REGISTRATE.item(
			"hostility_orb", p -> new HostilityOrb(p.stacksTo(16).rarity(Rarity.EPIC))).register();

	public static final ItemEntry<DetectorGlasses> DETECTOR_GLASSES = L2Hostility.REGISTRATE.item(
					"detector_glasses", p -> new DetectorGlasses(p.stacksTo(1)))
			.tag(ItemTags.create(new ResourceLocation("curios", "head")))
			.register();

	public static void register() {
	}

}
