package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class ReflectTrait extends MobTrait {

	public ReflectTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
		if (event.getSource().getDirectEntity() instanceof LivingEntity le && event.getSource().is(L2DamageTypes.DIRECT)) {
			if (LHItems.ABRAHADABRA.get().isOn(le)) return;
			float factor = (float) (level * LHConfig.COMMON.reflectFactor.get());
			GeneralEventHandler.schedule(() -> le.hurt(entity.level().damageSources().indirectMagic(null, entity), event.getAmount() * factor));
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal((int) Math.round(100 * (i * LHConfig.COMMON.reflectFactor.get())) + "")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}


}
