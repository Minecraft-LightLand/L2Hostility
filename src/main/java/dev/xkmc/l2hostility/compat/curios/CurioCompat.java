package dev.xkmc.l2hostility.compat.curios;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class CurioCompat {

	public static boolean hasItem(LivingEntity player, Item item) {
		for (EquipmentSlot e : EquipmentSlot.values()) {
			if (player.getItemBySlot(e).is(item)) {
				return true;
			}
		}
		if (ModList.get().isLoaded(CuriosApi.MODID)) {
			return hasItemImpl(player, item);
		}
		return false;
	}

	public static List<ItemStack> getItems(LivingEntity player, Predicate<ItemStack> pred) {
		List<ItemStack> ans = new ArrayList<>();
		for (EquipmentSlot e : EquipmentSlot.values()) {
			ItemStack stack = player.getItemBySlot(e);
			if (pred.test(stack)) {
				ans.add(stack);
			}
		}
		if (ModList.get().isLoaded(CuriosApi.MODID)) {
			getItemImpl(ans, player, pred);
		}
		return ans;
	}

	public static List<EntitySlotAccess> getItemAccess(LivingEntity player) {
		List<EntitySlotAccess> ans = new ArrayList<>();
		for (EquipmentSlot e : EquipmentSlot.values()) {
			ans.add(new EquipmentSlotAccess(player, e));
		}
		if (ModList.get().isLoaded(CuriosApi.MODID)) {
			getItemAccessImpl(ans, player);
		}
		return ans;
	}

	private static boolean hasItemImpl(LivingEntity player, Item item) {
		var opt = CuriosApi.getCuriosInventory(player);
		if (opt.resolve().isEmpty()) {
			return false;
		}
		for (var e : opt.resolve().get().getCurios().values()) {
			for (int i = 0; i < e.getStacks().getSlots(); i++) {
				if (e.getStacks().getStackInSlot(i).is(item)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void getItemImpl(List<ItemStack> list, LivingEntity player, Predicate<ItemStack> pred) {
		var opt = CuriosApi.getCuriosInventory(player);
		if (opt.resolve().isEmpty()) {
			return;
		}
		for (var e : opt.resolve().get().getCurios().values()) {
			for (int i = 0; i < e.getStacks().getSlots(); i++) {
				ItemStack stack = e.getStacks().getStackInSlot(i);
				if (pred.test(stack)) {
					list.add(stack);
				}
			}
		}
	}

	private static void getItemAccessImpl(List<EntitySlotAccess> list, LivingEntity player) {
		var opt = CuriosApi.getCuriosInventory(player);
		if (opt.resolve().isEmpty()) {
			return;
		}
		for (var e : opt.resolve().get().getCurios().values()) {
			for (int i = 0; i < e.getStacks().getSlots(); i++) {
				list.add(new CurioSlotAccess(player, e.getStacks(), i, e.getIdentifier()));
			}
		}
	}

	public static boolean isSlotAdder(EntitySlotAccess access) {
		if (!(access instanceof CurioSlotAccess slot)) return false;
		ItemStack stack = access.get();
		var opt = CuriosApi.getCurio(stack).resolve();
		if (opt.isEmpty()) return false;
		Multimap<Attribute, AttributeModifier> multimap =
				CuriosApi.getAttributeModifiers(new SlotContext(slot.id, slot.player, 0, false, true),
						UUID.randomUUID(), stack);
		for (var e : multimap.keySet()) {
			if (e instanceof SlotAttribute) {
				return true;
			}
		}
		return false;
	}

	private record CurioSlotAccess(LivingEntity player, IDynamicStackHandler handler, int slot,
								   String id) implements EntitySlotAccess {

		@Override
		public ItemStack get() {
			return handler.getStackInSlot(slot);
		}

		@Override
		public void set(ItemStack stack) {
			handler.setStackInSlot(slot, stack);
		}

	}

}
