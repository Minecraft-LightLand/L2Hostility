package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2core.util.DCStack;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class SealedItem extends Item {

	public static ItemStack sealItem(ItemStack stack, int time) {
		if (stack.is(LHItems.SEAL.get())) {
			LHItems.DC_SEAL_TIME.set(stack, time);
			return stack;
		}
		ItemStack ans = LHItems.SEAL.asStack();
		LHItems.DC_SEAL_TIME.set(ans, time);
		LHItems.DC_SEAL_STACK.set(ans, new DCStack(stack));
		if (stack.getEnchantmentLevel(LHEnchantments.VANISH.holder()) > 0)
			ans.enchant(LHEnchantments.VANISH.holder(), 1);
		if (stack.getEnchantmentLevel(LCEnchantments.SOUL_BOUND.holder()) > 0)
			ans.enchant(LCEnchantments.SOUL_BOUND.holder(), 1);
		return ans;
	}

	public SealedItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		user.stopUsingItem();
		var sealed = LHItems.DC_SEAL_STACK.get(stack);
		if (sealed == null) return ItemStack.EMPTY;
		return sealed.stack();
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity le) {
		return LHItems.DC_SEAL_TIME.getOrDefault(stack, 0);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.EAT;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.TOOLTIP_SEAL_DATA.get().withStyle(ChatFormatting.GRAY));
		var sealed = LHItems.DC_SEAL_STACK.get(stack);
		if (sealed != null) list.add(sealed.stack().getHoverName());
		int time = LHItems.DC_SEAL_TIME.getOrDefault(stack, 0);
		list.add(LangData.TOOLTIP_SEAL_TIME.get(
				Component.literal(time / 20 + "").withStyle(ChatFormatting.AQUA)
		).withStyle(ChatFormatting.RED));
	}
}
