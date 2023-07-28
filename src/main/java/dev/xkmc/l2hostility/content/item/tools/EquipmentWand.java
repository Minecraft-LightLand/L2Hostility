package dev.xkmc.l2hostility.content.item.tools;

import dev.xkmc.l2hostility.content.menu.equipments.EquipmentsMenuPvd;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EquipmentWand extends Item {

	public EquipmentWand(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Mob mob) {
			if (!player.level().isClientSide()) {
				new EquipmentsMenuPvd(mob).open((ServerPlayer) player);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}
}
