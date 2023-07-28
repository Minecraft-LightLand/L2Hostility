package dev.xkmc.l2hostility.content.item.tools;

import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TargetSelectWand extends Item {

	private static final String KEY = "cachedMobID";

	public TargetSelectWand(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (player.level().isClientSide())
			return InteractionResult.SUCCESS;
		stack = player.getItemInHand(hand);
		if (stack.getOrCreateTag().contains(KEY)) {
			Entity other = entity.level().getEntity(stack.getOrCreateTag().getInt(KEY));
			if (other instanceof LivingEntity le) {
				boolean succeed = false;
				if (entity instanceof Mob mob) {
					mob.setTarget(le);
					succeed = true;
				}
				if (le instanceof Mob mob) {
					mob.setTarget(entity);
					succeed = true;
				}
				stack.getOrCreateTag().remove(KEY);
				if (succeed) {
					player.sendSystemMessage(LangData.MSG_SET_TARGET.get(entity.getDisplayName(), le.getDisplayName()));
				} else {
					player.sendSystemMessage(LangData.MSG_TARGET_FAIL.get(entity.getDisplayName(), le.getDisplayName()));
				}
				return InteractionResult.SUCCESS;
			}
		}
		stack.getOrCreateTag().putInt(KEY, entity.getId());
		player.sendSystemMessage(LangData.MSG_TARGET_RECORD.get(entity.getDisplayName()));
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (level != null && stack.getOrCreateTag().contains(KEY)) {
			Entity other = level.getEntity(stack.getOrCreateTag().getInt(KEY));
			if (other instanceof LivingEntity le) {
				list.add(LangData.MSG_TARGET_RECORD.get(
						le.getDisplayName().copy().withStyle(ChatFormatting.AQUA)
				).withStyle(ChatFormatting.GRAY));
			}
		}
	}

}
