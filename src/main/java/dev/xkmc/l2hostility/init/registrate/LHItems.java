package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2hostility.content.item.tools.AiConfigWand;
import dev.xkmc.l2hostility.content.item.tools.EquipmentWand;
import dev.xkmc.l2hostility.content.item.tools.TargetSelectWand;
import dev.xkmc.l2hostility.content.item.tools.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.MethodsReturnNonnullByDefault;

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

	public static void register() {
	}

}
