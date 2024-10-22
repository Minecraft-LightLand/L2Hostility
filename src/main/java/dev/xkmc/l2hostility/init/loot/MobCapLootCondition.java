package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class MobCapLootCondition implements LootItemCondition {

	@SerialField
	public int minLevel;

	@SerialField
	public int maxLevel;

	@Deprecated
	public MobCapLootCondition() {

	}

	public MobCapLootCondition(int minLevel) {
		this.minLevel = minLevel;
	}

	@Override
	public LootItemConditionType getType() {
		return TraitGLMProvider.MOB_LEVEL.get();
	}

	@Override
	public boolean test(LootContext lootContext) {
		if (lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			int capLevel = LHMiscs.MOB.type().getExisting(le).map(MobTraitCap::getLevel).orElse(0);
			return (minLevel <= 0 || capLevel >= minLevel) && (maxLevel <= 0 || capLevel < maxLevel);
		}
		return false;
	}

}
