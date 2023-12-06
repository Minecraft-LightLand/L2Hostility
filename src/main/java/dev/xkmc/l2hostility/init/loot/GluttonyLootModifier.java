package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class GluttonyLootModifier extends LootModifier {

	public static final Codec<GluttonyLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i)
			.apply(i, GluttonyLootModifier::new));

	public GluttonyLootModifier(LootItemCondition... conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (context.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le) {
			if (MobTraitCap.HOLDER.isProper(le)) {
				MobTraitCap cap = MobTraitCap.HOLDER.get(le);
				double chance = cap.getLevel() * LHConfig.COMMON.gluttonyDropRate.get() * cap.dropRate;
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
	public Codec<GluttonyLootModifier> codec() {
		return CODEC;
	}

	public LootItemCondition[] getConditions() {
		return conditions;
	}

}
