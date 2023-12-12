package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.List;

public class RepellingTrait extends PushPullTrait {

	public RepellingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	protected int getRange() {
		return LHConfig.COMMON.repellRange.get();
	}

	@Override
	protected double getStrength(double dist) {
		return (1 - dist) * LHConfig.COMMON.repellStrength.get();
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (!event.getSource().isBypassInvul() &&
				!event.getSource().isBypassMagic() &&
				event.getSource().isProjectile()) {
			event.setCanceled(true);
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.COMMON.repellRange.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
	}

}
