package dev.xkmc.l2hostility.init.registrate;

import dev.xkmc.l2hostility.content.item.spawner.BurstSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlock;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import java.util.Locale;

public class LHBlocks {


	public static final LHTab TAB = new LHTab();

	static {
		L2Hostility.REGISTRATE.creativeModeTab(() -> TAB);
	}

	public static final BlockEntry<DelegateBlock> BURST_SPAWNER;
	public static final BlockEntityEntry<BurstSpawnerBlockEntity> BE_BURST;

	static {
		BURST_SPAWNER = L2Hostility.REGISTRATE.block("hostility_spawner", p ->
						DelegateBlock.newBaseBlock(DelegateBlockProperties.copy(Blocks.SPAWNER)
										.make(e -> e.strength(50.0F, 1200.0F)),
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
						}))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.simpleItem().register();

		BE_BURST = L2Hostility.REGISTRATE.blockEntity("hostility_spawner", BurstSpawnerBlockEntity::new)
				.validBlocks(BURST_SPAWNER).register();
	}

	public static void register() {

	}

}
