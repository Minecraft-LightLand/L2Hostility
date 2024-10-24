package dev.xkmc.l2hostility.content.item.beacon;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2modularblock.core.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class HostilityBeaconBlock extends DelegateEntityBlockImpl implements BeaconBeamBlock {

	public static final BlockEntityBlockMethodImpl<HostilityBeaconBlockEntity> BE =
			new BlockEntityBlockMethodImpl<>(LHBlocks.BE_BEACON, HostilityBeaconBlockEntity.class);

	public HostilityBeaconBlock(Properties p) {
		super(p, BE);
	}

	public DyeColor getColor() {
		return DyeColor.RED;
	}

	public static void buildModel(DataGenContext<Block, HostilityBeaconBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.models().withExistingParent(ctx.getName(), "block/beacon")
				.texture("particle", pvd.modLoc("block/beacon_glass"))
				.texture("glass", pvd.modLoc("block/beacon_glass"))
				.texture("obsidian", pvd.mcLoc("block/crying_obsidian"))
				.texture("beacon", pvd.modLoc("block/beacon"))
				.renderType("translucent");
		pvd.getMultipartBuilder(ctx.get())
				.part().modelFile(pvd.models().getBuilder(ctx.getName() + "_base")
						.parent(new ModelFile.UncheckedModelFile(L2Hostility.loc("block/beacon")))
						.texture("particle", pvd.modLoc("block/beacon_glass"))
						.texture("obsidian", pvd.mcLoc("block/crying_obsidian"))
						.texture("beacon", pvd.modLoc("block/beacon"))
						.renderType("translucent")
				).addModel().end()
				.part().modelFile(pvd.models().cubeAll(ctx.getName() + "_glass",
								pvd.modLoc("block/beacon_glass"))
						.renderType("translucent")).addModel().end();
	}
}
