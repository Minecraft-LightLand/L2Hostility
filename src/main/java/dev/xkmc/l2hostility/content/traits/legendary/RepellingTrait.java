package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class RepellingTrait extends PushPullTrait {

	public RepellingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	protected int getRange() {
		return LHConfig.SERVER.repellRange.get();
	}

	@Override
	protected double getStrength(double dist) {
		return (1 - dist) * LHConfig.SERVER.repellStrength.get();
	}

	@Override
	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		return !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				event.getSource().is(DamageTypeTags.IS_PROJECTILE);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.SERVER.repellRange.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
	}

}
