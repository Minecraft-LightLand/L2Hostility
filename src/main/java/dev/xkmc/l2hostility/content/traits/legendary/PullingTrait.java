package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PullingTrait extends PushPullTrait {

	public PullingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	protected int getRange() {
		return LHConfig.COMMON.pullingRange.get();
	}

	@Override
	protected double getStrength(double dist) {
		return (1 - dist) * dist * LHConfig.COMMON.pullingStrength.get() * -4;
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.COMMON.pullingRange.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
	}

}
