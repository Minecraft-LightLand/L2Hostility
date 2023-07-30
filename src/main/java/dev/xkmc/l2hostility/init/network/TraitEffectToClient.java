package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class TraitEffectToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public int id;

	@SerialClass.SerialField
	public MobTrait trait;

	@SerialClass.SerialField
	public TraitEffects effect;

	@Deprecated
	public TraitEffectToClient() {
	}

	public TraitEffectToClient(LivingEntity e, MobTrait trait, TraitEffects effect) {
		id = e.getId();
		this.trait = trait;
		this.effect = effect;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientSyncHandler.handleEffect(id, trait, effect);
	}

}
