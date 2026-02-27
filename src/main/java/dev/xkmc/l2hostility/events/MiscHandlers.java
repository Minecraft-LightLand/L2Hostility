package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.consumable.BookCopy;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.content.item.wand.IMobClickItem;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
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
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

@Mod.EventBusSubscriber(modid = L2Hostility.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiscHandlers {

	public static void copyCap(LivingEntity self, LivingEntity sub) {
		if (MobTraitCap.HOLDER.isProper(self) && MobTraitCap.HOLDER.isProper(sub)) {
			var selfCap = MobTraitCap.HOLDER.get(self);
			var subCap = MobTraitCap.HOLDER.get(sub);
			subCap.copyFrom(self, sub, selfCap);
		}
	}

	@SubscribeEvent
	public static void onTargetCardClick(PlayerInteractEvent.EntityInteract event) {
		if (event.getItemStack().getItem() instanceof IMobClickItem) {
			if (event.getTarget() instanceof LivingEntity le) {
				event.setCancellationResult(event.getItemStack().interactLivingEntity(event.getEntity(),
						le, event.getHand()));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
		var e = event.getEntity();
		if (e instanceof ItemEntity ie) {
			if (ie.getItem().getEnchantmentLevel(LHEnchantments.VANISH.get()) > 0) {
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
			var map = EnchantmentHelper.getEnchantments(book);
			for (var e : map.keySet()) {
				var holder = ForgeRegistries.ENCHANTMENTS.getHolder(e);
				if (holder.isEmpty() || holder.get().is(LHTagGen.NO_REPRINT)) return;
			}
			int cost = 0;
			for (var e : map.entrySet()) {
				cost += BookCopy.cost(e.getKey(), e.getValue());
			}
			ItemStack result = book.copy();
			if (!LHConfig.COMMON.bookOfReprintSpread.get())
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
			if (LHConfig.COMMON.bookOfReprintSpread.get()) {
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
		if (!ctx.getPlayer().hasEffect(LHEffects.ANTIBUILD.get())) return false;
		return stack.getItem() instanceof BlockItem || stack.is(LHTagGen.ANTIBUILD_BAN);
	}

	public static boolean predicateSlotValid(SlotContext slotContext, ItemStack stack) {
		if (!stack.hasTag() || stack.getTagElement(SealedItem.DATA) == null) return false;
		var ctag = stack.getOrCreateTag().getCompound(SealedItem.DATA);
		ItemStack content = ItemStack.of(ctag);
		return CuriosApi.isStackValid(slotContext, content);
	}

}
