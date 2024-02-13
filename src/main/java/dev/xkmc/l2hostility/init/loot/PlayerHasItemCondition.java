package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class PlayerHasItemCondition implements LootItemCondition {

	@SerialClass.SerialField
	public Item item;

	@Deprecated
	public PlayerHasItemCondition() {

	}

	public PlayerHasItemCondition(Item item) {
		this.item = item;
	}

	@Override
	public LootItemConditionType getType() {
		return TraitGLMProvider.HAS_ITEM.get();
	}

	@Override
	public boolean test(LootContext lootContext) {
		if (LHConfig.COMMON.disableHostilityLootCurioRequirement.get()) {
			return true;
		}
		if (!lootContext.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) return false;
		Player player = lootContext.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
		return CurioCompat.hasItem(player, item);
	}

}
