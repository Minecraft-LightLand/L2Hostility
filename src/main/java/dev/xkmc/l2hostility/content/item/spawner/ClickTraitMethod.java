package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2complements.init.data.LCLang;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2modularblock.mult.ToolTipBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class ClickTraitMethod implements UseWithoutItemBlockMethod, ToolTipBlockMethod {

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
		if (!LHConfig.SERVER.allowHostilitySpawner.get())
			return InteractionResult.PASS;
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

	@Override
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
		if (!LHConfig.SERVER.allowHostilitySpawner.get())
			list.add(LCLang.IDS.BANNED.get());
	}

}
