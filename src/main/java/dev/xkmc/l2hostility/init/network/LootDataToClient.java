package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class LootDataToClient extends SerialPacketBase {

	public static List<TraitLootModifier> LIST_CACHE = new ArrayList<>();

	@SerialClass.SerialField
	public ArrayList<CompoundTag> list = new ArrayList<>();

	@Deprecated
	public LootDataToClient() {
	}

	public LootDataToClient(List<TraitLootModifier> list) {
		for (var e : list) {
			var res = IGlobalLootModifier.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, e).result();
			if (res.isPresent() && res.get() instanceof CompoundTag ct) {
				this.list.add(ct);
			}
		}
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		LIST_CACHE = new ArrayList<>();
		for (var ct : list) {
			var ans = IGlobalLootModifier.DIRECT_CODEC.decode(NbtOps.INSTANCE, ct).result();
			if (ans.isEmpty()) continue;
			if (ans.get().getFirst() instanceof TraitLootModifier mod) {
				LIST_CACHE.add(mod);
			}
		}
	}

}
