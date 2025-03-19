package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.item.consumable.BookCopy;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.mob_weapon_api.example.vanilla.VanillaMobManager;
import dev.xkmc.mob_weapon_api.init.MobWeaponAPI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

@EventBusSubscriber(modid = L2Hostility.MODID, bus = EventBusSubscriber.Bus.GAME)
public class MiscHandlers {

	public static void copyCap(LivingEntity self, LivingEntity sub) {
		if (LHMiscs.MOB.type().isProper(self) && LHMiscs.MOB.type().isProper(sub)) {
			var selfCap = LHMiscs.MOB.type().getOrCreate(self);
			var subCap = LHMiscs.MOB.type().getOrCreate(sub);
			subCap.copyFrom(self, sub, selfCap);
		}
	}

	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
		var e = event.getEntity();
		if (e instanceof ItemEntity ie) {
			if (ie.getItem().getEnchantmentLevel(LHEnchantments.VANISH.holder()) > 0) {
				event.setCanceled(true);
			}
		}
		if (e instanceof PathfinderMob mob && e.getTags().contains(MobWeaponAPI.MODID + "_applied")) {
			ItemStack stack = mob.getMainHandItem();
			VanillaMobManager.attachGoal(mob, stack);
		}
	}

	@SubscribeEvent
	public static void onAnvilCraft(AnvilUpdateEvent event) {
		ItemStack copy = event.getLeft();
		ItemStack book = event.getRight();
		if (copy.getItem() instanceof BookCopy && book.getItem() instanceof EnchantedBookItem) {
			var map = EnchantmentHelper.getEnchantmentsForCrafting(book);
			int cost = 0;
			for (var e : map.entrySet()) {
				cost += BookCopy.cost(e.getKey().value(), e.getIntValue());
			}
			ItemStack result = book.copy();
			if (!LHConfig.SERVER.bookOfReprintSpread.get())
				result.setCount(book.getCount() + copy.getCount());
			event.setOutput(result);
			event.setMaterialCost(book.getCount());
			event.setCost(cost);
		}
	}

	@SubscribeEvent
	public static void onAnvilTake(AnvilRepairEvent event) {
		ItemStack copy = event.getLeft();
		ItemStack book = event.getRight();
		if (copy.getItem() instanceof BookCopy && book.getItem() instanceof EnchantedBookItem) {
			if (LHConfig.SERVER.bookOfReprintSpread.get()) {
				for (int i = 0; i < copy.getCount(); i++) {
					ItemStack result = book.copy();
					result.setCount(1);
					event.getEntity().getInventory().placeItemBackInInventory(result);
				}
			}
		}
	}

	public static boolean useOnSkip(UseOnContext ctx, ItemStack stack) {
		Player player = ctx.getPlayer();
		if (player == null) return false;
		if (!ctx.getPlayer().hasEffect(LHEffects.ANTIBUILD)) return false;
		return stack.getItem() instanceof BlockItem || stack.is(LHTagGen.ANTIBUILD_BAN);
	}

	public static boolean predicateSlotValid(SlotContext slotContext, ItemStack stack) {
		var sealed = LHItems.DC_SEAL_STACK.get(stack);
		if (sealed == null) return false;
		return CuriosApi.isStackValid(slotContext, sealed.stack());
	}

}
