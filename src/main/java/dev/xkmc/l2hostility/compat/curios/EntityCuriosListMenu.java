package dev.xkmc.l2hostility.compat.curios;

import dev.xkmc.l2library.compat.curio.BaseCuriosListMenu;
import dev.xkmc.l2library.compat.curio.CuriosEventHandler;
import dev.xkmc.l2library.compat.curio.CuriosWrapper;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

public class EntityCuriosListMenu extends BaseCuriosListMenu<EntityCuriosListMenu> {

	@Nullable
	public static EntityCuriosListMenu fromNetwork(MenuType<EntityCuriosListMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int id = buf.readInt();
		int page = buf.readInt();
		ClientLevel level = Proxy.getClientWorld();
		assert level != null;
		Entity entity = level.getEntity(id);
		if (entity instanceof LivingEntity le && CuriosApi.getCuriosHelper().getCuriosHandler(le).resolve().isPresent())
			return new EntityCuriosListMenu(type, wid, plInv, new CuriosWrapper(le, page));
		return null;
	}

	protected EntityCuriosListMenu(MenuType<?> type, int wid, Inventory plInv, CuriosWrapper curios) {
		super(type, wid, plInv, curios);
	}

	@Override
	public void switchPage(ServerPlayer player, int i) {
		if (CuriosApi.getCuriosHelper().getCuriosHandler(curios.entity).resolve().isPresent()) {
			var pvd = new EntityCuriosMenuPvd(curios.entity, i);
			CuriosEventHandler.openMenuWrapped(player, () -> NetworkHooks.openScreen(player, pvd, pvd::writeBuffer));
		}
	}

}
