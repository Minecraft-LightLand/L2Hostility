package dev.xkmc.l2hostility.content.item.spawner.block;

import dev.xkmc.l2hostility.content.capability.chunk.SectionDifficulty;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class BurstTraitMethod implements PlacementBlockMethod {

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		return state.setValue(TraitSpawnerBlock.STATE,
				SectionDifficulty.sectionAt(ctx.getLevel(), ctx.getClickedPos())
						.filter(SectionDifficulty::isCleared)
						.map(e -> TraitSpawnerBlock.State.CLEAR)
						.orElse(TraitSpawnerBlock.State.IDLE));
	}

}
