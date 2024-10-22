package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RingOfLife extends CurseCurioItem {

	public RingOfLife(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		int perc = (int) Math.round(LHConfig.SERVER.ringOfLifeMaxDamage.get() * 100);
		list.add(LangData.ITEM_RING_LIFE.get(perc).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void onDamage(ItemStack stack, LivingEntity user, DamageData.Defence event) {
		boolean bypassInvul = event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY);
		boolean bypassMagic = event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS);
		if (!bypassInvul && !bypassMagic) {
			var rl = getID();
			float max = (float) (user.getMaxHealth() * LHConfig.SERVER.ringOfLifeMaxDamage.get());
			event.addDealtModifier(DamageModifier.nonlinearMiddle(213, dmg -> Math.min(dmg, max), rl));
		}
	}

}
