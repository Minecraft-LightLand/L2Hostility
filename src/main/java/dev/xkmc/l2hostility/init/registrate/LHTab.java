package dev.xkmc.l2hostility.init.registrate;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LHTab extends CreativeModeTab {

	public LHTab() {
		super("l2hostility.hostility");
	}

	@Override
	public @NotNull ItemStack makeIcon() {
		return LHTraits.ENDER.get().asItem().getDefaultInstance();
	}

}
