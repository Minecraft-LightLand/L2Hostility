package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class TraitLootModifier extends LootModifier {

	public static final Codec<TraitLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i).and(i.group(
					LHTraits.TRAITS.get().getCodec().fieldOf("trait").forGetter(e -> e.trait),
					Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance),
					Codec.DOUBLE.fieldOf("rankBonus").forGetter(e -> e.rankBonus),
					ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result)))
			.apply(i, TraitLootModifier::new));

	private final MobTrait trait;
	private final double chance, rankBonus;
	private final ItemStack result;

	public TraitLootModifier(MobTrait trait, double chance, double rankBonus, ItemStack result, LootItemCondition... conditionsIn) {
		super(conditionsIn);
		this.trait = trait;
		this.chance = chance;
		this.rankBonus = rankBonus;
		this.result = result;
	}

	private TraitLootModifier(LootItemCondition[] conditionsIn, MobTrait trait, double chance, double rankBonus, ItemStack result) {
		super(conditionsIn);
		this.trait = trait;
		this.chance = chance;
		this.rankBonus = rankBonus;
		this.result = result;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (context.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			if (MobTraitCap.HOLDER.isProper(le)) {
				MobTraitCap cap = MobTraitCap.HOLDER.get(le);
				if (cap.traits.containsKey(trait)) {
					int lv = cap.traits.get(trait);
					double rate = chance + lv * rankBonus;
					int count = 0;
					for (int i = 0; i < result.getCount(); i++) {
						if (context.getRandom().nextDouble() < rate) {
							count++;
						}
					}
					if (count > 0) {
						ItemStack ans = result.copy();
						ans.setCount(count);
						list.add(ans);
					}
				}
			}
		}
		return list;
	}

	@Override
	public Codec<TraitLootModifier> codec() {
		return CODEC;
	}

}
