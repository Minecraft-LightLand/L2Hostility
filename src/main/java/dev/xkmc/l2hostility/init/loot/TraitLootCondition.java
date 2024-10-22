package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class TraitLootCondition implements LootItemCondition {

	@SerialField
	public MobTrait trait;

	@SerialField
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
			var opt = LHMiscs.MOB.type().getExisting(le);
			if (opt.isPresent()) {
				MobTraitCap cap = opt.get();
				if (!cap.hasTrait(trait)) return false;
				int lv = cap.getTraitLevel(trait);
				return lv >= minLevel && lv <= maxLevel;
			}
		}
		return false;
	}

}
