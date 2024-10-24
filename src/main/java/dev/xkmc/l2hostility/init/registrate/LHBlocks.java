package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2hostility.content.item.beacon.*;
import dev.xkmc.l2hostility.content.item.spawner.BurstSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlock;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import java.util.Locale;

public class LHBlocks {

	public static final SimpleEntry<CreativeModeTab> TAB = L2Hostility.REGISTRATE.buildL2CreativeTab(
			"hostility", "L2Hostility", e -> e.icon(() -> LHItems.LOOT_1.get().asItem().getDefaultInstance()));

	public static final BlockEntry<Block> CHAOS, MIRACLE;

	public static final BlockEntry<DelegateBlock> BURST_SPAWNER;
	public static final BlockEntityEntry<BurstSpawnerBlockEntity> BE_BURST;

	public static final BlockEntry<HostilityBeaconBlock> HOSTILITY_BEACON;
	public static final BlockEntityEntry<HostilityBeaconBlockEntity> BE_BEACON;
	public static final MenuEntry<HostilityBeaconMenu> MT_BEACON;

	static {
		CHAOS = L2Hostility.REGISTRATE.block("chaos_block", p -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL, LHTagGen.BEACON_BLOCK)
				.simpleItem().register();

		MIRACLE = L2Hostility.REGISTRATE.block("miracle_block", p -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL, LHTagGen.BEACON_BLOCK)
				.simpleItem().register();

		BURST_SPAWNER = L2Hostility.REGISTRATE.block("hostility_spawner", p ->
						DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER)
										.strength(50.0F, 1200.0F),
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

		HOSTILITY_BEACON = L2Hostility.REGISTRATE.block("hostility_beacon", p ->
						new HostilityBeaconBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON)))
				.blockstate(HostilityBeaconBlock::buildModel).tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
				.simpleItem().register();

		BE_BEACON = L2Hostility.REGISTRATE.blockEntity("hostility_beacon", HostilityBeaconBlockEntity::new)
				.renderer(() -> HostilityBeaconRenderer::new)
				.validBlocks(HOSTILITY_BEACON).register();

		MT_BEACON = L2Hostility.REGISTRATE.menu("hostility_beacon", HostilityBeaconMenu::new, () -> HostilityBeaconScreen::new)
				.register();
	}

	public static void register() {

	}

}
