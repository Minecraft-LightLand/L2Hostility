package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.MobEvents;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class ReflectTrait extends MobTrait {

	public ReflectTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
		if (event.getSource().getDirectEntity() instanceof LivingEntity le && !event.getSource().isMagic()) {
			if (CurioCompat.hasItem(le, LHItems.ABRAHADABRA.get())) return;
			float factor = (float) (level * LHConfig.COMMON.reflectFactor.get());
			MobEvents.schedule(() -> le.hurt(DamageSource.indirectMagic(entity, null), event.getAmount() * factor));
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
