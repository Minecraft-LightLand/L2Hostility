package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2core.events.SchedulerHandler;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ReflectTrait extends MobTrait {

	public ReflectTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onHurtByMax(int level, LivingEntity entity, DamageData.OffenceMax event) {
		if (event.getSource().getDirectEntity() instanceof LivingEntity le && event.getSource().is(L2DamageTypes.DIRECT)) {
			if (LHItems.ABRAHADABRA.get().isOn(le)) return;
			float factor = (float) (level * LHConfig.SERVER.reflectFactor.get());
			SchedulerHandler.schedule(() -> le.hurt(entity.level().damageSources().indirectMagic(entity, null), event.getDamageIncoming() * factor));
		}
	}

	@Override
	public void addDetail(RegistryAccess access, List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(access, i -> Component.literal((int) Math.round(100 * (i * LHConfig.SERVER.reflectFactor.get())) + "")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}


}
