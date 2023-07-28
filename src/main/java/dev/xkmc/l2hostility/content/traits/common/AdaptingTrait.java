package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;

public class AdaptingTrait extends MobTrait {

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
			data.count++;
			double factor = Math.pow(LHConfig.COMMON.adaptFactor.get(), data.count);
			event.setAmount((float) (event.getAmount() * factor));
		} else {
			data.memory.add(0, id);
			if (data.memory.size() > level) {
				data.memory.remove(data.memory.size() - 1);
			}
		}
	}

	@SerialClass
	public static class Data extends CapStorageData {

		@SerialClass.SerialField
		public final ArrayList<String> memory = new ArrayList<>();

		@SerialClass.SerialField
		public int count;
	}

}
