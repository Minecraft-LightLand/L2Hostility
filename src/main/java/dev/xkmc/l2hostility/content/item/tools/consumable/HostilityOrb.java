package dev.xkmc.l2hostility.content.item.tools.consumable;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HostilityOrb extends Item {

	public HostilityOrb(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			var opt = ChunkDifficulty.at(level, player.blockPosition());
			if (opt.isPresent()) {
				var sec = opt.get().getSection(player.blockPosition().getY());
				if (!sec.isCleared()) {
					stack.shrink(1);
					sec.setClear(opt.get(), player.blockPosition());
				}
			}
		}
		return InteractionResultHolder.success(stack);
	}
}
