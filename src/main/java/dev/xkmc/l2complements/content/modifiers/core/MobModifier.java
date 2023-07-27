package dev.xkmc.l2complements.content.modifiers.core;

import dev.xkmc.l2complements.content.config.ModifierConfig;
import dev.xkmc.l2complements.init.L2Hostility;
import dev.xkmc.l2complements.init.registrate.LHModifiers;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.world.entity.LivingEntity;

public class MobModifier extends NamedEntry<MobModifier> {

	public MobModifier() {
		super(LHModifiers.MODIFIERS);
	}

	@SuppressWarnings("ConstantConditions")
	public ModifierConfig getConfig() {
		ModifierConfig ans = L2Hostility.MODIFIER.getEntry(getRegistryName());
		if (ans == null) return ModifierConfig.DEFAULT;
		return ans;
	}

	public int getCost() {
		return getConfig().cost;
	}

	public boolean allow(LivingEntity le, int difficulty) {
		ModifierConfig config = getConfig();
		if (difficulty < config.cost) return false;
		if (config.blacklist.contains(le.getType())) return false;
		if (!config.whitelist.isEmpty() && !config.whitelist.contains(le.getType())) return false;
		return le.getRandom().nextDouble() < config.chance;
	}

	public void initialize(LivingEntity le, int level) {
	}

	public void tick(LivingEntity mob, int level) {
	}

}
