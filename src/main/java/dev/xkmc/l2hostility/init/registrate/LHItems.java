package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2hostility.content.item.tools.consumable.HostilityOrb;
import dev.xkmc.l2hostility.content.item.tools.wand.AiConfigWand;
import dev.xkmc.l2hostility.content.item.tools.wand.EquipmentWand;
import dev.xkmc.l2hostility.content.item.tools.wand.TargetSelectWand;
import dev.xkmc.l2hostility.content.item.tools.wand.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;

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
			"hostility_orb", HostilityOrb::new).register();

	public static void register() {
	}

}
