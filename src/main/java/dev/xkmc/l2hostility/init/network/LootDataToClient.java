package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

import java.util.ArrayList;
import java.util.List;

public record LootDataToClient(
		ArrayList<CompoundTag> list
) implements SerialPacketBase<LootDataToClient> {

	public static List<ITraitLootRecipe> LIST_CACHE = new ArrayList<>();

	public static LootDataToClient of(List<TraitLootModifier> list) {
		ArrayList<CompoundTag> ans = new ArrayList<>();
		for (var e : list) {
			var res = IGlobalLootModifier.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, e).result();
			if (res.isPresent() && res.get() instanceof CompoundTag ct) {
				ans.add(ct);
			}
		}
		return new LootDataToClient(ans);
	}

	@Override
	public void handle(Player player) {
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
