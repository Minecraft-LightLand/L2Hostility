package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.traits.DurabilityEater;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ErosionTrait extends LegendaryTrait {

	public ErosionTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache) {
		LivingEntity target = cache.getAttackTarget();
		List<EquipmentSlot> list = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = target.getItemBySlot(slot);
			if (stack.isDamageableItem()) {
				list.add(slot);
			}
		}
		int count = Math.min(level, list.size());
		for (int i = 0; i < count; i++) {
			int index = attacker.getRandom().nextInt(list.size());
			DurabilityEater.erosion(target, list.remove(index));
		}
		if (count < level) {
			cache.addHurtModifier(DamageModifier.multTotal((float) (LHConfig.COMMON.erosionDamage.get() * level * (level - count))));
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal(i + "")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.erosionFactor.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.erosionDamage.get() * i * 100) + "%")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
