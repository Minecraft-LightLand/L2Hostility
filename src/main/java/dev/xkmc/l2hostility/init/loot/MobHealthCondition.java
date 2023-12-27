package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class MobHealthCondition implements LootItemCondition {

	@SerialClass.SerialField
	public int minHealth;

	@Deprecated
	public MobHealthCondition() {

	}

	public MobHealthCondition(int minHealth) {
		this.minHealth = minHealth;
	}

	@Override
	public LootItemConditionType getType() {
		return TraitGLMProvider.MIN_HEALTH.get();
	}

	@Override
	public boolean test(LootContext lootContext) {
		if (lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			return le.getMaxHealth() >= minHealth;
		}
		return false;
	}

}
