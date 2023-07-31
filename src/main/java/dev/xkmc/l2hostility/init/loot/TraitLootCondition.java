package dev.xkmc.l2hostility.init.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Objects;

@SerialClass
public class TraitLootCondition implements LootItemCondition {

	@SerialClass.SerialField
	public MobTrait trait;

	@SerialClass.SerialField
	public int minLevel, maxLevel;

	@Deprecated
	public TraitLootCondition() {

	}

	public TraitLootCondition(MobTrait trait, int minLevel, int maxLevel) {
		this.trait = trait;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	@Override
	public LootItemConditionType getType() {
		return TraitGLMProvider.TRAIT_AND_LEVEL.get();
	}

	@Override
	public boolean test(LootContext lootContext) {
		if (lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			if (MobTraitCap.HOLDER.isProper(le)) {
				MobTraitCap cap = MobTraitCap.HOLDER.get(le);
				if (!cap.traits.containsKey(trait)) return false;
				int lv = cap.traits.get(trait);
				return lv >= minLevel && lv <= maxLevel;
			}
		}
		return false;
	}

}
