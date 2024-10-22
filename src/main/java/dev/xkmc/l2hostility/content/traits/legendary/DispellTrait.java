package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class DispellTrait extends LegendaryTrait {

	public DispellTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
		if (event.getResult() == L2DamageTypes.MOB_ATTACK)
			event.enable(DefaultDamageState.BYPASS_MAGIC);
	}

	@Override
	public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
		List<ItemStack> list = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = target.getItemBySlot(slot);
			if (stack.isEnchanted() && !(stack.getOrCreateTag().contains("l2hostility_enchantment"))) {
				list.add(stack);
			}
		}
		if (list.isEmpty()) return;
		int time = LHConfig.SERVER.dispellTime.get() * level;
		int count = Math.min(level, list.size());
		for (int i = 0; i < count; i++) {
			int index = attacker.getRandom().nextInt(list.size());
			EnchantmentDisabler.disableEnchantment(attacker.level(), list.remove(index), time);
		}
	}

	@Override
	public boolean onAttackedByOthers(int level, LivingEntity entity, DamageData.Attack event) {
		return !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				event.getSource().is(Tags.DamageTypes.IS_MAGIC);
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal(i + "")
								.withStyle(ChatFormatting.AQUA)),
						mapLevel(i -> Component.literal(LHConfig.SERVER.dispellTime.get() * i / 20 + "")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
