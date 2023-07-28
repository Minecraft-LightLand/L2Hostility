package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.item.tools.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.CreativeModeTab;

@SuppressWarnings({"unsafe"})
@MethodsReturnNonnullByDefault
public class LHItems {

	public static final RegistryEntry<CreativeModeTab> TAB = L2Hostility.REGISTRATE.buildL2CreativeTab(
			"hostility", "L2Hostility", e -> e.icon(LHItems.ADDER::asStack));

	public static final ItemEntry<TraitAdderWand> ADDER = L2Hostility.REGISTRATE.item(
			"trait_adder", p -> new TraitAdderWand(p.stacksTo(1))).register();


	public static void register() {
	}

}
