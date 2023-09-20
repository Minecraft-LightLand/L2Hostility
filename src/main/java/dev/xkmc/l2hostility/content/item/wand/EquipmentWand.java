package dev.xkmc.l2hostility.content.item.wand;

import dev.xkmc.l2hostility.content.menu.equipments.EquipmentsMenuPvd;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EquipmentWand extends BaseWand {

	public EquipmentWand(Properties properties) {
		super(properties);
	}

	@Override
	public void clickTarget(ItemStack stack, Player player, LivingEntity entity) {
		if (entity instanceof Mob mob) {
			new EquipmentsMenuPvd(mob).open((ServerPlayer) player);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_WAND_EQUIPMENT.get().withStyle(ChatFormatting.GRAY));
	}

}
