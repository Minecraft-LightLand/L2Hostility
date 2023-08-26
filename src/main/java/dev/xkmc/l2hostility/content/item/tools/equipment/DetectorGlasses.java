package dev.xkmc.l2hostility.content.item.tools.equipment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DetectorGlasses extends Item {

	public DetectorGlasses(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

}
