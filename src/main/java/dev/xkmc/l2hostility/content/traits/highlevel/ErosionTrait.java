package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.traits.DurabilityEater;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ErosionTrait extends SlotIterateDamageTrait {

	public ErosionTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, DamageData.Offence cache, TraitEffectCache traitCache) {
		if (LHItems.ABRAHADABRA.get().isOn(cache.getTarget())) return;
		int count = process(level, attacker, cache.getTarget());
		if (count < level) {
			cache.addHurtModifier(DamageModifier.multTotal((float) (LHConfig.SERVER.erosionDamage.get() * level * (level - count)), getRegistryName()));
		}
	}

	@Override
	protected void perform(LivingEntity target, EquipmentSlot slot) {
		DurabilityEater.erosion(target, slot);
	}

	@Override
	public void addDetail(RegistryAccess access, List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(access, i -> Component.literal(i + "")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(access, i -> Component.literal(Math.round(LHConfig.SERVER.erosionDurability.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(access, i -> Component.literal(Math.round(LHConfig.SERVER.erosionDamage.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
