package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BottleOfSanity extends DrinkableBottleItem {

	public BottleOfSanity(Properties prop) {
		super(prop);
	}

	@Override
	protected void doServerLogic(ServerPlayer player) {
		PlayerDifficulty cap = LHMiscs.PLAYER.type().getOrCreate(player);
		LevelEditor editor = cap.getLevelEditor(player);
		editor.setBase(0);
		cap.dimensions.clear();
		cap.sync(player);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		if (!LHConfig.SERVER.banBottles.get())
			list.add(LangData.ITEM_BOTTLE_SANITY.get().withStyle(ChatFormatting.GRAY));
	}

}
