package dev.xkmc.l2hostility.content.item.tools;

import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AiConfigWand extends Item {

	public AiConfigWand(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Mob mob) {
			if (!player.level().isClientSide()) {
				mob.setNoAi(!mob.isNoAi());
				player.sendSystemMessage(LangData.MSG_AI.get(mob.getDisplayName(), mob.isNoAi()));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

}
