package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class MobCapLootCondition implements LootItemCondition {

	@SerialClass.SerialField
	public int minLevel;

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
			if (MobTraitCap.HOLDER.isProper(le)) {
				MobTraitCap cap = MobTraitCap.HOLDER.get(le);
				return minLevel <= 0 || cap.getLevel() >= minLevel;
			}
		}
		return false;
	}

}
