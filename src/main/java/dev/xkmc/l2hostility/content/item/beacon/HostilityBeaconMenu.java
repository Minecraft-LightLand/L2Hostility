package dev.xkmc.l2hostility.content.item.beacon;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.Optional;

public class HostilityBeaconMenu extends AbstractContainerMenu {
   private final Container beacon = new SimpleContainer(1) {
      public boolean canPlaceItem(int p_39066_, ItemStack p_39067_) {
         return p_39067_.is(ItemTags.BEACON_PAYMENT_ITEMS);
      }

      public int getMaxStackSize() {
         return 1;
      }
   };
   private final HostilityBeaconMenu.PaymentSlot paymentSlot;
   private final ContainerLevelAccess access;
   private final ContainerData beaconData;


   public HostilityBeaconMenu(MenuType<HostilityBeaconMenu> type, int id, Inventory inv, @Nullable FriendlyByteBuf data) {
      this(type, id, inv, new SimpleContainerData(2), ContainerLevelAccess.NULL);
   }

   public HostilityBeaconMenu(MenuType<HostilityBeaconMenu> type, int id, Inventory cont, ContainerData data, ContainerLevelAccess access) {
      super(type, id);
      checkContainerDataCount(data, 3);
      this.beaconData = data;
      this.access = access;
      this.paymentSlot = new PaymentSlot(this.beacon, 0, 136, 110);
      this.addSlot(this.paymentSlot);
      this.addDataSlots(data);
      int i = 36;
      int j = 137;

      for (int k = 0; k < 3; ++k) {
         for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(cont, l + k * 9 + 9, i + l * 18, j + k * 18));
         }
      }

      for (int i1 = 0; i1 < 9; ++i1) {
         this.addSlot(new Slot(cont, i1, i + i1 * 18, 195));
      }

   }

   public void removed(Player p_39049_) {
      super.removed(p_39049_);
      if (!p_39049_.level().isClientSide) {
         ItemStack itemstack = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());
         if (!itemstack.isEmpty()) {
            p_39049_.drop(itemstack, false);
         }

      }
   }

   public boolean stillValid(Player p_39047_) {
      return stillValid(this.access, p_39047_, Blocks.BEACON);
   }

   public void setData(int p_39044_, int p_39045_) {
      super.setData(p_39044_, p_39045_);
      this.broadcastChanges();
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack ans = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack slotItem = slot.getItem();
         ans = slotItem.copy();
         if (index == 0) {
            if (!this.moveItemStackTo(slotItem, 1, 37, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(slotItem, ans);
         } else if (this.moveItemStackTo(slotItem, 0, 1, false)) {
            return ItemStack.EMPTY;
         } else if (index >= 1 && index < 28) {
            if (!this.moveItemStackTo(slotItem, 28, 37, false)) {
               return ItemStack.EMPTY;
            }
         } else if (index >= 28 && index < 37) {
            if (!this.moveItemStackTo(slotItem, 1, 28, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(slotItem, 1, 37, false)) {
            return ItemStack.EMPTY;
         }

         if (slotItem.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (slotItem.getCount() == ans.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(player, slotItem);
      }

      return ans;
   }

   public int getLevels() {
      return this.beaconData.get(0);
   }

   public int getPrimaryEffect() {
      return this.beaconData.get(1);
   }

   public void updateEffects(Optional<MobEffect> opt) {
      if (this.paymentSlot.hasItem()) {
         this.beaconData.set(1, opt.map(MobEffect::getId).orElse(-1));
         this.paymentSlot.remove(1);
         this.access.execute(Level::blockEntityChanged);
      }

   }

   public boolean hasPayment() {
      return !this.beacon.getItem(0).isEmpty();
   }

   static class PaymentSlot extends Slot {
      public PaymentSlot(Container p_39071_, int p_39072_, int p_39073_, int p_39074_) {
         super(p_39071_, p_39072_, p_39073_, p_39074_);
      }

      public boolean mayPlace(ItemStack p_39077_) {
         return p_39077_.is(ItemTags.BEACON_PAYMENT_ITEMS);
      }

      public int getMaxStackSize() {
         return 1;
      }
   }
}
