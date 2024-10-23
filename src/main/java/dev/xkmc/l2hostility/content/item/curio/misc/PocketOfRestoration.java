package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.curio.core.SingletonItem;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.function.Consumers;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class PocketOfRestoration extends SingletonItem implements ICurioItem {

	public static void setData(ItemStack stack, ItemStack sealed, String id, long time) {
		var data = LHItems.DC_SEAL_STACK.get(sealed);
		if (data == null) return;
		LHItems.DC_SEAL_STACK.set(stack, data);
		LHItems.DC_SEAL_TIME.set(stack, LHItems.DC_SEAL_TIME.getOrDefault(sealed, 0));
		LHItems.DC_UNSEAL_START.set(stack, time);
		LHItems.DC_UNSEAL_SLOT.set(stack, id);
	}

	public PocketOfRestoration(Properties properties, int durability) {
		super(properties.durability(durability));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		var le = slotContext.entity();
		if (!(le.level() instanceof ServerLevel sl)) return;
		if (!slotContext.entity().isAlive()) return;
		var list = CurioCompat.getItemAccess(le);
		var seal = LHItems.DC_SEAL_STACK.get(stack);
		if (seal != null) {
			long time = LHItems.DC_UNSEAL_START.getOrDefault(stack, 0L);
			int dur = LHItems.DC_SEAL_TIME.getOrDefault(stack, 0);
			String str = LHItems.DC_UNSEAL_SLOT.get(stack);
			if (le.level().getGameTime() >= time + dur && str != null) {
				ItemStack result = seal.stack();
				EntitySlotAccess slot = CurioCompat.decode(str, le);
				if (slot != null && slot.get().isEmpty()) {
					slot.set(result);
					stack.remove(LHItems.DC_SEAL_STACK);
					stack.remove(LHItems.DC_SEAL_TIME);
					stack.remove(LHItems.DC_UNSEAL_START);
					stack.remove(LHItems.DC_UNSEAL_SLOT);
				} else if (le instanceof Player player && player.addItem(result)) {
					stack.remove(LHItems.DC_SEAL_STACK);
					stack.remove(LHItems.DC_SEAL_TIME);
					stack.remove(LHItems.DC_UNSEAL_START);
					stack.remove(LHItems.DC_UNSEAL_SLOT);
				}
			}
			return;
		}
		if (stack.getDamageValue() + 1 >= stack.getMaxDamage())
			return;
		for (var e : list) {
			if (e.get().getItem() instanceof SealedItem) {
				ItemStack item = e.get();
				e.set(ItemStack.EMPTY);
				String id = e.getID();
				long time = le.level().getGameTime();
				stack.hurtAndBreak(1, sl, le, Consumers.nop());
				setData(stack, item, id, time);
				return;
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.POCKET_OF_RESTORATION.get().withStyle(ChatFormatting.GOLD));
		var seal = LHItems.DC_SEAL_STACK.get(stack);
		if (seal != null) {
			list.add(LangData.TOOLTIP_SEAL_DATA.get().withStyle(ChatFormatting.GRAY));
			list.add(seal.stack().getHoverName());
		}
	}

}
