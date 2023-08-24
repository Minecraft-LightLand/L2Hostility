package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdaptingTrait extends MobTrait {

	public AdaptingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) ||
				event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS))
			return;
		MobTraitCap cap = MobTraitCap.HOLDER.get(entity);
		Data data = cap.getOrCreateData(getRegistryName(), Data::new);
		String id = event.getSource().getMsgId();
		if (data.memory.contains(id)) {
			data.memory.remove(id);
			data.memory.add(0, id);
			int val = data.adaption.compute(id, (k, old) -> old == null ? 1 : old + 1);
			double factor = Math.pow(LHConfig.COMMON.adaptFactor.get(), val - 1);
			event.setAmount((float) (event.getAmount() * factor));
		} else {
			data.memory.add(0, id);
			data.adaption.put(id, 1);
			if (data.memory.size() > level) {
				String old = data.memory.remove(data.memory.size() - 1);
				data.adaption.remove(old);
			}
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal((int) Math.round(100 * (1 - LHConfig.COMMON.adaptFactor.get())) + "")
								.withStyle(ChatFormatting.AQUA),
						mapLevel(i -> Component.literal("" + i)
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}


	@SerialClass
	public static class Data extends CapStorageData {

		@SerialClass.SerialField
		public final ArrayList<String> memory = new ArrayList<>();

		@SerialClass.SerialField
		public final HashMap<String, Integer> adaption = new HashMap<>();

	}


}
