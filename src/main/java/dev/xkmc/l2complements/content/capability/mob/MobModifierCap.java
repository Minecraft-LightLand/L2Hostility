package dev.xkmc.l2complements.content.capability.mob;

import dev.xkmc.l2complements.content.modifiers.core.MobModifierInstance;
import dev.xkmc.l2complements.init.L2Hostility;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.ArrayList;

@SerialClass
public class MobModifierCap extends GeneralCapabilityTemplate<LivingEntity, MobModifierCap> {

	public static final Capability<MobModifierCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final GeneralCapabilityHolder<LivingEntity, MobModifierCap> HOLDER =
			new GeneralCapabilityHolder<>(new ResourceLocation(L2Hostility.MODID, "modifiers"),
					CAPABILITY, MobModifierCap.class, MobModifierCap::new, LivingEntity.class, (e) -> e instanceof Enemy);

	@SerialClass.SerialField(toClient = true)
	public final ArrayList<MobModifierInstance> modifiers = new ArrayList<>();

	public MobModifierCap() {
	}

	public void syncToClient(LivingEntity entity) {
		L2Hostility.HANDLER.toTrackingPlayers(new CapSyncPacket(entity, this), entity);
	}

	public static void register() {
	}

}
