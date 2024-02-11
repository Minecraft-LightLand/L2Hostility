package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.curio.core.ISimpleCapItem;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class PocketOfRestoration extends CurioItem implements ISimpleCapItem {

	public static final String ROOT = "UnsealRoot", KEY = "SealedSlotKey", START = "UnsealStartTime";

	public static void setData(ItemStack stack, ItemStack sealed, String id, long time) {
		var data = sealed.getOrCreateTag().get(SealedItem.DATA);
		if (data == null) return;
		var tag = stack.getOrCreateTagElement(ROOT);
		tag.putInt(SealedItem.TIME, sealed.getOrCreateTag().getInt(SealedItem.TIME));
		tag.put(SealedItem.DATA, data);
		tag.putString(KEY, id);
		tag.putLong(START, time);
	}

	public PocketOfRestoration(Properties properties, int durability) {
		super(properties, durability);
	}

	@Override
	public void curioTick(ItemStack stack, SlotContext slotContext) {
		var le = slotContext.entity();
		if (le.level().isClientSide) return;
		var list = CurioCompat.getItemAccess(le);

		if (stack.getTag() != null && stack.getTag().contains(ROOT)) {
			var tag = stack.getOrCreateTagElement(ROOT);
			long time = tag.getLong(START);
			int dur = tag.getInt(SealedItem.TIME);
			if (le.level().getGameTime() >= time + dur) {
				ItemStack result = ItemStack.of(tag.getCompound(SealedItem.DATA));
				EntitySlotAccess slot = CurioCompat.decode(tag.getString(KEY), le);
				if (slot != null && slot.get().isEmpty()) {
					slot.set(result);
					stack.getTag().remove(ROOT);
				} else if (le instanceof Player player && player.addItem(result)) {
					stack.getTag().remove(ROOT);
				}

			}
			return;
		}
		if (stack.getDamageValue() + 1 >= stack.getMaxDamage())
			return;
		for (var e : list) {
			if (e.get().getItem() instanceof SealedItem) {
				ItemStack sealed = e.get();
				e.set(ItemStack.EMPTY);
				String id = e.getID();
				long time = le.level().getGameTime();
				stack.hurtAndBreak(1, le, x -> {
				});
				setData(stack, sealed, id, time);
				return;
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.POCKET_OF_RESTORATION.get().withStyle(ChatFormatting.GOLD));
		if (stack.getTag() != null && stack.getTag().contains(ROOT)) {
			list.add(LangData.TOOLTIP_SEAL_DATA.get().withStyle(ChatFormatting.GRAY));
			list.add(ItemStack.of(stack.getOrCreateTagElement(ROOT).getCompound(SealedItem.DATA)).getHoverName());
		}

	}

}
