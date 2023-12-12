package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2hostility.backport.damage.DamageModifier;
import dev.xkmc.l2hostility.content.item.traits.DurabilityEater;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ErosionTrait extends SlotIterateDamageTrait {

	public ErosionTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
		int count = process(level, attacker, cache.getAttackTarget());
		if (count < level) {
			DamageModifier.hurtMultTotal(cache, (float) (LHConfig.COMMON.erosionDamage.get() * level * (level - count)));
		}
	}

	@Override
	protected void perform(LivingEntity target, EquipmentSlot slot) {
		DurabilityEater.erosion(target, slot);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal(i + "")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.erosionDurability.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.erosionDamage.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
