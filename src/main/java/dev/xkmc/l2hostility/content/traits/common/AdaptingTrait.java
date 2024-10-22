package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdaptingTrait extends MobTrait {

	public AdaptingTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onDamaged(int level, LivingEntity entity, DamageData.Defence event) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) ||
				event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS))
			return;
		MobTraitCap cap = LHMiscs.MOB.type().getOrCreate(entity);
		Data data = cap.getOrCreateData(getRegistryName(), Data::new);
		String id = event.getSource().getMsgId();
		if (data.memory.contains(id)) {
			data.memory.remove(id);
			data.memory.addFirst(id);
			int val = data.adaption.compute(id, (k, old) -> old == null ? 1 : old + 1);
			double factor = Math.pow(LHConfig.SERVER.adaptFactor.get(), val - 1);
			event.addDealtModifier(DamageModifier.multTotal((float) factor, getRegistryName()));
		} else {
			data.memory.addFirst(id);
			data.adaption.put(id, 1);
			if (data.memory.size() > level) {
				String old = data.memory.removeLast();
				data.adaption.remove(old);
			}
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						Component.literal((int) Math.round(100 * (1 - LHConfig.SERVER.adaptFactor.get())) + "")
								.withStyle(ChatFormatting.AQUA),
						mapLevel(i -> Component.literal("" + i)
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}


	@SerialClass
	public static class Data extends CapStorageData {

		@SerialField
		public final ArrayList<String> memory = new ArrayList<>();

		@SerialField
		public final HashMap<String, Integer> adaption = new HashMap<>();

	}


}
