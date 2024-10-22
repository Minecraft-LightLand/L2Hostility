package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class BookEverything extends Item {

	@Nullable
	private static Holder<Enchantment> getEnch(ItemStack stack) {
		String name = stack.getHoverName().getString();
		try {
			ResourceLocation rl = ResourceLocation.parse(name.trim());
			if (!ForgeRegistries.ENCHANTMENTS.containsKey(rl))
				return null;
			return ForgeRegistries.ENCHANTMENTS.getValue(rl);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean allow(Holder<Enchantment> holder) {
		if (holder.is(EnchantmentTags.TREASURE) || !holder.is(EnchantmentTags.IN_ENCHANTING_TABLE))
			return false;
		var ench = holder.value();
		return ench.getMaxCost(ench.getMaxLevel()) >= ench.getMinCost(ench.getMaxLevel());
	}

	public static int cost(Enchantment ench) {
		return Math.max(ench.getMaxLevel(), ench.getMaxCost(ench.getMaxLevel()) / 10);
	}

	public BookEverything(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
				ListTag listtag = EnchantedBookItem.getEnchantments(result);
				for (var e : ForgeRegistries.ENCHANTMENTS.getEntries()) {
					if (allow(e.getValue())) {
						listtag.add(EnchantmentHelper.storeEnchantment(e.getKey().location(), e.getValue().getMaxLevel()));
					}
				}
				result.getOrCreateTag().put("StoredEnchantments", listtag);
				stack.shrink(1);
				player.getInventory().placeItemBackInInventory(result);
			}
			return InteractionResultHolder.consume(stack);
		} else {
			Enchantment ench = getEnch(stack);
			if (ench == null) {
				return InteractionResultHolder.fail(stack);
			}
			if (!allow(ench)) {
				return InteractionResultHolder.fail(stack);
			}
			int cost = cost(ench);
			if (player.experienceLevel < cost) {
				return InteractionResultHolder.fail(stack);
			}
			if (!level.isClientSide()) {
				player.giveExperienceLevels(-cost);
				EnchantmentInstance ins = new EnchantmentInstance(ench, ench.getMaxLevel());
				ItemStack result = EnchantedBookItem.createForEnchantment(ins);
				player.getInventory().placeItemBackInInventory(result);
			}
			return InteractionResultHolder.success(stack);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.ITEM_BOOK_EVERYTHING_USE.get().withStyle(ChatFormatting.GRAY));
		list.add(LangData.ITEM_BOOK_EVERYTHING_SHIFT.get().withStyle(ChatFormatting.GOLD));
		Holder<Enchantment> holder = getEnch(stack);
		if (holder == null) {
			list.add(LangData.ITEM_BOOK_EVERYTHING_INVALID.get().withStyle(ChatFormatting.GRAY));
		} else {
			var e = holder.value();
			var name = Enchantment.getFullname(holder, e.getMaxLevel());
			if (allow(holder)) {
				int cost = cost(e);
				list.add(LangData.ITEM_BOOK_EVERYTHING_READY.get(name, cost).withStyle(ChatFormatting.GREEN));
			} else {
				list.add(LangData.ITEM_BOOK_EVERYTHING_FORBIDDEN.get(name).withStyle(ChatFormatting.RED));
			}
		}
	}
}
