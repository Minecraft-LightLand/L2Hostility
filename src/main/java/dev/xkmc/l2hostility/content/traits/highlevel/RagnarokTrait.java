package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class RagnarokTrait extends MobTrait {

	public RagnarokTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void postHurt(int level, LivingEntity attacker, LivingEntity target) {
		List<EntitySlotAccess> list = CurioCompat.getItemAccess(target)
				.stream().filter(e -> !e.get().isEmpty() && !e.get().is(LHItems.SEAL.get())).toList();
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
