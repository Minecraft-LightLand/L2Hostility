package dev.xkmc.l2hostility.content.item.spawner.block;

import dev.xkmc.l2hostility.content.item.spawner.tile.TraitSpawnerBlockEntity;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickTraitMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		switch (state.getValue(TraitSpawnerBlock.STATE)) {
			case IDLE -> {
				if (level.getBlockEntity(pos) instanceof TraitSpawnerBlockEntity be) {
					be.activate();
				}
				return InteractionResult.SUCCESS;
			}
			case FAILED -> {
				if (level.getBlockEntity(pos) instanceof TraitSpawnerBlockEntity be) {
					be.deactivate();
				}
				return InteractionResult.SUCCESS;
			}
			default -> {
				return InteractionResult.PASS;
			}
		}
	}

}
