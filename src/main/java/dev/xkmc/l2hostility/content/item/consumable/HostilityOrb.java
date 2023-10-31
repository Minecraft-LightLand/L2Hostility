package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.xkmc.l2complements.init.data.LangData.IDS.BANNED;

public class HostilityOrb extends Item {

	public HostilityOrb(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!LHConfig.COMMON.allowHostilityOrb.get())
			return InteractionResultHolder.pass(stack);
		if (!level.isClientSide()) {
			var opt = ChunkDifficulty.at(level, player.blockPosition());
			if (opt.isPresent()) {
				var sec = opt.get().getSection(player.blockPosition().getY());
				if (!sec.isCleared()) {
					stack.shrink(1);
					sec.setClear(opt.get(), player.blockPosition());
				}
			}
		} else {
			PlayerDifficulty.HOLDER.get(player).updateChunkFlag = true;
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (!LHConfig.COMMON.allowHostilityOrb.get()) list.add(BANNED.get());
		list.add(LangData.ITEM_ORB.get().withStyle(ChatFormatting.GRAY));
	}

}
