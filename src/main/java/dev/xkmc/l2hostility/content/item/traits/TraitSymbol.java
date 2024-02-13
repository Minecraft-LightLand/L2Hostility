package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TraitSymbol extends Item {

	private static boolean allow(Player player, MobTrait trait, LivingEntity target) {
		if (!LHConfig.COMMON.allowPlayerAllies.get() && target.isAlliedTo(player)) {
			return false;
		}
		if (!LHConfig.COMMON.allowTraitOnOwnable.get() && target instanceof OwnableEntity own &&
				own.getOwner() instanceof Player) {
			return false;
		}
		return trait.allow(target);
	}

	public TraitSymbol(Properties properties) {
		super(properties);
	}

	public MobTrait get() {
		var ans = LHTraits.TRAITS.get().getValue(ForgeRegistries.ITEMS.getKey(this));
		assert ans != null;
		return ans;
	}

	@Override
	protected String getOrCreateDescriptionId() {
		return Util.makeDescriptionId(LHTraits.TRAITS.key().location().getPath(), ForgeRegistries.ITEMS.getKey(this));
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (MobTraitCap.HOLDER.isProper(target)) {
			MobTraitCap cap = MobTraitCap.HOLDER.get(target);
			MobTrait trait = get();
			if (!allow(player, trait, target)) {
				if (player instanceof ServerPlayer sp) {
					sp.sendSystemMessage(LangData.MSG_ERR_DISALLOW.get().withStyle(ChatFormatting.RED), true);
				}
				return InteractionResult.FAIL;
			}
			if (cap.getTraitLevel(trait) >= trait.getMaxLevel()) {
				if (player instanceof ServerPlayer sp) {
					sp.sendSystemMessage(LangData.MSG_ERR_MAX.get().withStyle(ChatFormatting.RED), true);
				}
				return InteractionResult.FAIL;
			}
			if (player.level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			int val = cap.traits.compute(trait, (k, v) -> (v == null ? 0 : v) + 1);
			trait.initialize(target, val);
			trait.postInit(target, val);
			cap.syncToClient(target);
			target.setHealth(target.getMaxHealth());
			if (player instanceof ServerPlayer sp) {
				sp.sendSystemMessage(LangData.MSG_SET_TRAIT.get(trait.getDesc(), target.getDisplayName(), val), true);
				CriteriaTriggers.CONSUME_ITEM.trigger(sp, stack);
			}
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			return InteractionResult.SUCCESS;

		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(get().getFullDesc(null));
		if (get().isBanned()) {
			list.add(LangData.TOOLTIP_BANNED.get().withStyle(ChatFormatting.RED));
			return;
		}
		if (get() instanceof LegendaryTrait) {
			list.add(LangData.TOOLTIP_LEGENDARY.get().withStyle(ChatFormatting.GOLD));
		}
		if (Screen.hasShiftDown()) {
			var config = get().getConfig();
			list.add(LangData.TOOLTIP_MIN_LEVEL.get(Component.literal(config.min_level + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
			list.add(LangData.TOOLTIP_LEVEL_COST.get(Component.literal(config.cost + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
			list.add(LangData.TOOLTIP_WEIGHT.get(Component.literal(config.weight + "").withStyle(ChatFormatting.DARK_AQUA)).withStyle(ChatFormatting.DARK_GRAY));
		} else {
			get().addDetail(list);
			list.add(LangData.SHIFT.get().withStyle(ChatFormatting.GRAY));
		}
	}

}
