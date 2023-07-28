package dev.xkmc.l2hostility.content.menu.equipments;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class EquipmentsMenu extends BaseContainerMenu<EquipmentsMenu> {

	public static EquipmentsMenu fromNetwork(MenuType<EquipmentsMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		Entity entity = Proxy.getClientWorld().getEntity(buf.readInt());
		return new EquipmentsMenu(type, wid, plInv, entity instanceof Mob golem ? golem : null);
	}

	public static EquipmentSlot[] SLOTS = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	public static final SpriteManager MANAGER = new SpriteManager(L2Hostility.MODID, "equipments");

	@Nullable
	protected final Mob golem;

	protected EquipmentsMenu(MenuType<?> type, int wid, Inventory plInv, @Nullable Mob golem) {
		super(type, wid, plInv, MANAGER, EquipmentsContainer::new, false);
		this.golem = golem;
		addSlot("hand", (i, e) -> isValid(SLOTS[i], e));
		addSlot("armor", (i, e) -> isValid(SLOTS[i + 2], e));
	}

	private boolean isValid(EquipmentSlot slot, ItemStack stack) {
		if (golem == null || !stillValid(inventory.player)) {
			return false;
		}
		return LivingEntity.getEquipmentSlotForItem(stack) == slot;
	}

	@Override
	public boolean stillValid(Player player) {
		return golem != null && !golem.isRemoved();
	}

	@Override
	public PredSlot getAsPredSlot(String name, int i, int j) {
		return super.getAsPredSlot(name, i, j);
	}

	@Override
	public ItemStack quickMoveStack(Player pl, int id) {
		if (golem != null) {
			ItemStack stack = this.slots.get(id).getItem();
			if (id >= 36) {
				this.moveItemStackTo(stack, 0, 36, true);
			} else {
				for (int i = 0; i < 6; i++) {
					if (SLOTS[i] == LivingEntity.getEquipmentSlotForItem(stack)) {
						this.moveItemStackTo(stack, 36 + i, 37 + i, false);
					}
				}
			}
			this.container.setChanged();
		}
		return ItemStack.EMPTY;
	}
}
