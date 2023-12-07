package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RagnarokTrait extends LegendaryTrait {

	private static boolean allowSeal(EntitySlotAccess access) {
		ItemStack stack = access.get();
		if (stack.isEmpty()) return false;
		if (stack.is(LHItems.SEAL.get())) return false;
		if (stack.is(LHTagGen.NO_SEAL)) return false;
		if (!LHConfig.COMMON.ragnarokSealBackpack.get()) {
			var rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
			if (rl == null) return false;
			if (rl.toString().contains("backpack")) return false;
		}
		if (!LHConfig.COMMON.ragnarokSealSlotAdder.get()) {
			return !CurioCompat.isSlotAdder(access);
		}
		return true;
	}

	public RagnarokTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
		List<EntitySlotAccess> list = new ArrayList<>(CurioCompat.getItemAccess(target)
				.stream().filter(RagnarokTrait::allowSeal).toList());
		int count = Math.min(level, list.size());
		int time = LHConfig.COMMON.ragnarokTime.get() * level;
		for (int i = 0; i < count; i++) {
			int index = attacker.getRandom().nextInt(list.size());
			EntitySlotAccess slot = list.remove(index);
			slot.modify(e -> SealedItem.sealItem(e, time));
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal(i + "")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(i -> Component.literal("" + Math.round(LHConfig.COMMON.ragnarokTime.get() * i / 20f))
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
