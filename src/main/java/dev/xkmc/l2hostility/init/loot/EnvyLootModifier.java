package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class EnvyLootModifier extends LootModifier {

	public static final Codec<EnvyLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i)
			.apply(i, EnvyLootModifier::new));

	public EnvyLootModifier(LootItemCondition... conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (context.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			if (MobTraitCap.HOLDER.isProper(le)) {
				MobTraitCap cap = MobTraitCap.HOLDER.get(le);
				double factor = cap.dropRate;
				if (context.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
					Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
					var pl = PlayerDifficulty.HOLDER.get(player);
					for (var stack : CurseCurioItem.getFromPlayer(player)) {
						factor *= stack.item().getLootFactor(stack.stack(), pl, cap);
					}
				}
				for (var entry : cap.traits.entrySet()) {
					double chance = factor * entry.getValue() * LHConfig.COMMON.envyDropRate.get();
					if (cap.fullDrop || context.getRandom().nextDouble() < chance) {
						list.add(entry.getKey().asItem().getDefaultInstance());
					}
				}
			}
		}
		return list;
	}

	@Override
	public Codec<EnvyLootModifier> codec() {
		return CODEC;
	}

	public LootItemCondition[] getConditions() {
		return conditions;
	}

}
