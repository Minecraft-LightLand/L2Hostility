package dev.xkmc.l2hostility.content.item.tool;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.effects.EffectBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitchWand extends Item {

	private static MobEffectInstance getRandom(int maxRank, RandomSource source) {
		var list = LHTraits.TRAITS.get().getValues().stream()
				.filter(e -> e instanceof TargetEffectTrait).toList();
		TargetEffectTrait trait = (TargetEffectTrait) list.get(source.nextInt(list.size()));
		int rank = Math.min(maxRank, trait.getConfig().max_rank);
		var ans = trait.func.apply(rank);
		return new EffectBuilder(ans).setDuration(ans.getDuration() * LHConfig.COMMON.witchWandFactor.get()).ins;
	}

	public WitchWand(Properties properties) {
		super(properties);
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		level.playSound(player, player.getX(), player.getY(), player.getZ(),
				SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL,
				0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			ThrownPotion entity = new ThrownPotion(level, player);
			ItemStack potion = new ItemStack(Items.SPLASH_POTION);
			int maxRank = PlayerDifficulty.HOLDER.get(player).maxRankKilled;
			MobEffectInstance ins = getRandom(maxRank, player.getRandom());
			PotionUtils.setCustomEffects(potion, List.of(ins));
			potion.getOrCreateTag().putInt("CustomPotionColor", ins.getEffect().getColor());
			entity.setItem(potion);
			entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
			level.addFreshEntity(entity);
			if (!player.getAbilities().instabuild) {
				stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(hand));
			}
		}
		player.getCooldowns().addCooldown(this, 60);
		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_WITCH_WAND.get().withStyle(ChatFormatting.GOLD));
	}

}
