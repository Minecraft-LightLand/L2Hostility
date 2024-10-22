package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.Tags;

public class DementorTrait extends LegendaryTrait {

	public DementorTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
		if (event.getResult() == L2DamageTypes.MOB_ATTACK)
			event.enable(DefaultDamageState.BYPASS_ARMOR);
	}

	@Override
	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		return !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				!event.getSource().is(Tags.DamageTypes.IS_MAGIC);
	}

}
