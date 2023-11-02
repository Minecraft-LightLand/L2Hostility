package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class SlotIterateDamageTrait extends MobTrait {

	public SlotIterateDamageTrait(ChatFormatting format) {
		super(format);
	}

	@Override
	public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
		process(level, attacker, target);
	}

	protected int process(int level, LivingEntity attacker, LivingEntity target) {
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
			perform(target, list.remove(index));
		}
		return count;
	}

	protected abstract void perform(LivingEntity target, EquipmentSlot slot);

}
