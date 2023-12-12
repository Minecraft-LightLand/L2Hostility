package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.one.LightBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BaseTraitMethod implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, LightBlockMethod {

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TraitSpawnerBlock.STATE);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.IDLE);
	}

	@Override
	public int getLightValue(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getValue(TraitSpawnerBlock.STATE).light();
	}

}
