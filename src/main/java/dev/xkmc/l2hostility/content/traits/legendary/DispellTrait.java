package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.ArrayList;
import java.util.List;

public class DispellTrait extends LegendaryTrait {

	public DispellTrait(ChatFormatting style) {
		super(style);
	}

	@Override
	public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
		event.enable(DefaultDamageState.BYPASS_MAGIC);
	}

	@Override
	public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache) {
		LivingEntity target = cache.getAttackTarget();
		List<ItemStack> list = new ArrayList<>();
		target.getArmorSlots().forEach(list::add);
		if (list.size() == 0) return;
		int index = attacker.getRandom().nextInt(list.size());
		int time = LHConfig.COMMON.dispellTime.get() * level;
		EnchantmentDisabler.disableEnchantment(attacker.level(), list.get(index), time);
	}

	@Override
	public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
		if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) &&
				event.getSource().is(L2DamageTypes.MAGIC)) {
			event.setCanceled(true);
		}
	}

	@Override
	public void addDetail(List<Component> list) {
		list.add(Component.translatable(getDescriptionId() + ".desc",
						mapLevel(i -> Component.literal(LHConfig.COMMON.dispellTime.get() * i / 20 + "")
								.withStyle(ChatFormatting.AQUA)))
				.withStyle(ChatFormatting.GRAY));
	}

}
