package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class MasterTrait extends LegendaryTrait {

	@Nullable
	private static EntityConfig.MasterConfig getConfig(EntityType<?> type) {
		var config = L2Hostility.ENTITY.getMerged().get(type);
		if (config == null) return null;
		return config.asMaster;
	}

	public MasterTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
		return getConfig(le.getType()) != null && super.allow(le, difficulty, maxModLv);
	}
}
