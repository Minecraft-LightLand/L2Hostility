package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PullingTrait extends PushPullTrait {

	public PullingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	protected int getRange() {
		return LHConfig.SERVER.pullingRange.get();
	}

	@Override
	protected double getStrength(double dist) {
		return (1 - dist) * dist * LHConfig.SERVER.pullingStrength.get() * -4;
	}

	@Override
	public void addDetail(RegistryAccess access, List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(LHConfig.SERVER.pullingRange.get() + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GRAY));
	}

}
