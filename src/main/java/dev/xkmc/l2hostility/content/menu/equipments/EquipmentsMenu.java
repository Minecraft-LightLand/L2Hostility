package dev.xkmc.l2hostility.content.menu.equipments;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.PredSlot;
import dev.xkmc.l2library.base.menu.SpriteManager;
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
import java.util.function.BiPredicate;

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

	protected void addSlot(String name, BiPredicate<Integer, ItemStack> pred) {
		int current = this.added;
		this.sprite.getSlot(name, (x, y) -> {
			int i = this.added - current;
			PredSlot ans = new PredSlot(this.container, this.added, x, y, (e) -> pred.test(i, e));
			++this.added;
			return ans;
		}, this::addSlot);
	}

	private boolean isValid(EquipmentSlot slot, ItemStack stack) {
		if (golem == null || !stillValid(inventory.player)) {
			return false;
		}
		EquipmentSlot exp = LivingEntity.getEquipmentSlotForItem(stack);
		if (exp == slot) return true;
		return exp.getType() != EquipmentSlot.Type.ARMOR;
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
