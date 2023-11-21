package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SealedItem extends Item {

	public static final String TIME = "sealTime", DATA = "sealedItem";

	public static ItemStack sealItem(ItemStack stack, int time) {
		if (stack.is(LHItems.SEAL.get())) {
			stack.getOrCreateTag().putInt(TIME, Math.max(stack.getOrCreateTag().getInt(TIME), time));
			return stack;
		}
		ItemStack ans = LHItems.SEAL.asStack();
		ans.getOrCreateTag().putInt(TIME, time);
		ans.getOrCreateTag().put(DATA, stack.save(new CompoundTag()));
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
		return ItemStack.of(stack.getOrCreateTag().getCompound(DATA));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return stack.getOrCreateTag().getInt(TIME);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.EAT;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.TOOLTIP_SEAL_DATA.get().withStyle(ChatFormatting.GRAY));
		list.add(ItemStack.of(stack.getOrCreateTag().getCompound(DATA)).getHoverName());
		int time = stack.getOrCreateTag().getInt(TIME);
		list.add(LangData.TOOLTIP_SEAL_TIME.get(
				Component.literal(time / 20 + "").withStyle(ChatFormatting.AQUA)
		).withStyle(ChatFormatting.RED));
	}
}
