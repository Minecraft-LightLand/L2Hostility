package dev.xkmc.l2hostility.content.item.beacon;

import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2modularblock.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;

public class HostilityBeaconBlock extends DelegateEntityBlockImpl implements BeaconBeamBlock {

	public static final BlockEntityBlockMethodImpl<HostilityBeaconBlockEntity> BE =
			new BlockEntityBlockMethodImpl<>(LHBlocks.BE_BEACON, HostilityBeaconBlockEntity.class);

	public HostilityBeaconBlock(Properties p) {
		super(p, BE);
	}

	public DyeColor getColor() {
		return DyeColor.WHITE;
	}

}
