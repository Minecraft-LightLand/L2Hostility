package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.item.spawner.block.TraitSpawnerBlock;
import dev.xkmc.l2hostility.content.item.spawner.tile.BurstSpawnerBlockEntity;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2modularblock.DelegateBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import java.util.Locale;

public class LHBlocks {

	public static final RegistryEntry<CreativeModeTab> TAB = L2Hostility.REGISTRATE.buildL2CreativeTab(
			"hostility", "L2Hostility", e -> e.icon(() -> LHTraits.ENDER.get().asItem().getDefaultInstance()));

	public static final BlockEntry<DelegateBlock> BURST_SPAWNER;
	public static final BlockEntityEntry<BurstSpawnerBlockEntity> BE_BURST;

	static {
		BURST_SPAWNER = L2Hostility.REGISTRATE.block("hostility_spawner", p ->
						DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER),
								TraitSpawnerBlock.BASE, TraitSpawnerBlock.CLICK,
								TraitSpawnerBlock.BURST, TraitSpawnerBlock.BE_BURST))
				.blockstate((ctx, pvd) ->
						pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
							var s = state.getValue(TraitSpawnerBlock.STATE);
							String name = s == TraitSpawnerBlock.State.IDLE ?
									ctx.getName() : ctx.getName() + "_" + s.name().toLowerCase(Locale.ROOT);
							var ans = pvd.models().cubeAll(name, pvd.modLoc("block/" + name))
									.renderType("cutout");
							return ConfiguredModel.builder().modelFile(ans).build();
						})).simpleItem().register();

		BE_BURST = L2Hostility.REGISTRATE.blockEntity("hostility_spawner", BurstSpawnerBlockEntity::new)
				.validBlocks(BURST_SPAWNER).register();
	}

	public static void register() {

	}

}
