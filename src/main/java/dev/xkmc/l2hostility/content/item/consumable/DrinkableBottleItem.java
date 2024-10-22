package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public abstract class DrinkableBottleItem extends Item {

	public DrinkableBottleItem(Item.Properties props) {
		super(props);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		Player player = entity instanceof Player ? (Player) entity : null;
		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
		}

		if (!level.isClientSide && player instanceof ServerPlayer sp) {
			doServerLogic(sp);
		}

		if (player != null) {
			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}

		if (player == null || !player.getAbilities().instabuild) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (player != null) {
				player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		entity.gameEvent(GameEvent.DRINK);
		return stack;
	}

	protected abstract void doServerLogic(ServerPlayer player);

	public int getUseDuration(ItemStack p_43001_) {
		return 32;
	}

	public UseAnim getUseAnimation(ItemStack p_42997_) {
		return UseAnim.DRINK;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (LHConfig.SERVER.banBottles.get() || CurioCompat.hasItemInCurio(player, LHItems.DIVINITY_LIGHT.get()))
			return InteractionResultHolder.fail(stack);
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

}
