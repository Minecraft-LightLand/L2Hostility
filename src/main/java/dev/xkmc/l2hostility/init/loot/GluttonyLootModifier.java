package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class GluttonyLootModifier extends LootModifier {

	public static final MapCodec<GluttonyLootModifier> CODEC = RecordCodecBuilder.mapCodec(i -> codecStart(i)
			.apply(i, GluttonyLootModifier::new));

	public GluttonyLootModifier(LootItemCondition... conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (context.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			var opt = LHMiscs.MOB.type().getExisting(le);
			if (opt.isPresent()) {
				MobTraitCap cap = opt.get();
				double factor = cap.dropRate;
				if (context.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
					Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
					var pl = LHMiscs.PLAYER.type().getOrCreate(player);
					for (var stack : CurseCurioItem.getFromPlayer(player)) {
						factor *= stack.item().getLootFactor(stack.stack(), pl, cap);
					}
				}

				double chance = factor * cap.getLevel() * LHConfig.SERVER.gluttonyBottleDropRate.get();
				int base = (int) chance;
				if (context.getRandom().nextDouble() < chance - base) {
					base++;
				}
				if (base > 0) {
					list.add(new ItemStack(LHItems.BOTTLE_CURSE.get(), base));
				}
			}
		}
		return list;
	}

	@Override
	public MapCodec<GluttonyLootModifier> codec() {
		return CODEC;
	}

	public LootItemCondition[] getConditions() {
		return conditions;
	}

}
