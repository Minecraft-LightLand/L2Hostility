package dev.xkmc.l2hostility.content.item.tools.consumable;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BottleOfCurse extends DrinkableBottleItem {

	public BottleOfCurse(Properties prop) {
		super(prop);
	}

	@Override
	protected void doServerLogic(ServerPlayer player) {
		PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
		LevelEditor editor = cap.getLevelEditor();
		editor.addBase(LHConfig.COMMON.bottleOfCurseLevel.get());
		cap.sync();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_BOTTLE_CURSE.get().withStyle(ChatFormatting.GRAY));
	}

}
