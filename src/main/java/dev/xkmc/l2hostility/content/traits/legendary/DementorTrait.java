package dev.xkmc.l2hostility.content.traits.legendary;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class DementorTrait extends LegendaryTrait {

	public DementorTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onCreateSource(int level, LivingEntity attacker, LivingAttackEvent event) {
		event.getSource().bypassArmor();
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (!event.getSource().isBypassInvul() &&
				!event.getSource().isBypassMagic() &&
				!event.getSource().isMagic()) {
			event.setCanceled(true);
		}
	}
}
