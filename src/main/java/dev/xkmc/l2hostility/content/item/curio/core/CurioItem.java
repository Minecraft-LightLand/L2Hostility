package dev.xkmc.l2hostility.content.item.curio.core;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;

import javax.annotation.Nonnull;

public class CurioItem extends Item {

	public CurioItem(Properties properties) {
		super(properties.stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	public CurioItem(Properties properties, int durability) {
		super(properties.durability(durability).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (this instanceof ICapItem<?> item) {
			return new ICapabilityProvider() {
				@Nonnull
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
					return CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(() -> item.create(stack)));
				}
			};
		}
		return null;
	}

}
