package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TraitLootModifier extends LootModifier implements ITraitLootRecipe {

	public static final Codec<TraitLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i).and(i.group(
					LHTraits.TRAITS.get().getCodec().fieldOf("trait").forGetter(e -> e.trait),
					Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance),
					Codec.DOUBLE.fieldOf("rankBonus").forGetter(e -> e.rankBonus),
					ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result)))
			.apply(i, TraitLootModifier::new));

	public final MobTrait trait;
	public final double chance, rankBonus;
	public final ItemStack result;

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
				if (cap.hasTrait(trait)) {
					double factor = cap.dropRate;
					if (context.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
						Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
						var pl = PlayerDifficulty.HOLDER.get(player);
						for (var stack : CurseCurioItem.getFromPlayer(player)) {
							factor *= stack.item().getLootFactor(stack.stack(), pl, cap);
						}
					}
					int lv = cap.getTraitLevel(trait);
					double rate = chance + lv * rankBonus;
					int count = 0;
					for (int i = 0; i < result.getCount() * factor; i++) {
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

	public LootItemCondition[] getConditions() {
		return conditions;
	}

	@Override
	public List<ItemStack> getResults() {
		return List.of(result);
	}

	@Override
	public List<ItemStack> getInputs() {
		Set<MobTrait> set = new LinkedHashSet<>();
		List<ItemStack> ans = new ArrayList<>();
		set.add(trait);
		for (var c : getConditions()) {
			if (c instanceof TraitLootCondition cl) {
				set.add(cl.trait);
			} else if (c instanceof PlayerHasItemCondition item) {
				ans.add(item.item.getDefaultInstance());
			}
		}
		for (var e : set) {
			ans.add(e.asItem().getDefaultInstance());
		}
		return ans;
	}

	@Override
	public void addTooltip(List<Component> list) {
		int max = trait.getConfig().max_rank;
		int min = 1;
		int minLevel = 0;
		List<TraitLootCondition> other = new ArrayList<>();
		List<PlayerHasItemCondition> itemReq = new ArrayList<>();
		for (var c : getConditions()) {
			if (c instanceof TraitLootCondition cl) {
				if (cl.trait == trait) {
					max = Math.min(max, cl.maxLevel);
					min = Math.max(min, cl.minLevel);
				} else {
					other.add(cl);
				}
			} else if (c instanceof MobCapLootCondition cl) {
				minLevel = cl.minLevel;
			} else if (c instanceof PlayerHasItemCondition cl) {
				itemReq.add(cl);
			}
		}
		if (minLevel > 0) {
			list.add(LangData.LOOT_MIN_LEVEL.get(Component.literal(minLevel + "")
							.withStyle(ChatFormatting.AQUA))
					.withStyle(ChatFormatting.LIGHT_PURPLE));
		}
		for (int lv = min; lv <= max; lv++) {
			list.add(LangData.LOOT_CHANCE.get(
							Component.literal(Math.round((chance + rankBonus * lv) * 100) + "%")
									.withStyle(ChatFormatting.AQUA),
							trait.getDesc().withStyle(ChatFormatting.GOLD),
							Component.literal(lv + "").withStyle(ChatFormatting.AQUA))
					.withStyle(ChatFormatting.GRAY));
		}
		for (var c : other) {
			int cmin = Math.max(c.minLevel, 1);
			int cmax = Math.min(c.maxLevel, c.trait.getMaxLevel());
			String str = cmax == cmin ?
					cmin + "" :
					cmax >= c.trait.getMaxLevel() ?
							cmin + "+" :
							cmin + "-" + cmax;
			list.add(LangData.LOOT_OTHER_TRAIT.get(c.trait.getDesc().withStyle(ChatFormatting.GOLD),
							Component.literal(str).withStyle(ChatFormatting.AQUA))
					.withStyle(ChatFormatting.RED));
		}
		for (var e : itemReq) {
			var name = e.item.getDescription().copy().withStyle(ChatFormatting.LIGHT_PURPLE);
			list.add(LangData.TOOLTIP_JEI_REQUIRED.get(name).withStyle(ChatFormatting.YELLOW));
		}
	}

}
