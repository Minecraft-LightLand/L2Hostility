package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconBlock;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconBlockEntity;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconMenu;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconScreen;
import dev.xkmc.l2hostility.content.item.spawner.BurstSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlock;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2modularblock.DelegateBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import java.util.Locale;

public class LHBlocks {

	public static final RegistryEntry<CreativeModeTab> TAB = L2Hostility.REGISTRATE.buildL2CreativeTab(
			"hostility", "L2Hostility", e -> e.icon(() -> LHTraits.ENDER.get().asItem().getDefaultInstance()));

	public static final BlockEntry<Block> CHAOS, MIRACLE;

	public static final BlockEntry<DelegateBlock> BURST_SPAWNER;
	public static final BlockEntityEntry<BurstSpawnerBlockEntity> BE_BURST;

	public static final BlockEntry<HostilityBeaconBlock> HOSTILITY_BEACON;
	public static final BlockEntityEntry<HostilityBeaconBlockEntity> BE_BEACON;
	public static final MenuEntry<HostilityBeaconMenu> MT_BEACON;

	static {
		CHAOS = L2Hostility.REGISTRATE.block("chaos_block", p -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
				.simpleItem().register();

		MIRACLE = L2Hostility.REGISTRATE.block("miracle_block", p -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
				.simpleItem().register();

		BURST_SPAWNER = L2Hostility.REGISTRATE.block("hostility_spawner", p ->
						DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER)
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
						new HostilityBeaconBlock(BlockBehaviour.Properties.copy(Blocks.BEACON)))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().withExistingParent(ctx.getName(), "block/beacon")
						.texture("particle", pvd.modLoc("block/beacon_glass"))
						.texture("glass", pvd.modLoc("block/beacon_glass"))
						.texture("obsidian", pvd.mcLoc("block/crying_obsidian"))
						.texture("beacon", pvd.modLoc("block/beacon"))
						.renderType("translucent")
				)).tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
				.simpleItem().register();

		BE_BEACON = L2Hostility.REGISTRATE.blockEntity("hostility_beacon", HostilityBeaconBlockEntity::new)
				.validBlocks(HOSTILITY_BEACON).register();

		MT_BEACON = L2Hostility.REGISTRATE.menu("hostility_beacon", HostilityBeaconMenu::new, () -> HostilityBeaconScreen::new)
				.register();
	}

	public static void register() {

	}

}
